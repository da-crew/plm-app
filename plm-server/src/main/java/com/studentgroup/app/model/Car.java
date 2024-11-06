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

    @OneToOne(mappedBy = "car")
    private Report report;

    @ManyToOne
    private Truck truck;
}
