package com.example.storemanagementsystem;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.storemanagementsystem.adapter.VendorAdapter;
import com.example.storemanagementsystem.databinding.FragmentSendSMSBinding;
import com.example.storemanagementsystem.entites.VendorEntity;
import com.example.storemanagementsystem.vendor.VendorDetails;
import com.example.storemanagementsystem.viewmodels.VendorViewModel;

import java.util.List;

public class Send_SMS extends Fragment {
    private FragmentSendSMSBinding binding;
    private AlertDialog.Builder alertDialog;
    private AlertDialog Dialog;
    private static final int REQUEST_SMS = 222;

    public Send_SMS() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSendSMSBinding.inflate(inflater);
        BundleByGetData();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ///binding.textView1.setText();
        binding.SendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(getActivity(), "coming Soon", Toast.LENGTH_SHORT).show();
                final String[] permission = {Manifest.permission.SEND_SMS};
                int permissionCheck = ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.SEND_SMS);
                if(permissionCheck== PackageManager.PERMISSION_GRANTED){
                    MyMessage();
                }else{
                    requestPermissions(permission,REQUEST_SMS);
                }
            }
        });
    }

    private void BundleByGetData(){
        final Bundle bundle = getArguments();
        if(bundle!=null) {
            final String phone = bundle.getString("phone");
            final String name = bundle.getString("name");
            binding.textView1.setText(name);
            binding.textView2.setText(phone);
        }
    }

    private void MyMessage() {
            final Bundle bundle = getArguments();
            final String phone = bundle.getString("phone");
            String sms = binding.MessageText.getText().toString();
            if (!phone.isEmpty()) {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phone, null, sms, null, null);
                Toast.makeText(getActivity(), "Message SEND", Toast.LENGTH_SHORT).show();
            }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==REQUEST_SMS){
            if(grantResults.length>=0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                MyMessage();
            }else{
                //Toast.makeText(getActivity(), "Please allow this permission", Toast.LENGTH_SHORT).show();
                alertDialog = new AlertDialog.Builder(getActivity());
                alertDialog.setTitle("Exit");
                alertDialog.setMessage("Please allow this permission");
                alertDialog.setIcon(R.drawable.ok);
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //finish();
                    }
                });
                Dialog = alertDialog.create();
                Dialog.show();
            }
        }
    }
}