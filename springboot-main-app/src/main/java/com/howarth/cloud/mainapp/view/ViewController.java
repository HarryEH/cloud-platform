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
     *
     * @param model
     * @return
     */
    @GetMapping("/")
    public String index(Model model){

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
