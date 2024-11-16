package com.studentgroup.app.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "REPORT")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "DAMAGE_REPORT") private String damageReportText;
    @Column(name = "IMAGE_URL") private String imgUrl;

    @ManyToOne
    @JoinColumn(name = "CAR_ID")
    private Car car;

    //constructors
    public Report() {}
    public Report(String damageReportText, String imgUrl) {
        this.damageReportText = damageReportText;
        this.imgUrl = imgUrl;
    }

    //getters and setters
    
    public Long getId() {
        return id;
    }

    public String getDamageReportText() {
        return damageReportText;
    }

    public void setDamageReportText(String damageReportText) {
        this.damageReportText = damageReportText;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

}
