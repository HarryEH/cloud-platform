package com.howarth.library.database;

import com.howarth.library.database.model.DiamondStatistic;
import com.howarth.library.database.model.RhhStatistic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RhhRepository extends JpaRepository<RhhStatistic, Long>{
  RhhStatistic findTopByOrderByIdDesc();
}
