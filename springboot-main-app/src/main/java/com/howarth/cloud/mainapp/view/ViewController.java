package com.howarth.cloud.mainapp.view;

import com.howarth.cloud.mainapp.uploads.storage.ApplicationApp;
import com.howarth.cloud.mainapp.uploads.storage.ApplicationAppRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    private final ApplicationAppRepository applicationAppRepository;

    public ViewController(ApplicationAppRepository applicationAppRepository) {
        this.applicationAppRepository = applicationAppRepository;
    }

    /**
     * The index page of the app - this provides a sign-up, a login, uploading apps and showing the apps we have available
     * @param model
     * @return
     */
    @GetMapping("/")
    public String index(Model model){

        /**
         * This hack is required because we are not uploading the apps as
         * seperate spring applications - so add the embedded apps as if they
         * were uploaded through the web form.
         */
        if (applicationAppRepository.findByName("library") == null) {
            ApplicationApp library = new ApplicationApp();
            library.setName("library");
            library.setLogo("/diamond");
            library.setDescription("This is applications recommends which library to visit!");
            applicationAppRepository.save(library);
        }

        if (applicationAppRepository.findByName("other") == null) {
            //todo fill this in  later
        }


        model.addAttribute("apps", applicationAppRepository.findAll());
        
        return "index";
    }

}
