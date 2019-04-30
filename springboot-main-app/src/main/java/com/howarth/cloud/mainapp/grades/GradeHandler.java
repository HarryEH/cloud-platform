package com.howarth.cloud.mainapp.grades;

public class GradeHandler {
    private long id;
    private long parentId;
    private String name;
    private double grade;
    private double maxGrade;
    private double weight;

    public GradeHandler(){
        id = 0;
        parentId = 0;
        name = "";
        grade = 0;
        maxGrade = 0;
        weight = 0;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getParentId() {
        return parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
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
    }

    public double getMaxGrade() {
        return maxGrade;
    }

    public void setMaxGrade(double maxGrade) {
        this.maxGrade = maxGrade;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}
