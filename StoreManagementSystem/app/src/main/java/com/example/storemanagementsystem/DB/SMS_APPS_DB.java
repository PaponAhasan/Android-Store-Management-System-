package com.example.storemanagementsystem.DB;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.storemanagementsystem.daos.VendorDao;
import com.example.storemanagementsystem.entites.VendorEntity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {VendorEntity.class},version = 1)
public abstract class SMS_APPS_DB extends RoomDatabase {
    public abstract VendorDao getVendorDao();
    private static SMS_APPS_DB db;
    public static ExecutorService dbExecutorService = Executors.newFixedThreadPool(4);

    public static SMS_APPS_DB getDb(Context context){
        if(db!=null){
            return db;
        }
        return db = Room.databaseBuilder(context, SMS_APPS_DB.class,"vendor_db")
                //.allowMainThreadQueries()
                .build();
    }
}
