package com.howarth.cloudplatform.dao;

import com.howarth.cloudplatform.model.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationUserDao extends JpaRepository<ApplicationUser, Long> {
    ApplicationUser findByUsername(String username);
}