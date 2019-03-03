package com.howarth.library.database;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RhhRepository extends JpaRepository<RhhStatistics, Long>{
  RhhStatistics findTopByOrderByIdDesc();
}
