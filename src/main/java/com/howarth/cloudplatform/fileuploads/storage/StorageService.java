package com.howarth.cloudplatform.fileuploads.storage;

import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

public interface StorageService {

    void init();

    void store(MultipartFile file);

    Path load(String filename);
}
