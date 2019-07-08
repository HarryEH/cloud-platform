package com.howarth.cloud.mainapp.peanutbank.database;

import com.howarth.cloud.mainapp.peanutbank.database.model.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {
    BankAccount findByUsername(String username);
}
