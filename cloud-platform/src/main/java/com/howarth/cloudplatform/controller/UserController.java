package com.howarth.cloudplatform.controller;

import com.howarth.cloudplatform.model.ApplicationUser;
import com.howarth.cloudplatform.model.BankAccount;
import com.howarth.cloudplatform.model.Token;
import com.howarth.cloudplatform.security.SecurityConstants;
import com.howarth.cloudplatform.service.BankAccountService;
import com.howarth.cloudplatform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static com.howarth.cloudplatform.security.JWTAuthorizationFilter.verifyToken;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserService userService;
    private BankAccountService bankAccountService;

    @Autowired
    public UserController(UserService userService,
                          BankAccountService bankAccountService) {
        this.userService = userService;
        this.bankAccountService = bankAccountService;
    }

    @PostMapping("/sign-up")
    public ApplicationUser signUp(@RequestBody ApplicationUser user) {
        userService.createApplicationUser(user);
        bankAccountService.createNewBankAccount(new BankAccount(user.getUsername(), 0));
        return user;
    }

    @PostMapping("/sign-out")
    public Token signOut(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = new Cookie("access_token", "");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return new Token("no", false);
    }

    @GetMapping("/verify_token")
    public Token verify(@Param("access_token") String access_token) {
        try {
            String user = verifyToken(access_token, SecurityConstants.SECRET, "");
            return new Token(user, user != null);
        } catch (Exception ex) {
            return new Token("-", false);
        }
    }

    @GetMapping("/all")
    public List<ApplicationUser> all() {
        return userService.getAllApplicationUsers();
    }
}