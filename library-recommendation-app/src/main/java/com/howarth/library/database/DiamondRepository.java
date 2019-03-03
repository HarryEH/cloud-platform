package com.howarth.library.database;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiamondRepository extends JpaRepository<DiamondStatistics, Long>{
  DiamondStatistics findTopByOrderByIdDesc();
}
