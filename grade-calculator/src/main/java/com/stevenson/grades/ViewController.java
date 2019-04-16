package com.stevenson.grades;

import com.stevenson.grades.database.GradeRepository;
import com.stevenson.grades.database.ModuleRepository;
import com.stevenson.grades.database.UserModuleRepository;
import com.stevenson.grades.database.model.Grade;
import com.stevenson.grades.database.model.ModuleGrades;
import com.stevenson.grades.database.model.UserModules;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ViewController {
    private UserModuleRepository umr;

    public ViewController(ModuleRepository mr, GradeRepository gr, UserModuleRepository umr) {
        this.umr = umr;
        umr.deleteAll();
        mr.deleteAll();
        gr.deleteAll();

        List<Grade> grades1 = new ArrayList<>();
        List<Grade> grades2 = new ArrayList<>();
        grades1.add(new Grade("Assignment 1", 20, 20, 40));
        grades1.add(new Grade("Assignment 2", 20, 20, 40));
        grades1.add(new Grade("Assignment 3", 8, 10, 20));
        grades2.add(new Grade("Assignment 1", 20, 20, 40));
        grades2.add(new Grade("Assignment 2", 20, 20, 20));
        grades2.add(new Grade("Assignment 3", 8, 10, 20));
        gr.save(grades1);
        gr.save(grades2);

        ModuleGrades mg1 = new ModuleGrades("COM4519", 10, grades1);
        ModuleGrades mg2 = new ModuleGrades("COM4521", 20, grades2);
        mr.save(mg1);
        mr.save(mg2);

        long userId = 1500;
        List<ModuleGrades> mgList = new ArrayList<>();
        mgList.add(mg1);
        mgList.add(mg2);
        UserModules um = new UserModules(userId, mgList, 60);
        umr.save(um);
    }

    @GetMapping("/")
    public String index(Model model){
        UserModules userToShow = umr.findDistinctByUserID(1500);
        model.addAttribute("user", userToShow);
        model.addAttribute("moduleGrades", userToShow.getModules());

        return "index";
    }
}
