package com.howarth.cloud.mainapp.peanutbank;

import com.howarth.cloud.mainapp.peanutbank.database.BankAccountRepository;
import com.howarth.cloud.mainapp.peanutbank.database.model.BankAccount;
import com.howarth.cloud.mainapp.uploads.storage.database.ApplicationAppRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(value = "/peanut_bank")
public class PeanutBankController {

    private final BankAccountRepository bankAccountRepository;
    private final ApplicationAppRepository applicationAppRepository;

    public PeanutBankController(BankAccountRepository bankAccountRepository, ApplicationAppRepository applicationAppRepository) {
        this.bankAccountRepository = bankAccountRepository;
        this.applicationAppRepository = applicationAppRepository;
    }


    //FIXME this is an example of how you can create an account. - the signup microservice will post to this url itself
  /*
  $.ajax({
    type: "POST",
            url: "/peanut_bank/new_account",
            data: JSON.stringify({ username: "admin", balance: 0 }),
    success: function(data, textStatus, request){
      alert('it worked');
    },
    error: function (request, textStatus, errorThrown) {
      alert('it didn't work');
    },
    dataType: "json",
            contentType : "application/json"
  });
  */

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
        //TODO
    /*
    Call static Java method to get user from access_token
    So now you know who  is using the app.

    Take app_name and find user from JPA repo
    So now you have the app owner
    Now use the time the request came in to charge the current_username if they haven’t been charged that day
    (or whatever time period) and split and send the money to the app_owner_username account as well as the
    platform owner account (as specified in spec).
     */
        return new ValidUseToken("hello world", true);
    }

}
