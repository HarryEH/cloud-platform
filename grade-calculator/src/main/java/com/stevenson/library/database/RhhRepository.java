package com.stevenson.library.database;

import com.stevenson.library.database.model.RhhStatistic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RhhRepository extends JpaRepository<RhhStatistic, Long>{
  RhhStatistic findTopByOrderByIdDesc();
}
