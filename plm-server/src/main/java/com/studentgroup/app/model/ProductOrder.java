package com.studentgroup.app.model;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

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
    @JoinColumn(name = "EMP_ID")
    @JsonBackReference
    private EmployeeUser checker;

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "productOrder")
    @JsonManagedReference
    private List<ActionLog> actionLogs;

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "productOrder")
    @JsonManagedReference
    private List<Truck> trucks;
    

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
        trucks = new ArrayList<>();
        actionLogs = new ArrayList<>();
    }

    //misc methods

    public void addActionLog(ActionLog log) {
        actionLogs.add(log);
        log.setProductOrder(this);
    }

    public void addTruck(Truck truck) {
        trucks.add(truck);
        truck.setProductOrder(this);
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

    //public Integer getTotalTrucks() {
    //    return totalTrucks;
    //}

    //public void setTotalTrucks(Integer totalTrucks) {
    //    this.totalTrucks = totalTrucks;
    //}

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

    public List<ActionLog> getActionLogs() {
        return actionLogs;
    }

    //public void setActionLogs(List<ActionLog> actionLogs) {
    //    this.actionLogs = actionLogs;
    //}

    public List<Truck> getTrucks() {
        return trucks;
    }

    //public void setTrucks(List<Truck> trucks) {
    //    this.trucks = trucks;
    //}

}
