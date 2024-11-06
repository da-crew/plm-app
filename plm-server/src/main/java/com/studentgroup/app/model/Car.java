package com.studentgroup.app.model;

import jakarta.persistence.*;


@Entity
@Table(name = "CAR")
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "CAR_ID")
    private Long id;
    
    @Column(name = "CAR_MODEL")
    private String modelName;

    //table relationships 
    @OneToOne(cascade = CascadeType.ALL)
    //@JoinColumn(name = "CAR_ID") THIS BREAKS THE BACKEND!
    private Report report;

    @ManyToOne
    //@JoinColumn(name = "CAR_ID") THIS BREAKS THE BACKEND!
    private Truck truck;

    //constructors
    public Car() {}

    public Car(String modelName) {
        this.modelName = modelName;
    }

    public Car(String modelName, Report report) {
        this.modelName = modelName;
        this.report = report;
    }
    
    //getters and setters
    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public Report getReport() {
        return report;
    }

    public void setReport(Report report) {
        this.report = report;
    }

    public Truck getTruck() {
        return truck;
    }

    public void setTruck(Truck truck) {
        this.truck = truck;
    }
}
