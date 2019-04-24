package com.stevenson.grades.database;

import com.stevenson.grades.database.model.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModuleRepository extends JpaRepository<Module, Long> {
    Module findByModuleName(String moduleName);
    Module findTopByOrderByAverage();
    Module findTopByOrderByAverageDesc();
}
