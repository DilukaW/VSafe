package com.orionsoft.vsafe.model;

import java.io.Serializable;

public class Case implements Serializable {

    private String id;
    private String situation;
    private String details;
    private String dateTime;
    private String location;
    private String frontImg;
    private String backImg;
    private int status;
    private String note;

    public Case(String id, String situation, String details, String dateTime, String location, String frontImg, String backImg, int status, String note) {
        this.id = id;
        this.situation = situation;
        this.details = details;
        this.dateTime = dateTime;
        this.location = location;
        this.frontImg = frontImg;
        this.backImg = backImg;
        this.status = status;
        this.note = note;
    }

    public Case() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSituation() {
        return situation;
    }

    public void setSituation(String situation) {
        this.situation = situation;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getFrontImg() {
        return frontImg;
    }

    public void setFrontImg(String frontImg) {
        this.frontImg = frontImg;
    }

    public String getBackImg() {
        return backImg;
    }

    public void setBackImg(String backImg) {
        this.backImg = backImg;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
