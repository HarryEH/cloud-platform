package com.howarth.cloud.mainapp.uploads.storage;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AppController {

    private final ApplicationAppRepository applicationAppRepository;

    public AppController(ApplicationAppRepository applicationAppRepository) {
        this.applicationAppRepository = applicationAppRepository;
    }

    @GetMapping("/uploads")
    public List<ApplicationApp> listAllUploads(){
        return applicationAppRepository.findAll();
    }

}
