package com.studentgroup.app.model;

import jakarta.persistence.*;


@Entity
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    
    @Column(name = "CAR_MODEL")
    String modelName;

    @OneToOne
    Report report;
}
