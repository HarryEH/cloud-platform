package com.howarth.cloud.mainapp.security;

import org.springframework.http.HttpMethod;

public class SecurityConstants {
    public static final String SECRET = "SecretKeyToGenJWTs";
    public static final long EXPIRATION_TIME = 864_000_000; // 10 days
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String COOKIE = "Cookie";

    /**
     * URLS that are allowed through security, add below.
     */
    public static final String SIGN_UP_URL = "/users/sign-up";
    public static final String CHECK_USER_URL = "/users/verify_token";
    public static final String SIGN_IN = "/users/sign-in";
    public static final String INDEX = "/";
    public static final String ALL_USERS = "/users/all";

}