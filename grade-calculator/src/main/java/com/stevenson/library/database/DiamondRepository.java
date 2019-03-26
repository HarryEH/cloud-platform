package com.stevenson.library.database;

import com.stevenson.library.database.model.DiamondStatistic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiamondRepository extends JpaRepository<DiamondStatistic, Long>{
  DiamondStatistic findTopByOrderByIdDesc();
}
