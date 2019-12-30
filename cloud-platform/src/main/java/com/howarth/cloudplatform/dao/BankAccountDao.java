package com.howarth.cloudplatform.dao;

import com.howarth.cloudplatform.model.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankAccountDao extends JpaRepository<BankAccount, Long> {
    BankAccount findByUsername(String username);
}
