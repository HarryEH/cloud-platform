package com.howarth.cloud.mainapp.grades.database.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class UserModule {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;
    @Column(nullable = false)
    private String username;
    @ElementCollection
    private List<Module> modules = new ArrayList<>();
    @Column(nullable = false)
    private double totalCredits;
    @Column
    private double average;

    protected UserModule(){}

    public UserModule(String username, double totalCredits){
        this.username = username;
        this.modules = new ArrayList<>();
        this.totalCredits = totalCredits;
        this.average = 0;
    }

    public UserModule(String username, List<Module> modules, double totalCredits){
        this.username = username;
        this.modules = modules;
        this.totalCredits = totalCredits;
        updateAverage();
    }

    public long getId(){
        return id;
    }

    public String getUsername() {
        return username;
    }

    public List<Module> getModules() {
        return modules;
    }

    public void setModules(List<Module> modules) {
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
        for(Module m : modules){
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

    public double getSpareCredits(){
        double spare = totalCredits;
        for(Module m : modules){
            spare -= m.getCredits();
        }
        if(spare < 0) spare = 0;

        return spare;
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
            if(getRemainingCredits() != 0) targets[i] = diff / getRemainingCredits();
        }

        return targets;
    }

    public void addModule(Module module){
        this.modules.add(module);
        updateCredits();
        updateAverage();
    }

    private void updateAverage(){
        average = 0;
        int numModules = 0;
        for(Module m : modules){
            //if progress has actually been made on module (i.e. not 0 because just started)
            if(m.getRemaining() < 100.0){
                average += m.getAverage();
                numModules ++;
            }
        }
        if(numModules == 0) numModules = 1;
        average = average / numModules;
    }

    public void delModule(Module module){
        modules.remove(module);
        updateCredits();
        updateAverage();
    }

    public void updateCredits(){
        this.totalCredits = 0;
        for(Module m : modules){
            this.totalCredits += m.getCredits();
        }
    }
}
