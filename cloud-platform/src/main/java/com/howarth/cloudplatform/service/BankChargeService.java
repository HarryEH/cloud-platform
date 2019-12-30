package com.howarth.cloudplatform.service;

import com.howarth.cloudplatform.dao.BankChargeDao;
import com.howarth.cloudplatform.model.BankCharge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
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

        Calendar calendar = Calendar.getInstance();
        Timestamp currentTimestamp = new java.sql.Timestamp(calendar.getTime().getTime());

        return bankCharge != null
                && currentTimestamp.getDate() == bankCharge.getChargeDate().getDate()
                && currentTimestamp.getMonth() == bankCharge.getChargeDate().getMonth()
                && currentTimestamp.getYear() == bankCharge.getChargeDate().getYear();
    }

    public void createBankCharge(String username, String appName) {
        BankCharge bankChargeUser = new BankCharge();
        bankChargeUser.setAppName(appName);
        bankChargeUser.setUsername(username);
        bankChargeDao.save(bankChargeUser);
    }

}
