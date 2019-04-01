package com.howarth.cloud.mainapp.view;

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
        model.addAttribute("apps", applicationAppRepository.findAll());
        
        return "index";
    }

}
