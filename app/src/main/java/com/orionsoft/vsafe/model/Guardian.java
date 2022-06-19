package com.orionsoft.vsafe.model;

import java.io.Serializable;

public class Guardian implements Serializable {

    private String NICNumber;
    private String name;
    private String address;
    private String conNumber;
    private String relationship;

    public Guardian(String NICNumber, String name, String address, String conNumber, String relationship) {
        this.NICNumber = NICNumber;
        this.name = name;
        this.address = address;
        this.conNumber = conNumber;
        this.relationship = relationship;
    }

    public String getNICNumber() {
        return NICNumber;
    }

    public void setNICNumber(String NICNumber) {
        this.NICNumber = NICNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getConNumber() {
        return conNumber;
    }

    public void setConNumber(String conNumber) {
        this.conNumber = conNumber;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }
}
