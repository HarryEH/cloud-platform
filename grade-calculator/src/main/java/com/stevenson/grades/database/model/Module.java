package com.stevenson.grades.database.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Module {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;
    @Column (nullable = false)
    private String moduleName;
    @Column (nullable = false)
    private double credits;
    @Column
    private double average;
    @ElementCollection
    @OneToMany(cascade = {CascadeType.MERGE})
    private List<Grade> grades = new ArrayList<>();

    protected Module(){}

    public Module(String moduleName, double credits){
        this.moduleName = moduleName;
        this.credits = credits;
        this.average = 0;
    }

    public Module(String moduleName, double credits, List<Grade> grades){
        this.moduleName = moduleName;
        this.credits = credits;
        this.grades = grades;

        updateAverage();
    }

    public long getId(){
        return id;
    }

    public String getModuleName() {
        return moduleName;
    }

    public double getCredits() {
        return credits;
    }

    public double getAverage() {
        return average;
    }

    public List<Grade> getGrades() {
        return grades;
    }

    public double getRemaining(){
        double remaining = 100;
        for(Grade g : grades){
            remaining -= g.getWeight();
        }

        if(remaining < 0) remaining = 0;
        return remaining;
    }

    public double getContributed(){
        double contribution = 0;
        for(Grade g : grades){
            contribution += g.getContribution();
        }
        return contribution;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public void setCredits(double credits) {
        this.credits = credits;
    }

    public void setGrades(List<Grade> grades) {
        this.grades = grades;
        updateAverage();
    }

    public void addGrade(Grade grade){
        this.grades.add(grade);
        updateAverage();
    }

    public void addGrades(List<Grade> grades){
        this.grades.addAll(grades);
        updateAverage();
    }

    private void updateAverage(){
        average = 0;
        double weighting = 0;
        for(Grade g : grades){
            average += (g.getPercentage() * g.getWeight());
            weighting += g.getWeight();
        }
        if(weighting == 0) weighting = 1;
        average = average / weighting;
    }

    public void delGrade(Grade grade){
        grades.remove(grade);
        updateAverage();
    }

    public boolean equals(Object obj){
        if(obj.getClass() != Module.class) return false;
        return (this.getId() == ((Module)obj).getId());
    }
}
