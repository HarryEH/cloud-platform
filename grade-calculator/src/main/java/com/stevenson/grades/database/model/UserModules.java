package com.stevenson.grades.database.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class UserModules {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;
    @Column(nullable = false)
    private long userID;
    @ElementCollection
    private List<ModuleGrades> modules;
    @Column(nullable = false)
    private double totalCredits;
    @Column
    private double average;

    protected UserModules(){}

    public UserModules(long userID, double totalCredits){
        this.userID = userID;
        this.modules = new ArrayList<>();
        this.totalCredits = totalCredits;
        this.average = 0;
    }

    public UserModules(long userID, List<ModuleGrades> modules, double totalCredits){
        this.userID = userID;
        this.modules = modules;
        this.totalCredits = totalCredits;
        updateAverage();
    }

    public long getUserID() {
        return userID;
    }

    public List<ModuleGrades> getModules() {
        return modules;
    }

    public void setModules(List<ModuleGrades> modules) {
        this.modules = modules;
        updateAverage();
    }

    public double getTotalCredits() {
        return totalCredits;
    }

    public void setTotalCredits(double totalCredits) {
        this.totalCredits = totalCredits;
    }

    public double getAverage() {
        return average;
    }

    public double getCredits(){
        double credits = 0;
        for(ModuleGrades m : modules){
            //only count credits from fully completed modules
            double moduleCompleted = 0;
            for(Grade g : m.getGrades()){
                moduleCompleted += g.getWeight();
            }
            credits += moduleCompleted >= 100 ? m.getCredits() : 0;
        }
        return credits;
    }

    public double getRemainingCredits(){
        double remaining = totalCredits - getCredits();
        if(remaining < 0) remaining = 0;
        return remaining;
    }

    public double[] getTargets(){
        //pass (40), 2:2 (50), 2:1 (60), 1st (70)
        double[] targets = {0, 0, 0, 0};
        double current = getCredits() * average;

        for(int i = 0; i < 4; i++){
            //boundary is what credits * average would be if averaging target in each module
            double boundary = ((10 * i) + 40) * totalCredits;
            double diff = boundary - current;
            if(diff < 0) diff = 0;
            targets[i] = diff / getRemainingCredits();
        }

        return targets;
    }

    public void addModule(ModuleGrades module){
        this.modules.add(module);
        updateAverage();
    }

    public void addModules(List<ModuleGrades> modules){
        this.modules.addAll(modules);
        updateAverage();
    }

    public void updateAverage(){
        average = 0;
        for(ModuleGrades m : modules){
            average += m.getAverage();
        }
        average = average / modules.size();
    }
}
