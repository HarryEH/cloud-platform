package com.howarth.cloudplatform.controller;

import com.howarth.cloudplatform.model.ValidUseToken;
import com.howarth.cloudplatform.dao.BankAccountDao;
import com.howarth.cloudplatform.dao.BankChargeDao;
import com.howarth.cloudplatform.model.BankAccount;
import com.howarth.cloudplatform.model.BankCharge;
import com.howarth.cloudplatform.security.SecurityConstants;
import com.howarth.cloudplatform.dao.ApplicationAppDao;
import com.howarth.cloudplatform.security.JWTAuthorizationFilter;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

@RestController
@RequestMapping(value = "/peanut_bank")
public class PeanutBankController {

    private final BankAccountDao bankAccountDao;
    private final ApplicationAppDao applicationAppDao;
    private final BankChargeDao bankChargeDao;

    public PeanutBankController(BankAccountDao bankAccountDao, ApplicationAppDao applicationAppDao, BankChargeDao bankChargeDao) {
        this.bankAccountDao = bankAccountDao;
        this.applicationAppDao = applicationAppDao;
        this.bankChargeDao = bankChargeDao;
    }

    @PostMapping("/new_account")
    public ValidUseToken newAccount(@RequestBody BankAccount account) {
        if (bankAccountDao.findByUsername(account.getUsername()) != null) {
            return new ValidUseToken("-", false);
        }
        account.setBalance(1000);
        BankAccount b = bankAccountDao.save(account);
        return new ValidUseToken(b.getUsername(), true);
    }

    @GetMapping("/all_accounts")
    public List<BankAccount> allAccounts() {
        return bankAccountDao.findAll();
    }

    @GetMapping("/usage")
    public ValidUseToken usage(@Param("access_token") String access_token, @Param("app_name") String app_name,
                               HttpServletRequest request) {
        String user = JWTAuthorizationFilter.verifyToken(access_token, SecurityConstants.SECRET, "");
        String app_owner = applicationAppDao.findByName(app_name).getUsername();

        if (user.equals(app_owner)) {
            return new ValidUseToken("you own the app", true);
        }

        BankCharge bankCharge = bankChargeDao.findByUsernameAndAppName(user, app_name);

        if (bankCharge != null) {

            Calendar calendar = Calendar.getInstance();
            Timestamp currentTimestamp = new java.sql.Timestamp(calendar.getTime().getTime());

            if(currentTimestamp.getDate() == bankCharge.getChargeDate().getDate() && currentTimestamp.getMonth() == bankCharge.getChargeDate().getMonth() &&
                currentTimestamp.getYear() == bankCharge.getChargeDate().getYear()) {
                return new ValidUseToken("already charged", true);
            }
        }

        // App user
        chargeUser(user, -5);

        // App owner
        chargeUser(app_owner, 3);

        // Charging owner
        final String hiren = "hapatel1";
        chargeUser(hiren, 1);

        // Login owner
        final String jack = "jcdhan1";
        chargeUser(jack, 1);

        BankCharge bankChargeUser = new BankCharge();
        bankChargeUser.setApp_name(app_name);
        bankChargeUser.setUsername(user);
        bankChargeDao.save(bankChargeUser);

        return new ValidUseToken("successful charge", true);
    }

    private void chargeUser(String username, int charge) {
        BankAccount bankAccount = bankAccountDao.findByUsername(username);
        int userBankBalance = bankAccount.getBalance();
        bankAccount.setBalance(userBankBalance + charge);
        bankAccountDao.save(bankAccount);
    }

}
