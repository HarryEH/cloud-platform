package com.howarth.cloud.mainapp.peanutbank;

import com.howarth.cloud.mainapp.peanutbank.database.BankAccountRepository;
import com.howarth.cloud.mainapp.peanutbank.database.BankChargeRepository;
import com.howarth.cloud.mainapp.peanutbank.database.model.BankAccount;
import com.howarth.cloud.mainapp.peanutbank.database.model.BankCharge;
import com.howarth.cloud.mainapp.security.SecurityConstants;
import com.howarth.cloud.mainapp.uploads.storage.database.ApplicationAppRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

import static com.howarth.cloud.mainapp.security.JWTAuthorizationFilter.verifyToken;

@RestController
@RequestMapping(value = "/peanut_bank")
public class PeanutBankController {

    private final BankAccountRepository bankAccountRepository;
    private final ApplicationAppRepository applicationAppRepository;
    private final BankChargeRepository bankChargeRepository;

    public PeanutBankController(BankAccountRepository bankAccountRepository, ApplicationAppRepository applicationAppRepository, BankChargeRepository bankChargeRepository) {
        this.bankAccountRepository = bankAccountRepository;
        this.applicationAppRepository = applicationAppRepository;
        this.bankChargeRepository = bankChargeRepository;
    }

    @PostMapping("/new_account")
    public ValidUseToken newAccount(@RequestBody BankAccount account) {
        if (bankAccountRepository.findByUsername(account.getUsername()) != null) {
            return new ValidUseToken("-", false);
        }
        account.setBalance(1000);
        BankAccount b = bankAccountRepository.save(account);
        return new ValidUseToken(b.getUsername(), true);
    }

    @GetMapping("/all_accounts")
    public List<BankAccount> allAccounts() {
        return bankAccountRepository.findAll();
    }

    @GetMapping("/usage")
    public ValidUseToken usage(@Param("access_token") String access_token, @Param("app_name") String app_name,
                               HttpServletRequest request) {
        String user = verifyToken(access_token, SecurityConstants.SECRET, "");
        String app_owner = applicationAppRepository.findByName(app_name).getUsername();

        if (user.equals(app_owner)) {
            return new ValidUseToken("you own the app", true);
        }

        BankCharge bankCharge = bankChargeRepository.findByUsernameAndAppName(user, app_name);

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
        bankChargeRepository.save(bankChargeUser);

        return new ValidUseToken("successful charge", true);
    }

    private void chargeUser(String username, int charge) {
        BankAccount bankAccount = bankAccountRepository.findByUsername(username);
        int userBankBalance = bankAccount.getBalance();
        bankAccount.setBalance(userBankBalance + charge);
        bankAccountRepository.save(bankAccount);
    }

}
