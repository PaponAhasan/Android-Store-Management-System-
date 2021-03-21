package com.example.storemanagementsystem.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.storemanagementsystem.entites.VendorEntity;
import com.example.storemanagementsystem.repositories.VendorRepository;

import java.util.List;

public class VendorViewModel extends AndroidViewModel {
    private VendorRepository vendorRepository;
    public VendorViewModel(@NonNull Application application) {
        super(application);
        vendorRepository = new VendorRepository(application);
    }

    public void addVendor(VendorEntity vendorEntity){
        vendorRepository.insertVendor(vendorEntity);
    }

    public  void deleteVendor(VendorEntity vendorEntity){
        vendorRepository.deleteVendor(vendorEntity);
    }

    public  void updateVendor(VendorEntity vendorEntity){
        vendorRepository.updateVendor(vendorEntity);
    }
    public LiveData<List<VendorEntity>>fetchAllVendors(){
        return vendorRepository.getAllVendors();
    }
    public LiveData<VendorEntity>fetchVendorByID(long id){
        return vendorRepository.getVendorByID(id);
    }
    public LiveData<VendorEntity>fetchVendorByID(String  DocId){
        return vendorRepository.getVendorByID(DocId);
    }
    public  void updateVendorImageUri(String DocId,String Uri){
        vendorRepository.updateVendorImageUrl(DocId,Uri);
    }
}
