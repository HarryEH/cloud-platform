package com.howarth.cloud.mainapp.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

import static com.howarth.cloud.mainapp.security.SecurityConstants.ACCESS_TOKEN;
import static com.howarth.cloud.mainapp.security.SecurityConstants.SESSION_STRING;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    public JWTAuthorizationFilter(AuthenticationManager authManager) {
        super(authManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {
        UsernamePasswordAuthenticationToken authentication = getAuthentication(req);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(req, res);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(SecurityConstants.HEADER_STRING);

        if (token != null) {
            return verifyBearerAuth(token);
        } else {
            return verifyCookieAuth(request.getHeader(SecurityConstants.COOKIE));
        }
    }

    private UsernamePasswordAuthenticationToken verifyBearerAuth(final String token){
        // parse the token.
        String user = verifyToken(token, SecurityConstants.SECRET, SecurityConstants.TOKEN_PREFIX);

        if (user != null) {
            return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
        }
        return null;
    }

    private UsernamePasswordAuthenticationToken verifyCookieAuth(String token){
        if (token == null) {
            return null;
        }

        if (token.contains(ACCESS_TOKEN)) {
            String cookie[] = token.split(ACCESS_TOKEN);

            if ( cookie.length == 2 ) {
                if (cookie[1].contains(SESSION_STRING)) {
                    String qqq[] = cookie[1].split(SESSION_STRING);
                    if (qqq.length == 2) {
                        token = qqq[0];
                    } else {
                        return null;
                    }
                } else {
                    token = cookie[1];
                }
            } else {
                return null;
            }

            try {
                String user = verifyToken(token, SecurityConstants.SECRET, "");

                if (user != null) {
                    return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
                }
            } catch (JWTVerificationException exc) {
                return null;
            }

            return null;
        } else {
            return null;
        }
    }

    public static String verifyToken(final String token, final String secret, final String prefix) throws JWTVerificationException {
        return JWT.require(Algorithm.HMAC512(secret.getBytes()))
                .build()
                .verify(prefix.equals("") ? token : token.replace(prefix, ""))
                .getSubject();
    }
}