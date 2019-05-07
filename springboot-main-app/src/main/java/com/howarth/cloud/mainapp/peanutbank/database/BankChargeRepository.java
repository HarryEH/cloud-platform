package com.howarth.cloud.mainapp.peanutbank.database;

import com.howarth.cloud.mainapp.peanutbank.database.model.BankCharge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BankChargeRepository extends JpaRepository<BankCharge, Long> {
    BankCharge findByUsernameAndAppName(String username, String appName);
    List<BankCharge> findByUsername(String username);
}
