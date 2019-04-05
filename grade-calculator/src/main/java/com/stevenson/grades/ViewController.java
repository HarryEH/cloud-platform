package com.stevenson.grades;

import com.stevenson.grades.database.ModuleRepository;
import com.stevenson.grades.database.model.ModuleGrades;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ViewController {
    private ModuleRepository mr;

    public ViewController(ModuleRepository mr) {
        this.mr = mr;
        mr.deleteAll();
        List<String> names = new ArrayList<>();
        names.add("Assignment 1"); names.add("Assignment 2");
        List<Double> scores = new ArrayList<>();
        scores.add(10.0); scores.add(20.0);
        ModuleGrades mg1 = new ModuleGrades("COM4519", 10, names, scores);
        ModuleGrades mg2 = new ModuleGrades("COM4521", 20, names, scores);
        mr.save(mg1);
        mr.save(mg2);
    }

    @GetMapping("/")
    public String index(Model model){
        model.addAttribute("moduleGrades", mr.findAll());

        return "index";
    }
}
