package com.howarth.cloudplatform.virtualbank;

import com.howarth.cloudplatform.virtualbank.database.BankAccountRepository;
import com.howarth.cloudplatform.virtualbank.database.BankChargeRepository;
import com.howarth.cloudplatform.virtualbank.database.model.BankCharge;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.howarth.cloudplatform.security.JWTAuthorizationFilter.verifyCookieAuth;

@Controller
public class PeanutBankViewController {

    private final BankAccountRepository bankAccountRepository;
    private final BankChargeRepository bankChargeRepository;

    public PeanutBankViewController(BankAccountRepository bankAccountRepository, BankChargeRepository bankChargeRepository) {
        this.bankAccountRepository = bankAccountRepository;
        this.bankChargeRepository = bankChargeRepository;
    }

    @GetMapping("/peanut_bank/")
    public String bank(Model model, HttpServletRequest request) {
        return homepage(model, request);
    }

    @GetMapping("/peanut_bank")
    public String bankHome(Model model, HttpServletRequest request) {
        return homepage(model, request);
    }

    private String homepage(Model model, HttpServletRequest request) {
        String username = verifyCookieAuth(request);
        if (username != null) {
            List<BankCharge> bankChargeList = bankChargeRepository.findByUsername(username);
            model.addAttribute("charges", bankChargeList);
            model.addAttribute("account_holder", username);
            model.addAttribute("account_balance", bankAccountRepository.findByUsername(username).getBalance());

            return "peanut_bank";
        } else {
            return "error";
        }
    }
}
