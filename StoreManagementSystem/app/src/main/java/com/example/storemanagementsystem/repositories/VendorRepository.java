package com.example.storemanagementsystem.repositories;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.storemanagementsystem.DB.SMS_APPS_DB;
import com.example.storemanagementsystem.entites.VendorEntity;
import com.example.storemanagementsystem.viewmodels.AuthViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VendorRepository {
    private Context context;
    private final String COLLECTION_VENDOR = "Vendors";
    private FirebaseFirestore db;

    public VendorRepository(Context context) {
        this.context = context;
        db = FirebaseFirestore.getInstance();
    }

    public void insertVendor(VendorEntity vendorEntity){
        //insert localDatabase
        /*SMS_APPS_DB.dbExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                //runs on background threads
                SMS_APPS_DB.getDb(context).getVendorDao().InsertNewVendor(vendorEntity);
            }
        });*/

        //insert Firebase
        final DocumentReference doc = db.collection(COLLECTION_VENDOR).document();
        vendorEntity.setDocId(doc.getId());
        doc.set(vendorEntity).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    public LiveData<List<VendorEntity>>getAllVendors(){
        //Local database getAllVendorList
           //return SMS_APPS_DB.getDb(context).getVendorDao().getAllVendor();
        //Firebase database getAllVendorList
        MutableLiveData<List<VendorEntity>> vendorListLiveData = new MutableLiveData<>();

        db.collection(COLLECTION_VENDOR).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error!=null){
                    Log.e("fireStore","onEvent:"+error.getLocalizedMessage());
                    return;
                }
                List<VendorEntity>vendorEntities = new ArrayList<>();
                for(DocumentSnapshot ds:value.getDocuments()){
                    final  VendorEntity vendorEntity = ds.toObject(VendorEntity.class);
                    //final  VendorEntity vendorEntity = ds.get;
                    vendorEntities.add(vendorEntity);
                }
                vendorListLiveData.postValue(vendorEntities);
            }
        });

        return vendorListLiveData;
    }

    //Local database getVendorByID
    public LiveData<VendorEntity>getVendorByID(long id){
        return SMS_APPS_DB.getDb(context).getVendorDao().getVendorByID(id);
    }


    //Firebase database getVendorByID
    public LiveData<VendorEntity>getVendorByID(String docId){
        MutableLiveData<VendorEntity>vendorEntityMutableLiveData = new MutableLiveData<>();
        db.collection(COLLECTION_VENDOR).document(docId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error!=null) return;
                VendorEntity vendorEntity = value.toObject(VendorEntity.class);
                vendorEntityMutableLiveData.postValue(vendorEntity);
            }
        });
        return vendorEntityMutableLiveData;
    }

    public void deleteVendor(VendorEntity vendorEntity){
        //Local database delete
        /*SMS_APPS_DB.dbExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                SMS_APPS_DB.getDb(context).getVendorDao().DeleteNewVendor(vendorEntity);
            }
        });*/

        //Firebase database Delete
        db.collection(COLLECTION_VENDOR).document(vendorEntity.getDocId()).delete()
               .addOnSuccessListener(new OnSuccessListener<Void>() {
                   @Override
                   public void onSuccess(Void aVoid) {

                   }
               }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                
            }
        });

    }

    public void updateVendor(VendorEntity vendorEntity){
        //Local database Update
        /*SMS_APPS_DB.dbExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                SMS_APPS_DB.getDb(context).getVendorDao().UpdateNewVendor(vendorEntity);
            }
        });*/

        //Firebase database Update
        Map<String,Object> map = new HashMap<>();
        final DocumentReference doc = db.collection(COLLECTION_VENDOR).document(vendorEntity.getDocId());
        /*map.put("docId",vendorEntity.getDocId());
        map.put("address",vendorEntity.getAddress());
        map.put("date",vendorEntity.getDate());
        map.put("email",vendorEntity.getEmail());
        map.put("name",vendorEntity.getName());
        map.put("phone",vendorEntity.getPhone());*/
        //doc.update(map).addOnSuccessListener(new OnSuccessListener<Void>()
         doc.set(vendorEntity).addOnSuccessListener(new OnSuccessListener<Void>(){
            @Override
            public void onSuccess(Void aVoid) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    public void updateVendorImageUrl(String DocId,String Url){
        //Firebase database Update
        DocumentReference doc = db.collection(COLLECTION_VENDOR).document(DocId);
        Map<String,Object> map = new HashMap<>();
        map.put("ImageDownloadUrl",Url);
        doc.update(map);
        doc.update(map).addOnSuccessListener(new OnSuccessListener<Void>(){
            @Override
            public void onSuccess(Void aVoid) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
}
