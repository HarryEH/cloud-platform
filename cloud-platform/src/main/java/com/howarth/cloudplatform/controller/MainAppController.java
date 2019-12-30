package com.howarth.cloudplatform.controller;

import com.howarth.cloudplatform.dao.ApplicationAppDao;
import com.howarth.cloudplatform.dao.ApplicationUserDao;
import com.howarth.cloudplatform.model.ApplicationUser;
import com.howarth.cloudplatform.model.BankAccount;
import com.howarth.cloudplatform.security.JWTAuthorizationFilter;
import com.howarth.cloudplatform.service.BankAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class MainAppController {

    private final ApplicationAppDao applicationAppDao;
    private final BankAccountService bankAccountService;
    private final ApplicationUserDao applicationUserDao;

    @Autowired
    public MainAppController(ApplicationAppDao applicationAppDao,
                             BankAccountService bankAccountService,
                             ApplicationUserDao applicationUserDao) {
        this.applicationAppDao = applicationAppDao;
        this.bankAccountService = bankAccountService;
        this.applicationUserDao = applicationUserDao;
    }

    @GetMapping("/")
    public String index(Model model, HttpServletRequest request) {
        horribleHack();

        String message = JWTAuthorizationFilter.verifyCookieAuth(request);

        final String NOT_LOGGED_IN = "You are not logged in!";

        boolean loggedIn = false;

        if (message != null) {
            if (message.equals("-")) {
                message = NOT_LOGGED_IN;
            } else {
                loggedIn = true;
                message = "You are logged in as " + message + "!";
            }
        } else {
            message = NOT_LOGGED_IN;
        }

        model.addAttribute("message", message);
        model.addAttribute("loggedIn", loggedIn);
        model.addAttribute("apps", applicationAppDao.findAll());

        return "index";
    }


    @GetMapping("/users/sign-in")
    public String signIn(Model model) {
        return "signin";
    }


    @GetMapping("/users/sign-up")
    public String signUp(Model model) {
        return "signup";
    }

    private void horribleHack() {
        /**
         * This is another hack that is required
         */
        final String[] usernames = {"HarryEH"};
        for (String username : usernames) {
            if (applicationUserDao.findByUsername(username) == null) {
                ApplicationUser user = new ApplicationUser();
                user.setPassword(new BCryptPasswordEncoder().encode("password"));
                user.setUsername(username);
                applicationUserDao.save(user);

                bankAccountService.createNewBankAccount(new BankAccount(username, 0));
            }
        }
    }

}
