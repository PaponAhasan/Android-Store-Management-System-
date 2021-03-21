package com.example.storemanagementsystem.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.storemanagementsystem.entites.VendorEntity;

import java.util.List;

@Dao
public interface VendorDao {
    @Insert
    void InsertNewVendor(VendorEntity vendorEntity);

    @Update
    void UpdateNewVendor(VendorEntity vendorEntity);

    @Delete
    void DeleteNewVendor(VendorEntity vendorEntity);

    @Query("select * from vendor_details_tbl")
    LiveData<List<VendorEntity>> getAllVendor();

    @Query("select * from vendor_details_tbl where id = :rowID")
    LiveData<VendorEntity>getVendorByID(long rowID);
}
