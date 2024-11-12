package com.studentgroup.app.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
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

    @ManyToOne
    @JoinColumn(name = "PROD_ORDER_ID")
    @JsonBackReference
    private ProductOrder productOrder;

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "truck")
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
    public String getTruckNumber() {
        return truckNumber;
    }

    public void setTruckNumber(String truckNumber) {
        this.truckNumber = truckNumber;
    }
    
    public ProductOrder getProductOrder() {
        return productOrder;
    }
    public void setProductOrder(ProductOrder productOrder) {
        this.productOrder = productOrder;
    }

    //public Integer getTotalCars() {
    //    return totalCars;
    //}


    //public void setTotalCars(Integer totalCars) {
    //    this.totalCars = totalCars;
    //}


    public List<Car> getCars() {
        return cars;
    }


    //public void setCars(List<Car> cars) {
    //    this.cars = cars;
    //}



}
