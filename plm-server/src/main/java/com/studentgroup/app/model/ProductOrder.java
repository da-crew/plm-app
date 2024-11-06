package com.studentgroup.app.model;

import java.util.Date;
import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "PRODUCT_ORDER")
public class ProductOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "PROD_ORDER_ID")
    private Long id; 

    @Column(name = "BL_NO")
    private String BLNumber;

    @Column(name = "ORDER_DATE")
    private Date orderDate;

    @Column(name = "VESSEL")
    private String vesselName;

    @Column(name = "VOY_NO")
    private String voyNumber;

    @Column(name = "COSIGNEE")
    private String cosigneeName;

    @Column(name = "WHARF_RECEIPT_IMAGE")
    private String wharfReceiptImgUrl;

    @Column(name = "TOTAL_TRUCKS")
    private Integer totalTrucks;
    
    @Column(name = "STATUS")
    private String statusName;


    @ManyToOne
    private EmployeeUser checker;

    @OneToMany(mappedBy = "productOrder")
    private List<ActionLog> actionLogs;

    @OneToMany(mappedBy = "productOrder")
    private List<Truck> trucks;

}
