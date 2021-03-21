package com.example.storemanagementsystem.entites;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "vendor_details_tbl")
public class VendorEntity implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private long id;
    @Ignore
    private String docId;

    private String name;
    private String phone;
    private String email;
    private String address;
    private String date;
    @Ignore
    private String ImageDownloadUrl;
    @Ignore
    public VendorEntity() {
        //required by firebase cloud fire-store db
    }

    @Ignore
    public VendorEntity(long id,String name, String phone, String email, String address, String date,String docId) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.date = date;
        this.docId = docId;
    }

    public VendorEntity(String name, String phone, String email, String address, String date) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getImageDownloadUrl() {
        return ImageDownloadUrl;
    }

    public void setImageDownloadUrl(String imageDownloadUrl) {
        ImageDownloadUrl = imageDownloadUrl;
    }
}
