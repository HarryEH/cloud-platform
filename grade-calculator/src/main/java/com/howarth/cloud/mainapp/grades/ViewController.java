package com.howarth.cloud.mainapp.grades;

import com.howarth.cloud.mainapp.grades.database.GradeRepository;
import com.howarth.cloud.mainapp.grades.database.ModuleRepository;
import com.howarth.cloud.mainapp.grades.database.UserModuleRepository;
import com.howarth.cloud.mainapp.grades.database.model.Grade;
import com.howarth.cloud.mainapp.grades.database.model.Module;
import com.howarth.cloud.mainapp.grades.database.model.UserModule;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ViewController {
    private UserModuleRepository umr;
    private ModuleRepository mr;
    private GradeRepository gr;
    private String user = "nstevenson1";

    public ViewController(UserModuleRepository umr, ModuleRepository mr, GradeRepository gr) {
        this.umr = umr;
        this.mr = mr;
        this.gr = gr;

        umr.deleteAll();
        mr.deleteAll();
        gr.deleteAll();

        if(umr.findDistinctByUsername(user) == null){
            UserModule um = new UserModule(user, new ArrayList<>(), 0);
            umr.save(um);
        }

        //TODO: Find actual current user

//        List<Grade> grades1 = new ArrayList<>();
//        List<Grade> grades2 = new ArrayList<>();
//        grades1.add(new Grade("Assignment 1", 20, 20, 40));
//        grades1.add(new Grade("Assignment 2", 20, 20, 40));
//        grades1.add(new Grade("Assignment 3", 8, 10, 20));
//        grades2.add(new Grade("Assignment 1", 20, 20, 40));
//        grades2.add(new Grade("Assignment 2", 20, 20, 20));
//        grades2.add(new Grade("Assignment 3", 8, 10, 20));
//        gr.save(grades1);
//        gr.save(grades2);
//
//        Module m1 = new Module("COM4519", 10, grades1);
//        Module m2 = new Module("COM4521", 20, grades2);
//        mr.save(m1);
//        mr.save(m2);
//
//        List<Module> mList = new ArrayList<>();
//        mList.add(m1);
//        mList.add(m2);
//        UserModule um = new UserModule(user, mList, 60);
//        umr.save(um);
    }

    @GetMapping("/grade_calculator")
    public String loadIndex(Model model){
        UserModule userToShow = umr.findDistinctByUsername(user);
        model.addAttribute("user", userToShow);
        model.addAttribute("modules", userToShow.getModules());
        model.addAttribute("newm", new Module(null,0));
        model.addAttribute("newg", new GradeHandler());


        return "grade_calc";
    }

    @PostMapping("/grade_calculator/updm")
    public String updateM(@ModelAttribute("newm") Module m){
        if(m.getId() != 0){
            Module oldM = mr.findOne(m.getId());
            if(oldM != null){
                oldM.setModuleName(m.getModuleName());
                oldM.setCredits(m.getCredits());
                mr.save(oldM);
                UserModule curUser = umr.findDistinctByUsername(user);
                curUser.updateCredits();
            }
        }else{
            //in case constructor ever changes to include extra code
            Module newM = new Module(m.getModuleName(), m.getCredits());
            mr.save(newM);
            UserModule curUser = umr.findDistinctByUsername(user);
            curUser.addModule(newM);
            umr.save(curUser);
        }

        return "redirect:/";
    }

    @PostMapping("/grade_calculator/updg")
    public String updateG(@ModelAttribute("newg") GradeHandler g){
        if(g.getId() != 0){
            Grade oldG = gr.findOne(g.getId());
            if(oldG != null){
                oldG.setName(g.getName());
                oldG.setGrade(g.getGrade());
                oldG.setMaxGrade(g.getMaxGrade());
                oldG.setWeight(g.getWeight());
                gr.save(oldG);
            }
        }else{
            //this constructor does actually include extra code
            Grade newG = new Grade(g.getName(), g.getGrade(), g.getMaxGrade(), g.getWeight());
            gr.save(newG);
            Module curModule = mr.findOne(g.getParentId());
            curModule.addGrade(newG);
            mr.save(curModule);
        }

        return "redirect:/";
    }

    @GetMapping("/grade_calculator/delete_grade")
    public String deleteGrade(@RequestParam(name="gradeId")long gId, @RequestParam(name="moduleId")long mId){
        Grade delG = gr.findOne(gId);
        Module parM = mr.findOne(mId);

        parM.delGrade(delG);
        mr.save(parM);
        gr.delete(gId);

        return "redirect:/grade_calculator";
    }

    @GetMapping("/grade_calculator/delete_module")
    public String deleteModule(@RequestParam(name="moduleId")long mId, @RequestParam(name="userId")long uId){
        Module delM = mr.findOne(mId);
        UserModule user = umr.findOne(uId);
        List<Grade> grades = delM.getGrades();

        user.delModule(delM);
        umr.save(user);
        delM.setGrades(new ArrayList<>());
        for(Grade g : grades){
            gr.delete(g.getId());
        }
        mr.delete(mId);

        return "redirect:/grade_calculator";
    }
}
