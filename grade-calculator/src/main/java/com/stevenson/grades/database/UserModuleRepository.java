package com.stevenson.grades.database;

import com.stevenson.grades.database.model.UserModule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserModuleRepository extends JpaRepository<UserModule, Long> {
    UserModule findDistinctByUserID(long userID);
}
