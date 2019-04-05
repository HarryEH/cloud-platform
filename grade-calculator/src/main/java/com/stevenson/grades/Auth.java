package com.stevenson.grades;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Base64;

//This won't work- need authorization from Uni Admins to get access
public class Auth {

    public Auth(){}

    public Token authorize(){
        RestTemplate rt = new RestTemplate();
        URI uri = null;

        try{
            uri = new URI(BlackboardConstants.AUTH_ENDPOINT);
            uri = new URI(BlackboardConstants.HOSTNAME + "/learn/api/public/v1/oauth2/token");
        }catch(URISyntaxException e){
            e.printStackTrace();
        }

        HttpHeaders headers = new HttpHeaders();

        headers.add("Authorization", "Basic " + getHash());
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<String> request = new HttpEntity<String>("grant_type=client_credentials",headers);

        ResponseEntity<Token> response = rt.exchange(uri, HttpMethod.POST, request, Token.class);

        return response.getBody();
    }


    private String getHash(){
        String hashable = BlackboardConstants.APP_KEY + ":" + BlackboardConstants.APP_SECRET;
        String hash = Base64.getEncoder().encodeToString(hashable.getBytes());

        return(hash);
    }
}
