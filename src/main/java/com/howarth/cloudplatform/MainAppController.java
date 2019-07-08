package com.howarth.cloudplatform;

import com.howarth.cloudplatform.virtualbank.database.BankAccountRepository;
import com.howarth.cloudplatform.virtualbank.database.model.BankAccount;
import com.howarth.cloudplatform.fileuploads.storage.database.ApplicationApp;
import com.howarth.cloudplatform.fileuploads.storage.database.ApplicationAppRepository;
import com.howarth.cloudplatform.user.database.ApplicationUser;
import com.howarth.cloudplatform.user.database.ApplicationUserRepository;
import com.howarth.cloudplatform.security.JWTAuthorizationFilter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class MainAppController {

    private final ApplicationAppRepository applicationAppRepository;
    private final BankAccountRepository bankAccountRepository;
    private final ApplicationUserRepository applicationUserRepository;

    public MainAppController(ApplicationAppRepository applicationAppRepository, BankAccountRepository bankAccountRepository, ApplicationUserRepository applicationUserRepository) {
        this.applicationAppRepository = applicationAppRepository;
        this.bankAccountRepository = bankAccountRepository;
        this.applicationUserRepository = applicationUserRepository;
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
        model.addAttribute("apps", applicationAppRepository.findAll());

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
            if(applicationUserRepository.findByUsername(username) == null) {
                ApplicationUser user = new ApplicationUser();
                user.setPassword(new BCryptPasswordEncoder().encode("password"));
                user.setUsername(username);
                applicationUserRepository.save(user);

                BankAccount bankAccount = new BankAccount();
                bankAccount.setUsername(username);
                bankAccount.setBalance(1000);
                bankAccountRepository.save(bankAccount);
            }
        }

        /**
         * This hack is required to seed the database
         */
        if (applicationAppRepository.findByName("library") == null) {
            ApplicationApp library = new ApplicationApp();
            library.setName("library");
            library.setLogo("/diamond");
            library.setUsername("exampleName");
            library.setDescription("This is applications recommends which library to visit!");
            applicationAppRepository.save(library);
        }
    }

}
