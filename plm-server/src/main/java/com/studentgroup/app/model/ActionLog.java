package com.studentgroup.app.model;

import java.util.Date;

import jakarta.persistence.*;

@Entity
@Table(name = "ACTION_LOG")
public class ActionLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ACTION_LOG_ID")
    private Long id;

    @ManyToOne
    private ProductOrder productOrder;

    @ManyToOne
    private EmployeeUser employee;

    @Column(name = "TIMESTAMP")
    private Date timestamp;
}
