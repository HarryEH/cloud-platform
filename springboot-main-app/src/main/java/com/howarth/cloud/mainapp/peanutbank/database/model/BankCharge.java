package com.howarth.cloud.mainapp.peanutbank.database.model;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
public class BankCharge {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String username;
    private String app_name;

    @CreationTimestamp
    @Column
    private Timestamp chargeDate;

    public BankCharge() {
    }

    public BankCharge(String username, String app_name, Timestamp chargeDate) {
        this.username = username;
        this.app_name = app_name;
        this.chargeDate = chargeDate;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getApp_name() {
        return app_name;
    }

    public void setApp_name(String app_name) {
        this.app_name = app_name;
    }

    public Timestamp getChargeDate() {
        return chargeDate;
    }

    public void setChargeDate(Timestamp chargeDate) {
        this.chargeDate = chargeDate;
    }
}
