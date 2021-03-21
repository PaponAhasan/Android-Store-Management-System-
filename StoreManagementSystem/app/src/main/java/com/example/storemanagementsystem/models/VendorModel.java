package com.example.storemanagementsystem.models;

import java.io.Serializable;

//temporary
public class VendorModel implements Serializable {
    private String name;
    private String phone;
    private String email;
    private String address;
    private String date;

    public VendorModel(String name, String phone, String email, String address, String date) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
