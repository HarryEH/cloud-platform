package com.howarth.peanutbank;

import com.howarth.peanutbank.database.BankAccountRepository;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class PeanutBankViewController {

    private final BankAccountRepository bankAccountRepository;

    public PeanutBankViewController(Environment environment, BankAccountRepository bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;

    }

    @GetMapping("/bank/")
    public String bank(Model model, HttpServletRequest request){
        return homepage(model, request);
    }

    @GetMapping("/bank")
    public String bankHome(Model model, HttpServletRequest request){
        return homepage(model, request);
    }

    private String homepage(Model model, HttpServletRequest request) {
        //TODO find the current user by username and show their balance!!!
        // ^ get this out of the request

        model.addAttribute("account_holder", "daddy");
        model.addAttribute("account_balance", "number");

        return "peanut_bank";
    }
}
