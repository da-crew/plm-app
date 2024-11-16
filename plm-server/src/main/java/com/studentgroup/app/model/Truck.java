package com.studentgroup.app.model;

import java.util.ArrayList;
import java.util.List;

//import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;

@Entity
@Table(name = "TRUCK")
public class Truck {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TRUCK_ID")
    private Long id;

    @Column(name = "TRUCK_NUMBER")
    private String truckNumber;

    @OneToMany(cascade = CascadeType.MERGE, mappedBy = "truck")
    @JsonManagedReference
    private List<Car> cars = new ArrayList<>();
    
    //constructor
    public Truck() {}
    public Truck(String truckNumber) {
        this.truckNumber = truckNumber;
    }

    //misc methods
    public void addCar(Car car) {
        cars.add(car);
        car.setTruck(this);
    }

    //getters and setters

    public Long getId() {
        return id;
    }

    public String getTruckNumber() {
        return truckNumber;
    }

    public void setTruckNumber(String truckNumber) {
        this.truckNumber = truckNumber;
    }


    public List<Car> getCars() {
        return cars;
    }
    
}
