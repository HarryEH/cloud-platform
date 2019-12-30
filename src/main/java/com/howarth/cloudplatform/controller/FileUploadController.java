package com.howarth.cloudplatform.controller;

import com.howarth.cloudplatform.dao.ApplicationAppDao;
import com.howarth.cloudplatform.exception.StorageFileNotFoundException;
import com.howarth.cloudplatform.model.ApplicationApp;
import com.howarth.cloudplatform.service.StorageService;
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

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static com.howarth.cloudplatform.security.JWTAuthorizationFilter.verifyCookieAuth;

@Controller
public class FileUploadController {

    public static final String REDIRECT_HOME_UPLOAD = "redirect:/upload";
    private final StorageService storageService;
    private final ApplicationAppDao applicationAppDao;

    @Autowired
    public FileUploadController(StorageService storageService,
                                ApplicationAppDao applicationAppDao) {
        this.storageService = storageService;
        this.applicationAppDao = applicationAppDao;
    }

    @GetMapping("/upload")
    public String listUploadedFiles(Model model) {
        return "uploadForm";
    }

    @PostMapping("/upload")
    public String handleFileUpload(HttpServletRequest request,
                                   @RequestParam("file") MultipartFile file,
                                   @RequestParam("description") String description,
                                   RedirectAttributes redirectAttributes) {

        if (file == null || description == null) {
            redirectAttributes.addFlashAttribute("message",
                    "You missed a required field!");
            return REDIRECT_HOME_UPLOAD;
        }

        String filename = file.getOriginalFilename().replace(".war", "");

        if (!file.getOriginalFilename().contains("war")) {
            redirectAttributes.addFlashAttribute("message",
                    "The application was not a war...");
            return REDIRECT_HOME_UPLOAD;
        }

        if (applicationAppDao.findByName(filename) != null) {
            redirectAttributes.addFlashAttribute("message",
                    "Please rename your .war file.");
            return REDIRECT_HOME_UPLOAD;
        }


        System.out.println("\n\nWorking Directory = " +
                System.getProperty("user.dir") + "\n\n");

        // Store the images
        storageService.store(file);

        ApplicationApp applicationApp = new ApplicationApp();
        applicationApp.setDescription(description);
        applicationApp.setName(filename);
        applicationApp.setUsername(verifyCookieAuth(request));
        applicationApp.setLogo("this is so irrelevant");

        applicationAppDao.save(applicationApp);

        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + file.getOriginalFilename() + "!");

        // ADD COMMAND LINE runner!!
        try {
            Process p = Runtime.getRuntime().exec("cp upload-dir/" + file.getOriginalFilename() + " webapps/");
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
