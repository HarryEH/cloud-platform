package com.howarth.cloud.mainapp.grades.database;

import com.howarth.cloud.mainapp.grades.database.model.Grade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GradeRepository extends JpaRepository<Grade, Long> {
}
