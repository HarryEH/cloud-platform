package com.howarth.cloud.mainapp.library.controllers;

import com.howarth.cloud.mainapp.library.database.DiamondRepository;
import com.howarth.cloud.mainapp.library.database.IcRepository;
import com.howarth.cloud.mainapp.library.database.RhhRepository;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LibraryViewController {
//    private String appMode;

    private DiamondRepository diamondRepository;
    private IcRepository icRepository;
    private RhhRepository rhhRepository;

    public LibraryViewController(Environment environment, DiamondRepository diamondRepository, IcRepository icRepository, RhhRepository rhhRepository) {
        this.diamondRepository = diamondRepository;
        this.icRepository = icRepository;
        this.rhhRepository = rhhRepository;
//        appMode = environment.getProperty("app-mode");
    }

    @GetMapping("/library")
    public String index(Model model){
        model.addAttribute("diamond", diamondRepository.findTopByOrderByIdDesc().getNumberOfPeople());
        model.addAttribute("ic", icRepository.findTopByOrderByIdDesc().getNumberOfPeople());
        model.addAttribute("rhh", rhhRepository.findTopByOrderByIdDesc().getNumberOfPeople());
        model.addAttribute("best", "Diamond");

        return "library";
    }

    @GetMapping("/library/")
    public String lib(Model model){
        model.addAttribute("diamond", diamondRepository.findTopByOrderByIdDesc().getNumberOfPeople());
        model.addAttribute("ic", icRepository.findTopByOrderByIdDesc().getNumberOfPeople());
        model.addAttribute("rhh", rhhRepository.findTopByOrderByIdDesc().getNumberOfPeople());
        model.addAttribute("best", "Diamond");

        return "library";
    }
}
