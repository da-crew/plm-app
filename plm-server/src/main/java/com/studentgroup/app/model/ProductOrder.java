package com.studentgroup.app.model;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "product_order")
public class ProductOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "prod_order_id")
    private Long id; 

    @Column(name = "BL_NO")
    String BLNumber;

    @Column(name = "ORDER_DATE")
    Date orderDate;

    @Column(name = "VESSEL")
    String vesselName;

    @Column(name = "VOY_NO")
    String voyNumber;

    @Column(name = "COSIGNEE")
    String cosigneeName;

    @Column(name = "WHARF_RECEIPT_IMAGE")
    String wharfReceiptImgUrl;

    @Column(name = "TOTAL_TRUCKS")
    int totalTrucks;
    
    @Column(name = "STATUS")
    String statusName;


    @OneToOne()
    EmployeeUser checker;

}
