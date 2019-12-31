package com.howarth.cloudplatform.service;

import com.howarth.cloudplatform.dao.BankChargeDao;
import com.howarth.cloudplatform.model.BankCharge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Calendar;

@Service
public class BankChargeService {
    private final BankChargeDao bankChargeDao;

    @Autowired
    public BankChargeService(BankChargeDao bankChargeDao) {
        this.bankChargeDao = bankChargeDao;
    }

    public boolean isAppUsageChargeable(String username, String appName) {
        BankCharge bankCharge = bankChargeDao.findByUsernameAndAppName(username, appName);

        return bankCharge != null && !isChargeFromToday(bankCharge);
    }

    private boolean isChargeFromToday(BankCharge bankCharge) {
        Timestamp today = Timestamp.from(Instant.now());

        return today.getDate() == bankCharge.getChargeDate().getDate()
                && today.getMonth() == bankCharge.getChargeDate().getMonth()
                && today.getYear() == bankCharge.getChargeDate().getYear();
    }

    public void createBankCharge(String username, String appName) {
        BankCharge bankChargeUser = new BankCharge();
        bankChargeUser.setAppName(appName);
        bankChargeUser.setUsername(username);
        bankChargeDao.save(bankChargeUser);
    }

}
