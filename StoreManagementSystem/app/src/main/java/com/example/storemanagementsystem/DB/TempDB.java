package com.example.storemanagementsystem.DB;

import com.example.storemanagementsystem.models.VendorModel;

import java.util.ArrayList;
import java.util.List;

//temporary
public class TempDB {
    public static List<VendorModel> vendorList;
    static {
        vendorList = new ArrayList<>();
        vendorList.add(new VendorModel("Papon","01741885987","papon.ahasan@gmail.com",
                "Lakshmipur","30,Nov,2020"));
        vendorList.add(new VendorModel("Papon","01741885987","papon.ahasan@gmail.com",
                "Lakshmipur","30,Nov,2020"));
        vendorList.add(new VendorModel("Papon","01741885987","papon.ahasan@gmail.com",
                "Lakshmipur","30,Nov,2020"));
    }
}
