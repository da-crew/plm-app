package com.studentgroup.app.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    @Column(name = "ORDER_DATE") private Date orderDate;
    @Column(name = "VESSEL") private String vesselName;
    @Column(name = "VOY_NO") private String voyNumber;
    @Column(name = "COSIGNEE") private String cosigneeName;
    @Column(name = "WHARF_RECEIPT_IMAGE") private String wharfReceiptImgUrl;
    @Column(name = "TOTAL_TRUCKS") private Integer totalTrucks;
    @Enumerated(EnumType.STRING) @Column(name = "STATUS") private ProductOrderStatus statusName = ProductOrderStatus.UNKNOWN;

    //table relationships
    @ManyToOne private EmployeeUser checker;
    @OneToMany(mappedBy = "productOrder") private List<ActionLog> actionLogs;
    @OneToMany(mappedBy = "productOrder") private List<Truck> trucks;
    

    //constructor
    public ProductOrder() {}
    public ProductOrder(String bLNumber, Date orderDate, String vesselName, String voyNumber, String cosigneeName,
            String wharfReceiptImgUrl, Integer totalTrucks, ProductOrderStatus prodStatus) {
        BLNumber = bLNumber;
        this.orderDate = orderDate;
        this.vesselName = vesselName;
        this.voyNumber = voyNumber;
        this.cosigneeName = cosigneeName;
        this.wharfReceiptImgUrl = wharfReceiptImgUrl;
        this.totalTrucks = totalTrucks;
        this.statusName = prodStatus;
        trucks = new ArrayList<>();
        actionLogs = new ArrayList<>();
    }

    //getters and setters, this will make a bad Qi flow
    public String getBLNumber() {
        return BLNumber;
    }

    public void setBLNumber(String bLNumber) {
        BLNumber = bLNumber;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
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

    public Integer getTotalTrucks() {
        return totalTrucks;
    }

    public void setTotalTrucks(Integer totalTrucks) {
        this.totalTrucks = totalTrucks;
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

    public List<ActionLog> getActionLogs() {
        return actionLogs;
    }

    public void setActionLogs(List<ActionLog> actionLogs) {
        this.actionLogs = actionLogs;
    }

    public List<Truck> getTrucks() {
        return trucks;
    }

    public void setTrucks(List<Truck> trucks) {
        this.trucks = trucks;
    }

}
