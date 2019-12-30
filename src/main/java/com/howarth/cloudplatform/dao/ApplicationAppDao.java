package com.howarth.cloudplatform.dao;

import com.howarth.cloudplatform.model.ApplicationApp;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationAppDao extends JpaRepository<ApplicationApp, Long> {
    ApplicationApp findByName(String name);
}