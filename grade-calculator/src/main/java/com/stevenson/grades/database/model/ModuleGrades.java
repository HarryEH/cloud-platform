package com.stevenson.grades.database.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
public class ModuleGrades {
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
    private Map<String, Double> grades = new HashMap<String, Double>();

    protected ModuleGrades(){}

    public ModuleGrades(String moduleName, double credits){
        this.moduleName = moduleName;
        this.credits = credits;
    }

    public ModuleGrades(String moduleName, double credits, List<String> names, List<Double> scores){
        this.moduleName = moduleName;
        this.credits = credits;
        this.grades = zipGrades(names, scores);

        updateAverage();
    }

    public ModuleGrades(String moduleName, double credits, Map<String, Double> grades){
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

    public Map<String, Double> getGrades() {
        return grades;
    }

    public List<String> getNames(){
        return new ArrayList<>(grades.keySet());
    }

    public List<Double> getScores() {
        return new ArrayList<>(grades.values());
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public void setCredits(double credits) {
        this.credits = credits;
    }

    public void setGrades(List<String> names, List<Double> scores){
        this.grades = zipGrades(names, scores);
        updateAverage();
    }

    public void setGrades(Map<String, Double> grades) {
        this.grades = grades;
        updateAverage();
    }

    public void addGrades(List<String> names, List<Double> scores){
        this.grades.putAll(zipGrades(names, scores));
        updateAverage();
    }

    public void addGrade(String name, Double score){
        this.grades.put(name, score);
        updateAverage();
    }

    private Map<String, Double> zipGrades(List<String> l1, List<Double> l2){
        Map<String, Double> m1 = new HashMap<>();

        int len = l1.size() > l2.size() ? l2.size() : l1.size();
        for(int i = 0; i < len; i++){
            m1.put(l1.get(i), l2.get(i));
        }

        return m1;
    }

    private void updateAverage(){
        average = 0;
        for(Double d : grades.values()){
            average += d;
        }
        average = average / grades.size();
    }
}
