package com.howarth.cloudplatform.controller;

import com.howarth.cloudplatform.model.ApplicationUser;
import com.howarth.cloudplatform.dao.ApplicationUserDao;
import com.howarth.cloudplatform.model.ValidUseToken;
import com.howarth.cloudplatform.security.SecurityConstants;
import com.howarth.cloudplatform.model.VerifiedToken;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;
import org.springframework.data.repository.query.Param;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static com.howarth.cloudplatform.security.JWTAuthorizationFilter.verifyToken;

@RestController
@RequestMapping("/users")
public class UserController {

    private ApplicationUserDao applicationUserDao;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserController(ApplicationUserDao applicationUserDao,
                          BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.applicationUserDao = applicationUserDao;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @PostMapping("/sign-up")
    public ApplicationUser signUp(@RequestBody ApplicationUser user, HttpServletRequest request) {
        if (applicationUserDao.findByUsername(user.getUsername()) != null) {
            return null;
        }
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        applicationUserDao.save(user);

        createBankAccount(user.getUsername(), 0, request);

        return user;
    }

    @PostMapping("/sign-out")
    public ValidUseToken signOut(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = new Cookie("access_token", "");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return new ValidUseToken("no", false);
    }


    private void createBankAccount(String username, int balance, HttpServletRequest request) {
        JSONObject bankAccount = createJsonBankAccount(username, balance);

        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        try {
            // This automatically gets the correct URL to sent the request to.
            final String URL = request.getScheme() + "://" + request.getServerName() +
                    ("http".equals(request.getScheme()) && request.getServerPort() == 80 || "https".equals(request.getScheme()) && request.getServerPort() == 443 ? "" : ":" + request.getServerPort()) +
                    SecurityConstants.CREATE_ACCOUNT;


            HttpPost req = new HttpPost(URL);

            StringEntity params = new StringEntity(bankAccount.toString());

            req.addHeader("content-type", "application/json");

            req.setEntity(params);

            httpClient.execute(req);

            httpClient.close();
        } catch (Exception ex) {
            // handle exception here
        }
    }

    private JSONObject createJsonBankAccount(String username, int balance) {
        JSONObject json = new JSONObject();
        json.put("username", username);
        json.put("balance", balance);
        return json;
    }

    @GetMapping("/verify_token")
    public VerifiedToken verify(@Param("access_token") String access_token) {

        System.out.println("\n\n" + access_token + "\n\n");

        try {
            String user = verifyToken(access_token, SecurityConstants.SECRET, "");
            return new VerifiedToken(user, user != null);
        } catch (NullPointerException ex) {
            return new VerifiedToken("-", false);
        }
    }

    @GetMapping("/all")
    public List<ApplicationUser> all() {
        return applicationUserDao.findAll();
    }
}