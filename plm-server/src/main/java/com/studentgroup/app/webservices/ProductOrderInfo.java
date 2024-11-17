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
    private String markAndNumsText;
    private String packagesText;
    private String description;
    private String remarks;
    


    public ProductOrderInfo() {}
    
    /*
    JSON must look like this: 
    {
        BLNumber: String,
        orderDate: String(formatted date time with timezone)
        vesselName: String,
        voyNumber: String,
        cosigneeName: String,
        markAndNumsText: String,
        packagesText: String,
        description: String,
        remarks: String
    }
    */
    public static ProductOrderInfo fromJsonNode(JsonNode jsonNode) {
        ProductOrderInfo prodInfo = new ProductOrderInfo();

        prodInfo.BLNumber = Misc.jsonToString(jsonNode, "BLNumber");
        prodInfo.vesselName = Misc.jsonToString(jsonNode, "vesselName");
        prodInfo.voyNumber = Misc.jsonToString(jsonNode, "voyNumber");
        prodInfo.cosigneeName = Misc.jsonToString(jsonNode, "cosigneeName");
        prodInfo.markAndNumsText = Misc.jsonToString(jsonNode, "markAndNumsText");
        prodInfo.packagesText = Misc.jsonToString(jsonNode, "packagesText");
        prodInfo.description = Misc.jsonToString(jsonNode, "description");
        prodInfo.remarks = Misc.jsonToString(jsonNode, "remarks");
        String dateString = Misc.jsonToString(jsonNode, "orderDate");

        if (prodInfo.BLNumber == null || 
        prodInfo == null || 
        prodInfo.voyNumber == null || 
        prodInfo.cosigneeName == null ||
        prodInfo.markAndNumsText == null ||
        prodInfo.packagesText == null ||
        prodInfo.description == null ||
        prodInfo.remarks == null 
        ) {
            return null;
        }

        //truncate nanoseconds
        dateString = dateString.substring(0, dateString.indexOf(".")) + "Z";
        
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

    public String getMarkAndNumsText() {
        return markAndNumsText;
    }

    public String getPackagesText() {
        return packagesText;
    }

    public String getDescription() {
        return description;
    }

    public String getRemarks() {
        return remarks;
    }
}
