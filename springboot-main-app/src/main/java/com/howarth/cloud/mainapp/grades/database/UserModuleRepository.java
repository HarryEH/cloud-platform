package com.howarth.cloud.mainapp.grades.database;

import com.howarth.cloud.mainapp.grades.database.model.UserModule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserModuleRepository extends JpaRepository<UserModule, Long> {
    UserModule findDistinctByUsername(String username);
}
