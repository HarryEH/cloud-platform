package com.stevenson.library.database;

import com.stevenson.library.database.model.IcStatistic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IcRepository extends JpaRepository<IcStatistic, Long>{
  IcStatistic findTopByOrderByIdDesc();
}
