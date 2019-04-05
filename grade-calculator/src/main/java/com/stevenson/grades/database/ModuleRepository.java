package com.stevenson.grades.database;

import com.stevenson.grades.database.model.ModuleGrades;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModuleRepository extends JpaRepository<ModuleGrades, Long> {
    ModuleGrades findById(long id);
    ModuleGrades findByModuleName(String moduleName);
    ModuleGrades findTopByOrderByAverage();
    ModuleGrades findTopByOrderByAverageDesc();
}
