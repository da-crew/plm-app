package com.studentgroup.app.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

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
    @ManyToOne
    @JoinColumn(name = "TRUCK_ID")
    @JsonBackReference
    private Truck truck;

    @ManyToOne
    @JoinColumn(name = "PROD_ORDER_ID")
    @JsonBackReference
    private ProductOrder productOrder;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Report> reports = new ArrayList<>();

    public void addReport(Report report) {
        reports.add(report);
        report.setCar(this);
    }

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

    public List<Report> getReports() {
        return reports;
    }


    
    public Truck getTruck() {
        return truck;
    }

    public void setTruck(Truck truck) {
        this.truck = truck;
    }

    public ProductOrder getProductOrder() {
        return productOrder;
    }

    public void setProductOrder(ProductOrder productOrder) {
        this.productOrder = productOrder;
    }
}
