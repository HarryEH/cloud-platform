package com.stevenson.grades.database.model;

import javax.persistence.*;

@Entity
public class Grade {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;
    @Column
    private String name;
    @Column
    private double grade;
    @Column
    private double maxGrade;
    @Column
    private double percentage;
    @Column
    private double weight; //as a percentage, i.e. x% of the module
    @Column
    private double contribution;

    protected Grade(){}

    public Grade(String name, double grade, double maxGrade, double weight){
        this.name = name;
        this.grade = grade;
        this.maxGrade = maxGrade;
        this.percentage = 100 * (grade / maxGrade);
        this.weight = weight;
        this.contribution = weight * (percentage / 100);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
        this.percentage = 100 * (grade / maxGrade);
        this.contribution = weight * (percentage / 100);
    }

    public double getMaxGrade() {
        return maxGrade;
    }

    public void setMaxGrade(double maxGrade) {
        this.maxGrade = maxGrade;
        this.percentage = 100 * (grade / maxGrade);
        this.contribution = weight * (percentage / 100);
    }

    public double getPercentage() {
        return percentage;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
        this.contribution = grade * (weight / 100);
    }

    public double getContribution() {
        return contribution;
    }
}
