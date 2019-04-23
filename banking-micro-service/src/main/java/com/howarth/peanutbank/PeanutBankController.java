package com.howarth.peanutbank;

import com.howarth.peanutbank.database.BankAccountRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/peanut_bank")
public class PeanutBankController {

  private BankAccountRepository bankAccountRepository;

  public PeanutBankController(BankAccountRepository bankAccountRepository) {
    this.bankAccountRepository = bankAccountRepository;
  }

  @GetMapping("/usage")
    public ValidUseToken diamond(@Param("access_token") String access_token, @Param("app_name") String app_name,
                                 HttpServletRequest request) {
        return new ValidUseToken("hello world", true);
    }

}
