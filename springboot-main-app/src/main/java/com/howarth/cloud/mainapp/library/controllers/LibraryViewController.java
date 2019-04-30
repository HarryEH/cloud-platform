package com.howarth.cloud.mainapp.library.controllers;

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
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import static com.howarth.cloud.mainapp.security.SecurityConstants.ACCESS_TOKEN;

@Controller
public class LibraryViewController {
//    private String appMode;

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
