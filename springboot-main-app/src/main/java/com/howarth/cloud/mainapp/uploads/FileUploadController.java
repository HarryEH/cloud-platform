package com.howarth.cloud.mainapp.uploads;

import com.howarth.cloud.mainapp.uploads.storage.ApplicationApp;
import com.howarth.cloud.mainapp.uploads.storage.ApplicationAppRepository;
import com.howarth.cloud.mainapp.uploads.storage.exception.StorageFileNotFoundException;
import com.howarth.cloud.mainapp.uploads.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
public class FileUploadController {

    public static final String REDIRECT_HOME_UPLOAD = "redirect:/upload";
    private final StorageService storageService;
    private final ApplicationAppRepository applicationAppRepository;

    @Autowired
    public FileUploadController(StorageService storageService, ApplicationAppRepository applicationAppRepository) {
        this.storageService = storageService;
        this.applicationAppRepository = applicationAppRepository;
    }


    @GetMapping("/upload")
    public String listUploadedFiles(Model model) {
        return "uploadForm";
    }

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, @RequestParam("logo") MultipartFile logo,
                                   @RequestParam("description") String description, RedirectAttributes redirectAttributes) {

        if (logo == null || file == null || description == null) {
            redirectAttributes.addFlashAttribute("message",
                    "You missed a required field!");
            return REDIRECT_HOME_UPLOAD;
        }

        String filename = file.getOriginalFilename().replace(".war", "");

        if (!logo.getContentType().contains("image")) {
            redirectAttributes.addFlashAttribute("message",
                    "The logo was not an image...");
            return REDIRECT_HOME_UPLOAD;
        }

        if (!file.getOriginalFilename().contains("war")) {
            redirectAttributes.addFlashAttribute("message",
                    "The application was not a war...");
            return REDIRECT_HOME_UPLOAD;
        }

        if (applicationAppRepository.findByName(filename) != null) {
            redirectAttributes.addFlashAttribute("message",
                    "Please rename your .war file.");
            return REDIRECT_HOME_UPLOAD;
        }


        System.out.println("\n\nWorking Directory = " +
                System.getProperty("user.dir") + "\n\n");

        // Store the images
        storageService.store(logo);
        storageService.store(file);

        ApplicationApp applicationApp = new ApplicationApp();
        applicationApp.setDescription(description);
        applicationApp.setName(filename);
        applicationApp.setLogo(logo.getOriginalFilename());

        applicationAppRepository.save(applicationApp);

        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + file.getOriginalFilename() + "!");

        // ADD COMMAND LINE runner!!
        try {
            Process p = Runtime.getRuntime().exec("cp /var/lib/tomcat8/webapps/ROOT/upload-dir/" + file.getOriginalFilename() + " /var/lib/tomcat8/webapps/");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return REDIRECT_HOME_UPLOAD;
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

}
