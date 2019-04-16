package com.stevenson.grades.database;

import com.stevenson.grades.database.model.UserModules;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserModuleRepository extends JpaRepository<UserModules, Long> {
    UserModules findDistinctByUserID(long userID);
}
