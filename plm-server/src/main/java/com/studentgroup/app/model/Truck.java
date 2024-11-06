package com.studentgroup.app.model;

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

    @ManyToOne
    private ProductOrder productOrder;

    @OneToMany(mappedBy = "TRUCK")
    private List<Car> cars;
}
