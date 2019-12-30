package com.howarth.cloudplatform.controller;

import com.howarth.cloudplatform.dao.ApplicationAppDao;
import com.howarth.cloudplatform.model.BankAccount;
import com.howarth.cloudplatform.model.Token;
import com.howarth.cloudplatform.security.JWTAuthorizationFilter;
import com.howarth.cloudplatform.security.SecurityConstants;
import com.howarth.cloudplatform.service.BankAccountService;
import com.howarth.cloudplatform.service.BankChargeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/peanut_bank")
public class PeanutBankController {

    private final BankAccountService bankAccountService;
    private final ApplicationAppDao applicationAppDao;
    private final BankChargeService bankChargeService;

    @Autowired
    public PeanutBankController(BankAccountService bankAccountService,
                                ApplicationAppDao applicationAppDao,
                                BankChargeService bankChargeService) {
        this.bankAccountService = bankAccountService;
        this.applicationAppDao = applicationAppDao;
        this.bankChargeService = bankChargeService;
    }

    @PostMapping("/new_account")
    public Token newAccount(@RequestBody BankAccount account) {
        return bankAccountService.createNewBankAccount(account);
    }

    @GetMapping("/all_accounts")
    public List<BankAccount> allAccounts() {
        return bankAccountService.getAllBankAccounts();
    }

    @GetMapping("/usage")
    public Token usage(@Param("access_token") String access_token,
                       @Param("app_name") String appName) {
        String user = JWTAuthorizationFilter.verifyToken(access_token, SecurityConstants.SECRET, "");
        String appOwner = applicationAppDao.findByName(appName).getUsername();

        boolean isChargeable = user != null && (user.equals(appOwner) || bankChargeService.isAppUsageChargeable(user, appName));

        return isChargeable ? chargeUser(user, appName, appOwner) : new Token("No charge required", true);
        // App user
    }

    private Token chargeUser(String username, String appName, String appOwner) {
        bankAccountService.chargeUser(username);
        bankAccountService.payOwner(appOwner);
        bankChargeService.createBankCharge(username, appName);

        return new Token("Successful charge", true);
    }

}
