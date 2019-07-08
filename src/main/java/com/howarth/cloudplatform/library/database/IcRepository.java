package com.howarth.cloudplatform.library.database;

import com.howarth.cloudplatform.library.database.model.IcStatistic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IcRepository extends JpaRepository<IcStatistic, Long> {
    IcStatistic findTopByOrderByIdDesc();
}
