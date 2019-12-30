package com.howarth.cloudplatform.dao;

import com.howarth.cloudplatform.model.BankCharge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BankChargeDao extends JpaRepository<BankCharge, Long> {
    BankCharge findByUsernameAndAppName(String username, String appName);
    List<BankCharge> findByUsername(String username);
}
