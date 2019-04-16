package com.stevenson.grades.database;

import com.stevenson.grades.database.model.Grade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GradeRepository extends JpaRepository<Grade, Long> {
}
