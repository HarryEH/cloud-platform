package com.howarth.cloud.mainapp.uploads.storage.database;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationAppRepository extends JpaRepository<ApplicationApp, Long> {
    ApplicationApp findByName(String name);
}