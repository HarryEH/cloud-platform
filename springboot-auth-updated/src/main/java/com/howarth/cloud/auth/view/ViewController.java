package com.howarth.cloud.auth.view;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    public ViewController() {
    }

    @GetMapping("/users/sign-in")
    public String index(Model model){
        return "index";
    }
}
