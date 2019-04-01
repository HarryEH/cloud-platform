package com.howarth.cloud.mainapp.view;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    public ViewController() {
    }

    @GetMapping("/users/sign-in")
    public String signIn(Model model){
        return "sign-in";
    }

    /**
     * //TODO pass in all of the uploaded apps
     * //TODO
     * @param model
     * @return
     */
    @GetMapping("/")
    public String index(Model model){
        return "index";
    }

}
