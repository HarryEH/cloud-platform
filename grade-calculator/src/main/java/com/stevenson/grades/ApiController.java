package com.stevenson.grades;

import com.stevenson.grades.database.ModuleRepository;
import com.stevenson.grades.database.model.ModuleGrades;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api")
public class ApiController {
    private ModuleRepository mr;

    public ApiController(ModuleRepository mr) {
        this.mr = mr;
    }

    @GetMapping("/modulegrades")
    public List<ModuleGrades> moduleGrades(){
        return mr.findAll();
    }
}
