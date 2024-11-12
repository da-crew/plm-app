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

    @Column(name = "DAMAGE_IMAGE")
    private String damageImageLink;

    //table relationships
    //@OneToOne(cascade = CascadeType.PERSIST, mappedBy = "car")
    //@JoinColumn(name = "REPORT_ID")
    //private Report report;

    @ManyToOne
    @JoinColumn(name = "TRUCK_ID")

    private Truck truck;

    //constructors
    public Car() {}

    public Car(String modelName) {
        this.modelName = modelName;
    }
    
    //getters and setters

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    
    public Truck getTruck() {
        return truck;
    }

    public void setTruck(Truck truck) {
        this.truck = truck;
    }

    public String getDamageReport() {
        return damageReport;
    }

    public void setDamageReport(String damageReport) {
        this.damageReport = damageReport;
    }

    public String getDamageImageLink() {
        return damageImageLink;
    }

    public void setDamageImageLink(String damageImageLink) {
        this.damageImageLink = damageImageLink;
    }
}
