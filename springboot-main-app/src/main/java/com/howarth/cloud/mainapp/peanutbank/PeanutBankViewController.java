package com.howarth.cloud.mainapp.peanutbank;

import com.howarth.cloud.mainapp.peanutbank.database.BankAccountRepository;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

import static com.howarth.cloud.mainapp.security.JWTAuthorizationFilter.verifyCookieAuth;

@Controller
public class PeanutBankViewController {

    private final BankAccountRepository bankAccountRepository;

    public PeanutBankViewController(Environment environment, BankAccountRepository bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
    }

    @GetMapping("/peanut_bank/")
    public String bank(Model model, HttpServletRequest request){
        return homepage(model, request);
    }

    @GetMapping("/peanut_bank")
    public String bankHome(Model model, HttpServletRequest request){
        return homepage(model, request);
    }

    private String homepage(Model model, HttpServletRequest request) {
        String username = verifyCookieAuth(request);
        if (username != null) {
            model.addAttribute("account_holder", username);
            model.addAttribute("account_balance", bankAccountRepository.findByUsername(username).getBalance());

            return "peanut_bank";
        } else {
            return "error";
        }
    }
}
