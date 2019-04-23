package com.howarth.cloud.mainapp.user;

import com.howarth.cloud.mainapp.security.SecurityConstants;
import com.howarth.cloud.mainapp.security.VerifiedToken;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;
import org.springframework.data.repository.query.Param;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

import static com.howarth.cloud.mainapp.security.JWTAuthorizationFilter.verifyToken;

@RestController
@RequestMapping("/users")
public class UserController {

    private ApplicationUserRepository applicationUserRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserController(ApplicationUserRepository applicationUserRepository,
                          BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.applicationUserRepository = applicationUserRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }


    /**
     * Mapping for user sign up. This saves the ApplicationUser in the JPA repository
     * @param user a user object with a username and password
     * @return
     */
    @PostMapping("/sign-up")
    public ApplicationUser signUp(@RequestBody ApplicationUser user) {
        if (applicationUserRepository.findByUsername(user.getUsername()) != null) {
            return null;
        }
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        applicationUserRepository.save(user);

        JSONObject json = new JSONObject();
        json.put("username", user.getUsername());
        json.put("balance", 0);

        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        try {
            HttpPost request = new HttpPost("http://localhost:8080" + SecurityConstants.CREATE_ACCOUNT);
            StringEntity params = new StringEntity(json.toString());
            request.addHeader("content-type", "application/json");
            request.setEntity(params);
            CloseableHttpResponse response = httpClient.execute(request);
            // handle response here...
        } catch (Exception ex) {
            // handle exception here
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return user;
    }


    /**
     * This mapping is for verifying JWT access tokens. It uses the static
     * function verifyToken found in the AuthorizationFilter.
     *
     * @param access_token
     * @return VerifiedToken object, this will be JSON. It informs the caller
     * if the token is valid and if it is it returns the username
     */
    @GetMapping("/verify_token")
    public VerifiedToken verify(@Param("access_token") String access_token) {

        System.out.println("\n\n"+access_token+"\n\n");

        try {
            String user = verifyToken(access_token, SecurityConstants.SECRET, "");
            return new VerifiedToken(user, user != null);
        } catch (NullPointerException ex) {
            return new VerifiedToken("-", false);
        }
    }

    /**
     * This returns a list of all the users of the site
     * FIXME: remove this
     * @return List of ApplicationUser that includes all the signed up users of the platform
     */
    @GetMapping("/all")
    public List<ApplicationUser> all() {
        return applicationUserRepository.findAll();
    }
}