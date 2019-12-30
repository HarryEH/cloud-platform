package com.howarth.cloudplatform.controller;

import com.howarth.cloudplatform.dao.BankAccountDao;
import com.howarth.cloudplatform.model.BankAccount;
import com.howarth.cloudplatform.model.ApplicationApp;
import com.howarth.cloudplatform.dao.ApplicationAppDao;
import com.howarth.cloudplatform.model.ApplicationUser;
import com.howarth.cloudplatform.dao.ApplicationUserDao;
import com.howarth.cloudplatform.security.JWTAuthorizationFilter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class MainAppController {

    private final ApplicationAppDao applicationAppDao;
    private final BankAccountDao bankAccountDao;
    private final ApplicationUserDao applicationUserDao;

    public MainAppController(ApplicationAppDao applicationAppDao, BankAccountDao bankAccountDao, ApplicationUserDao applicationUserDao) {
        this.applicationAppDao = applicationAppDao;
        this.bankAccountDao = bankAccountDao;
        this.applicationUserDao = applicationUserDao;
    }

    /**
     * The index page of the app - this provides a sign-up, a login, uploading apps and showing the apps we have available
     *
     * @param model
     * @return
     */
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

    /**
     * This is the mapping for the view that controls signing in. Which is handled by
     * a post method that doesn't actually need to be in the UserController
     *
     * @param model
     * @return
     */
    @GetMapping("/users/sign-in")
    public String signIn(Model model) {
        return "signin";
    }

    /**
     * This is the mapping for the view that controls signing in. Which is handled by
     * a post method that doesn't actually need to be in the UserController
     *
     * @param model
     * @return
     */
    @GetMapping("/users/sign-up")
    public String signUp(Model model) {
        return "signup";
    }


    private void horribleHack() {
        /**
         * This is another hack that is required
         */
        final String[] usernames = { "exampleName" };
        for(String username : usernames) {
            if(applicationUserDao.findByUsername(username) == null) {
                ApplicationUser user = new ApplicationUser();
                user.setPassword(new BCryptPasswordEncoder().encode("password"));
                user.setUsername(username);
                applicationUserDao.save(user);

                BankAccount bankAccount = new BankAccount();
                bankAccount.setUsername(username);
                bankAccount.setBalance(1000);
                bankAccountDao.save(bankAccount);
            }
        }

        /**
         * This hack is required to seed the database
         */
        if (applicationAppDao.findByName("library") == null) {
            ApplicationApp library = new ApplicationApp();
            library.setName("library");
            library.setLogo("/diamond");
            library.setUsername("exampleName");
            library.setDescription("This is applications recommends which library to visit!");
            applicationAppDao.save(library);
        }
    }

}
