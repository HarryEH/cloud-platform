package com.howarth.cloud.mainapp.peanutbank.database;

import com.howarth.cloud.mainapp.peanutbank.database.model.BankCharge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankChargeRepository extends JpaRepository<BankCharge, Long> {
    BankCharge findByUsername(String username);
}
