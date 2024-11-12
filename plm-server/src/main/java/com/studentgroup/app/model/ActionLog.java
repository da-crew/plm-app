package com.studentgroup.app.model;

import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;

@Entity
@Table(name = "ACTION_LOG")
public class ActionLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ACTION_LOG_ID")
    private Long id;

    //table relationships
    @ManyToOne
    @JoinColumn(name = "PROD_ORDER_ID")
    @JsonBackReference
    private ProductOrder productOrder;

    @ManyToOne
    @JoinColumn(name = "EMP_ID")
    @JsonBackReference
    private EmployeeUser employee;

    @Column(name = "TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private ZonedDateTime timestamp;

    //constructor   
    public ActionLog() {}
    public ActionLog(ZonedDateTime timestamp) {
        this.timestamp = timestamp;
    }

    //getter and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProductOrder getProductOrder() {
        return productOrder;
    }

    public void setProductOrder(ProductOrder productOrder) {
        this.productOrder = productOrder;
    }

    public EmployeeUser getEmployee() {
        return employee;
    }

    public void setEmployee(EmployeeUser employee) {
        this.employee = employee;
    }

    public ZonedDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(ZonedDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
