package com.studentgroup.app.webservices;

import com.fasterxml.jackson.databind.JsonNode;
import com.studentgroup.app.Misc;

import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;

class ProductOrderInfo {

    private String BLNumber;
    private ZonedDateTime orderDate;
    private String vesselName;
    private String voyNumber;
    private String cosigneeName;

    public ProductOrderInfo() {}
    
    /*
    JSON must look like this: 
    {
        BLNumber: String,
        orderDate: String(formatted date time with timezone)
        vesselName: String,
        voyNumber: String,
        cosigneeName: String
    }
    */
    public static ProductOrderInfo fromJsonNode(JsonNode jsonNode) {
        ProductOrderInfo prodInfo = new ProductOrderInfo();

        prodInfo.BLNumber = Misc.jsonToString(jsonNode, "BLNumber");
        prodInfo.vesselName = Misc.jsonToString(jsonNode, "vesselName");
        prodInfo.voyNumber = Misc.jsonToString(jsonNode, "voyNumber");
        prodInfo.cosigneeName = Misc.jsonToString(jsonNode, "cosigneeName");
        String dateString = Misc.jsonToString(jsonNode, "orderDate");

        if (prodInfo.BLNumber == null || prodInfo == null || prodInfo.voyNumber == null || prodInfo.cosigneeName == null) {
            return null;
        }
        
        try {
            prodInfo.orderDate = ZonedDateTime.parse(dateString);
        } catch (DateTimeParseException e) {
            prodInfo.orderDate = null;
        }

        return prodInfo;
    }

    public String getBLNumber() {
        return BLNumber;
    }

    public ZonedDateTime getOrderDate() {
        return orderDate;
    }

    public String getVesselName() {
        return vesselName;
    }

    public String getVoyNumber() {
        return voyNumber;
    }

    public String getCosigneeName() {
        return cosigneeName;
    }
}
