package com.howarth.cloud.mainapp.grades.database;

import com.howarth.cloud.mainapp.grades.database.model.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModuleRepository extends JpaRepository<Module, Long> {
    Module findByModuleName(String moduleName);
    Module findTopByOrderByAverage();
    Module findTopByOrderByAverageDesc();
}
