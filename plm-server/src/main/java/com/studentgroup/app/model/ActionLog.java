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

    //constructor   
    public ActionLog(Date timestamp) {
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

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
