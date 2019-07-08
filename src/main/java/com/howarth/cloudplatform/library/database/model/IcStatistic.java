package com.howarth.cloudplatform.library.database.model;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
public class IcStatistic {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @CreationTimestamp
    @Column
    private Timestamp createdDate;
    private int numberOfPeople;

    public IcStatistic() {
    }

    public IcStatistic(int numberOfPeople) {
        this.numberOfPeople = numberOfPeople;
    }

    public int getNumberOfPeople() {
        return numberOfPeople;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }


    public long getId() {
        return id;
    }
}
