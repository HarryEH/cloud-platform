package com.howarth.cloud.mainapp.library.database;

import com.howarth.cloud.mainapp.library.database.model.RhhStatistic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RhhRepository extends JpaRepository<RhhStatistic, Long> {
    RhhStatistic findTopByOrderByIdDesc();
}
