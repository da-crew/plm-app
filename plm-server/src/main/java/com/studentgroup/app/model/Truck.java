package com.studentgroup.app.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "TRUCK")
public class Truck {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TRUCK_ID")
    private Long id;

    //we probably dont need this
    @Column(name = "TRUCK_NUMBER")
    private String truckNumber;

    //@Column(name = "TOTAL_CARS")
    //private Integer totalCars;

    @ManyToOne
    @JoinColumn(name = "PROD_ORDER_ID")
    private ProductOrder productOrder;

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "truck")
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
