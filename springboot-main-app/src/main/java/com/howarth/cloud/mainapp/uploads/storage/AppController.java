package com.howarth.cloud.mainapp.uploads.storage;

import com.howarth.cloud.mainapp.uploads.storage.database.ApplicationApp;
import com.howarth.cloud.mainapp.uploads.storage.database.ApplicationAppRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AppController {

    private final ApplicationAppRepository applicationAppRepository;

    public AppController(ApplicationAppRepository applicationAppRepository) {
        this.applicationAppRepository = applicationAppRepository;
    }

    /**
     * Mapping to display all of the uploads that are on the platform
     * //FIXME: delete
     * @return
     */
    @GetMapping("/uploads")
    public List<ApplicationApp> listAllUploads(){
        return applicationAppRepository.findAll();
    }

}
