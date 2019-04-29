package com.howarth.cloud.mainapp.view;

import com.howarth.cloud.mainapp.peanutbank.database.BankAccountRepository;
import com.howarth.cloud.mainapp.peanutbank.database.BankChargeRepository;
import com.howarth.cloud.mainapp.peanutbank.database.model.BankAccount;
import com.howarth.cloud.mainapp.uploads.storage.database.ApplicationApp;
import com.howarth.cloud.mainapp.uploads.storage.database.ApplicationAppRepository;
import com.howarth.cloud.mainapp.user.ApplicationUser;
import com.howarth.cloud.mainapp.user.ApplicationUserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

import static com.howarth.cloud.mainapp.security.JWTAuthorizationFilter.verifyCookieAuth;

@Controller
public class ViewController {

    private final ApplicationAppRepository applicationAppRepository;
    private final BankAccountRepository bankAccountRepository;
    private final ApplicationUserRepository applicationUserRepository;

    public ViewController(ApplicationAppRepository applicationAppRepository, BankAccountRepository bankAccountRepository, ApplicationUserRepository applicationUserRepository) {
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

        /**
         * This is another hack that is required
         */
        final String[] usernames = {"hapatel1", "hhowarth1", "nstevenson1", "jcdhan1"};
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
         * This hack is required because we are not uploading the apps as
         * seperate spring applications - so add the embedded apps as if they
         * were uploaded through the web form.
         */
        if (applicationAppRepository.findByName("library") == null) {
            ApplicationApp library = new ApplicationApp();
            library.setName("library");
            library.setLogo("/diamond");
            library.setUsername("hhowarth1");
            library.setDescription("This is applications recommends which library to visit!");
            applicationAppRepository.save(library);
        }



        if (applicationAppRepository.findByName("other") == null) {
            //todo fill this in  later
        }

        String message = verifyCookieAuth(request);

        final String NOT_LOGGED_IN = "You are not logged in!";

        if (message != null) {
            if (message.equals("-")) {
                message = NOT_LOGGED_IN;
            } else {
                message = "You are logged in as " + message + "!";
            }


        } else {
            message = NOT_LOGGED_IN;
        }


        model.addAttribute("message", message);


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

}
