package com.howarth.cloudplatform.controller;

import com.howarth.cloudplatform.dao.BankAccountDao;
import com.howarth.cloudplatform.dao.BankChargeDao;
import com.howarth.cloudplatform.model.BankCharge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.howarth.cloudplatform.security.JWTAuthorizationFilter.verifyCookieAuth;

@Controller
public class PeanutBankViewController {

    private final BankAccountDao bankAccountDao;
    private final BankChargeDao bankChargeDao;

    @Autowired
    public PeanutBankViewController(BankAccountDao bankAccountDao,
                                    BankChargeDao bankChargeDao) {
        this.bankAccountDao = bankAccountDao;
        this.bankChargeDao = bankChargeDao;
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
            List<BankCharge> bankChargeList = bankChargeDao.findByUsername(username);
            model.addAttribute("charges", bankChargeList);
            model.addAttribute("account_holder", username);
            model.addAttribute("account_balance", bankAccountDao.findByUsername(username).getBalance());

            return "peanut_bank";
        } else {
            return "error";
        }
    }
}
