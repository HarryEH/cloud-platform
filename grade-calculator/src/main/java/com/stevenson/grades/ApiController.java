package com.stevenson.grades;

import com.stevenson.grades.database.GradeRepository;
import com.stevenson.grades.database.ModuleRepository;
import com.stevenson.grades.database.UserModuleRepository;
import com.stevenson.grades.database.model.Grade;
import com.stevenson.grades.database.model.Module;
import com.stevenson.grades.database.model.UserModule;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api")
public class ApiController {
    private ModuleRepository mr;
    private GradeRepository gr;
    private UserModuleRepository umr;

    public ApiController(ModuleRepository mr, GradeRepository gr, UserModuleRepository umr) {
        this.mr = mr;
        this.gr = gr;
        this.umr = umr;
    }

    @GetMapping("/modules")
    public List<Module> modules(){
        return mr.findAll();
    }

    @GetMapping("/grades")
    public List<Grade> grades(){
        return gr.findAll();
    }

    @GetMapping("/usermodules")
    public List<UserModule> userModules(){
        return umr.findAll();
    }
}
