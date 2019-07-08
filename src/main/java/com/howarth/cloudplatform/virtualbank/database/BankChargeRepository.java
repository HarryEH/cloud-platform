package com.howarth.cloudplatform.virtualbank.database;

import com.howarth.cloudplatform.virtualbank.database.model.BankCharge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BankChargeRepository extends JpaRepository<BankCharge, Long> {
    BankCharge findByUsernameAndAppName(String username, String appName);
    List<BankCharge> findByUsername(String username);
}
