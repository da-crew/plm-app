package com.studentgroup.app.model;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ListModel;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.studentgroup.app.model.enums.ProductOrderStatus;

import jakarta.persistence.*;

@Entity
@Table(name = "PRODUCT_ORDER")
public class ProductOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "PROD_ORDER_ID")
    private Long id; 

    //product order details
    @Column(name = "BL_NO") private String BLNumber;
    @Column(name = "ORDER_DATE") @Temporal(TemporalType.TIMESTAMP) private ZonedDateTime orderDate;
    @Column(name = "VESSEL") private String vesselName;
    @Column(name = "VOY_NO") private String voyNumber;
    @Column(name = "COSIGNEE") private String cosigneeName;
    @Column(name = "WHARF_RECEIPT_IMAGE") private String wharfReceiptImgUrl;
    @Enumerated(EnumType.STRING) @Column(name = "STATUS") private ProductOrderStatus statusName = ProductOrderStatus.UNKNOWN;

    //table relationships

    @ManyToOne
    @JoinColumn(name = "CHECKER_ID")
    @JsonBackReference
    private EmployeeUser checker;
    
    @ManyToOne
    @JoinColumn(name = "DISPATCHER_ID")
    @JsonBackReference
    private EmployeeUser dispatcher;

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "productOrder")
    @JsonManagedReference
    private List<ActionLog> actionLogs = new ArrayList<>();

    @OneToMany(cascade = CascadeType.MERGE, mappedBy = "productOrder")
    @JsonManagedReference
    private List<Car> cars = new ArrayList<>();

    //constructor
    public ProductOrder() {}
    public ProductOrder(String bLNumber, ZonedDateTime orderDate, String vesselName, String voyNumber, String cosigneeName,
            String wharfReceiptImgUrl, ProductOrderStatus prodStatus) {
        BLNumber = bLNumber;
        this.orderDate = orderDate;
        this.vesselName = vesselName;
        this.voyNumber = voyNumber;
        this.cosigneeName = cosigneeName;
        this.wharfReceiptImgUrl = wharfReceiptImgUrl;
        this.statusName = prodStatus;
    }

    //misc methods

    public void addActionLog(ActionLog log) {
        actionLogs.add(log);
        log.setProductOrder(this);
    }

    public void addCar(Car car) {
        cars.add(car);
    }

    //getters and setters
    public String getBLNumber() {
        return BLNumber;
    }

    public void setBLNumber(String bLNumber) {
        BLNumber = bLNumber;
    }

    public ZonedDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(ZonedDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public String getVesselName() {
        return vesselName;
    }

    public void setVesselName(String vesselName) {
        this.vesselName = vesselName;
    }

    public String getVoyNumber() {
        return voyNumber;
    }

    public void setVoyNumber(String voyNumber) {
        this.voyNumber = voyNumber;
    }

    public String getCosigneeName() {
        return cosigneeName;
    }

    public void setCosigneeName(String cosigneeName) {
        this.cosigneeName = cosigneeName;
    }

    public String getWharfReceiptImgUrl() {
        return wharfReceiptImgUrl;
    }

    public void setWharfReceiptImgUrl(String wharfReceiptImgUrl) {
        this.wharfReceiptImgUrl = wharfReceiptImgUrl;
    }

    public ProductOrderStatus getStatusName() {
        return statusName;
    }

    public void setStatusName(ProductOrderStatus statusName) {
        this.statusName = statusName;
    }

    public EmployeeUser getChecker() {
        return checker;
    }

    public void setChecker(EmployeeUser checker) {
        this.checker = checker;
    }

    public EmployeeUser getDispatcher() {
        return dispatcher;
    }
    public void setDispatcher(EmployeeUser dispatcher) {
        this.dispatcher = dispatcher;
    }

    public List<ActionLog> getActionLogs() {
        return actionLogs;
    }

    public List<Car> getCars() {
        return cars;
    }

    //public List<Truck> getTrucks() {
    //    return trucks;
    //}
}
