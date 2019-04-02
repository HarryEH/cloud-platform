package com.howarth.cloud.mainapp.library.database.model;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
public class RhhStatistic {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;

    @CreationTimestamp
    @Column
    private Timestamp createdDate;
    private int numberOfPeople;

    public RhhStatistic(){}

    public RhhStatistic(int numberOfPeople){
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
