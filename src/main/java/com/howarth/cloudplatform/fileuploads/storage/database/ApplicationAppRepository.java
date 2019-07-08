package com.howarth.cloudplatform.fileuploads.storage.database;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationAppRepository extends JpaRepository<ApplicationApp, Long> {
    ApplicationApp findByName(String name);
}