package com.howarth.cloudplatform.service;

import com.howarth.cloudplatform.dao.BankAccountDao;
import com.howarth.cloudplatform.model.BankAccount;
import com.howarth.cloudplatform.model.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BankAccountService {
    private static final int USER_CHARGE = -5;
    private static final int OWNER_PAYMENT = 3;

    private final BankAccountDao bankAccountDao;

    @Autowired
    public BankAccountService(BankAccountDao bankAccountDao) {
        this.bankAccountDao = bankAccountDao;
    }

    public Token createNewBankAccount(BankAccount account) {
        if (bankAccountDao.findByUsername(account.getUsername()) != null) {
            return new Token("-", false);
        }
        account.setBalance(1000);
        BankAccount b = bankAccountDao.save(account);
        return new Token(b.getUsername(), true);
    }

    public List<BankAccount> getAllBankAccounts() {
        return bankAccountDao.findAll();
    }

    public void payOwner(String username) {
        changeBankBalances(username, OWNER_PAYMENT);
    }

    public void chargeUser(String username) {
        changeBankBalances(username, USER_CHARGE);
    }

    private void changeBankBalances(String username, int amount) {
        BankAccount bankAccount = bankAccountDao.findByUsername(username);
        int userBankBalance = bankAccount.getBalance();
        bankAccount.setBalance(userBankBalance + amount);
        bankAccountDao.save(bankAccount);
    }
}
