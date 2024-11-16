package com.studentgroup.app.model;

import java.time.ZonedDateTime;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.studentgroup.app.model.serializer.EmployeeUserFieldSerializer;
import com.studentgroup.app.model.serializer.ProductOrderFieldSerializer;

import jakarta.persistence.*;

@Entity
@Table(name = "ACTION_LOG")
public class ActionLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ACTION_LOG_ID")
    private Long id;

    @Column(name = "TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private ZonedDateTime timestamp;

    @Column(name = "ACTION")
    private String actionText;

    //table relationships
    @ManyToOne
    @JoinColumn(name = "PROD_ORDER_ID")
    @JsonSerialize(using = ProductOrderFieldSerializer.class)
    private ProductOrder productOrder;

    @ManyToOne
    @JoinColumn(name = "EMP_ID")
    @JsonSerialize(using = EmployeeUserFieldSerializer.class)
    private EmployeeUser employee;


    //constructor   
    public ActionLog() {}
    public ActionLog(ZonedDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public ActionLog(String action) {
        this.timestamp = ZonedDateTime.now();
        this.actionText = action;
    }

    public ActionLog(String action, ZonedDateTime timestamp) {
        this.timestamp = timestamp;
        this.actionText = action;
    }

    //getter and setters
    public Long getId() {
        return id;
    }

    public String getActionText() {
        return actionText;
    }
    public void setActionText(String actionText) {
        this.actionText = actionText;
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
