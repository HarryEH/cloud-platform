package com.howarth.cloud.mainapp.library.database.model;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
public class DiamondStatistic {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;

    @CreationTimestamp
    @Column
    private Timestamp createdDate;
    private int numberOfPeople;

    public DiamondStatistic(){}

    public DiamondStatistic(int numberOfPeople){
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
