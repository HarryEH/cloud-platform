package com.howarth.cloud.mainapp.library.controllers;

import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.*;
import com.howarth.cloud.mainapp.library.database.DiamondRepository;
import com.howarth.cloud.mainapp.library.database.IcRepository;
import com.howarth.cloud.mainapp.library.database.RhhRepository;
import com.howarth.cloud.mainapp.security.SecurityConstants;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.core.env.Environment;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.howarth.cloud.mainapp.security.SecurityConstants.ACCESS_TOKEN;

@Controller
public class LibraryViewController {
//    private String appMode;

    private final String regexPattern = "([Gg][Ii][Rr] 0[Aa]{2})|((([A-Za-z][0-9]{1,2})|(([A-Za-z][A-Ha-hJ-Yj-y][0-9]{1,2})|(([A-Za-z][0-9][A-Za-z])|([A-Za-z][A-Ha-hJ-Yj-y][0-9][A-Za-z]?))))\\s?[0-9][A-Za-z]{2})";
    private final String diamondIcPostcode = "S3+7RD";
    private final String rhhPostcode = "S10+2JF";
    private final String API_KEY = "AIzaSyDCIl9nnsYUy9BfozMtZY3YO3C-1Jm6noQ";

    private DiamondRepository diamondRepository;
    private IcRepository icRepository;
    private RhhRepository rhhRepository;

    public LibraryViewController(Environment environment, DiamondRepository diamondRepository, IcRepository icRepository, RhhRepository rhhRepository) {
        this.diamondRepository = diamondRepository;
        this.icRepository = icRepository;
        this.rhhRepository = rhhRepository;
    }

    @GetMapping("/library")
    public String index(Model model, HttpServletRequest request) {
        return libraryPage(model, request);
    }

    @GetMapping("/library/")
    public String lib(Model model, HttpServletRequest request) {
        return libraryPage(model, request);
    }

    private String libraryPage(Model model, HttpServletRequest request) {
        informPaymentServer(request);

        model.addAttribute("diamond", diamondRepository.findTopByOrderByIdDesc().getNumberOfPeople());
        model.addAttribute("ic", icRepository.findTopByOrderByIdDesc().getNumberOfPeople());
        model.addAttribute("rhh", rhhRepository.findTopByOrderByIdDesc().getNumberOfPeople());
        return "library";
    }

    @GetMapping("/library/directions")
    public String directionsToLibrary(Model model, HttpServletRequest request,
                                      @Param("library") String library, @Param("postcode") String postcode) {


        // Create a Pattern object
        Pattern r = Pattern.compile(regexPattern);

        final String postcodeMatch;

        // Now create matcher object.
        Matcher m = r.matcher(postcode.replace(" ", ""));

        if (m.find()) {
            postcodeMatch = m.group(0);
        } else {
            return "error";
        }

        if (library.equals("diamond") || library.equals("ic")) {
            if (sendUserDirections(model, postcodeMatch, diamondIcPostcode)) return "directions";
        }

        if (library.equals("rhh")) {
            if (sendUserDirections(model, postcodeMatch, rhhPostcode)) return "directions";
        }

        return "error";
    }

    private boolean sendUserDirections(Model model, String postcodeMatch, String diamondIcPostcode) {
        try {
            DirectionsStep[] steps = googleDirections(postcodeMatch, diamondIcPostcode);

            model.addAttribute("directions", steps);

            return true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ApiException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private DirectionsStep[] googleDirections(final String origin, final String destination) throws InterruptedException, ApiException, IOException {
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey(API_KEY)
                .build();

        DirectionsResult result = DirectionsApi.newRequest(context)
                .mode(TravelMode.WALKING)
                .units(Unit.METRIC)
                .region("uk")
                .origin(origin)
                .destination(destination)
                .await();

        for( DirectionsRoute r : result.routes) {
            System.out.println(r.summary);
            for (DirectionsLeg leg : r.legs) {
                return leg.steps;
            }
        }
        return null;
    }


    private void informPaymentServer(HttpServletRequest request) {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        try {

            // This automatically gets the correct URL to sent the request to.
            final String URL = request.getScheme() + "://" + request.getServerName() +
                    ("http".equals(request.getScheme()) && request.getServerPort() == 80 || "https".equals(request.getScheme()) && request.getServerPort() == 443 ? "" : ":" + request.getServerPort()) +
                    SecurityConstants.NEW_USAGE + "?app_name=library&access_token=";

            //access token
            String token = "x.y.z";

            for (Cookie c : request.getCookies()) {
                if (c.getName().equals(ACCESS_TOKEN)) {
                    token = c.getValue();
                }
            }

            final String FINAL_URL = URL + token;

            HttpGet req = new HttpGet(FINAL_URL);

            httpClient.execute(req);

            httpClient.close();
        } catch (Exception ex) {
            // handle exception here
        }
    }
}
