package com.howarth.cloud.mainapp.uploads;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.howarth.cloud.mainapp.uploads.storage.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class FileUploadController {

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
            return "redirect:/upload";
        }

        String filename = file.getOriginalFilename().replace(".war", "");

        if (!logo.getContentType().contains("image")) {
            redirectAttributes.addFlashAttribute("message",
                    "The logo was not an image...");
            return "redirect:/upload";
        }

        if (!file.getOriginalFilename().contains("war")) {
            redirectAttributes.addFlashAttribute("message",
                    "The application was not a war...");
            return "redirect:/upload";
        }

        if (applicationAppRepository.findByName(filename) != null) {
            redirectAttributes.addFlashAttribute("message",
                    "Please rename your .war file.");
            return "redirect:/upload";
        }

        ApplicationApp applicationApp = new ApplicationApp();
        applicationApp.setDescription(description);
        applicationApp.setName(filename);
        applicationApp.setLogo(filename +"-"+ logo.getOriginalFilename());

        applicationAppRepository.save(applicationApp);

        // Store the images
        storageService.store(logo);
        storageService.store(file);

        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + file.getOriginalFilename() + "!");

        // ADD COMMAND LINE runner!!
        try {
            Process p = Runtime.getRuntime().exec("cp upload-dir/"+file.getOriginalFilename()+" ../");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "redirect:/upload";
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

}
