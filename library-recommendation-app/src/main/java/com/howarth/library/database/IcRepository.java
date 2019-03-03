package com.howarth.library.database;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IcRepository extends JpaRepository<IcStatistics, Long>{
  IcStatistics findTopByOrderByIdDesc();
}
