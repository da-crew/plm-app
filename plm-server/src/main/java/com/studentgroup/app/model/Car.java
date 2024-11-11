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

    @Column(name = "DAMAGE_REPORT")
    private String damageReport;

    public String getDamageReport() {
        return damageReport;
    }

    public void setDamageReport(String damageReport) {
        this.damageReport = damageReport;
    }

    @Column(name = "DAMAGE_IMAGE")
    private String damageImageLink;

    //table relationships
    //@OneToOne(cascade = CascadeType.PERSIST, mappedBy = "car")
    //@JoinColumn(name = "REPORT_ID")
    //private Report report;

    public String getDamageImageLink() {
        return damageImageLink;
    }

    public void setDamageImageLink(String damageImageLink) {
        this.damageImageLink = damageImageLink;
    }

    @ManyToOne
    @JoinColumn(name = "TRUCK_ID")
    private Truck truck;

    //constructors
    public Car() {}

    public Car(String modelName) {
        this.modelName = modelName;
    }

    public Car(String modelName, Report report) {
        this.modelName = modelName;
        //this.report = report;
    }
    
    //getters and setters
    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    //public Report getReport() {
    //    return report;
    //}

    //public void setReport(Report report) {
        //report.setCar(this);
    //    this.report = report;
    //}

    public Truck getTruck() {
        return truck;
    }

    public void setTruck(Truck truck) {
        this.truck = truck;
    }
}
