package com.orionsoft.vsafe.model;

import java.io.Serializable;

public class User implements Serializable {

    private String NICNumber;
    private String firstName;
    private String lastName;
    private String gender;
    private String dob;
    private String address;
    private String mobNumber;
    private String emailAddress;
    private String bloodGroup;

    public User(String NICNumber, String firstName, String lastName, String mobNumber, String emailAddress) {
        this.NICNumber = NICNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.mobNumber = mobNumber;
        this.emailAddress = emailAddress;
    }

    public User(String NICNumber, String firstName, String lastName, String gender, String dob, String address, String mobNumber, String emailAddress, String bloodGroup) {
        this.NICNumber = NICNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.dob = dob;
        this.address = address;
        this.mobNumber = mobNumber;
        this.emailAddress = emailAddress;
        this.bloodGroup = bloodGroup;
    }

    public String getNICNumber() {
        return NICNumber;
    }

    public void setNICNumber(String NICNumber) {
        this.NICNumber = NICNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobNumber() {
        return mobNumber;
    }

    public void setMobNumber(String mobNumber) {
        this.mobNumber = mobNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }
}
