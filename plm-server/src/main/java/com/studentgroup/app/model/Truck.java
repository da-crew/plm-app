package com.studentgroup.app.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "TRUCK")
public class Truck {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "TRUCK_NUMBER")
    private String truckNumber;

    @Column(name = "TOTAL_CARS")
    private Integer totalCars;

    @OneToMany(mappedBy = "truck")
    private List<Car> cars;
    
    //constructor
    public Truck(String truckNumber, Integer totalCars) {
        this.truckNumber = truckNumber;
        this.totalCars = totalCars;
        this.cars = new ArrayList<>();
    }

    //getters and setters
    @ManyToOne
    private ProductOrder productOrder;
    
    public String getTruckNumber() {
        return truckNumber;
    }


    public void setTruckNumber(String truckNumber) {
        this.truckNumber = truckNumber;
    }


    public Integer getTotalCars() {
        return totalCars;
    }


    public void setTotalCars(Integer totalCars) {
        this.totalCars = totalCars;
    }


    public List<Car> getCars() {
        return cars;
    }


    public void setCars(List<Car> cars) {
        this.cars = cars;
    }



}
