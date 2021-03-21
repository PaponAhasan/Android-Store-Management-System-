package com.example.storemanagementsystem.vendor;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;

import com.example.storemanagementsystem.DB.SMS_APPS_DB;
import com.example.storemanagementsystem.R;
import com.example.storemanagementsystem.databinding.FragmentAddVendorBinding;
import com.example.storemanagementsystem.DB.TempDB;
import com.example.storemanagementsystem.entites.VendorEntity;
import com.example.storemanagementsystem.models.VendorModel;
import com.example.storemanagementsystem.viewmodels.VendorViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

public class AddVendor extends Fragment {
    private FragmentAddVendorBinding binding;
    private VendorViewModel vendorViewModel;
    private String DoB = "";
    private long rowId = 0;
    private String DocId = null;
    public AddVendor() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddVendorBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        vendorViewModel = new ViewModelProvider(requireActivity()).get(VendorViewModel.class);
        binding.DateID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog pickerDialog =
                        new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                //binding.DatePickerButton.setText(dayOfMonth+"/"+(month+1)+"/"+year);

                                final SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd,yyyy");
                                //DoB = dateFormat.format(new Date()); //today date
                                Date date = new GregorianCalendar(year,month,dayOfMonth).getTime();
                                DoB = dateFormat.format(date);
                                binding.DateID.setText(DoB);
                            }
                        }, 2000, 10, 24);

                pickerDialog.show();
            }
        });

        binding.SaveID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = binding.NameID.getText().toString();
                final String email = binding.EmailID.getText().toString();
                final String phone = binding.PhoneID.getText().toString();
                final String address = binding.AddressID.getText().toString();
                final String date = binding.DateID.getText().toString();
                if(!name.isEmpty() && !email.isEmpty() && !phone.isEmpty() && !address.isEmpty() && !date.isEmpty()){
                    final VendorEntity vendorEntity = new VendorEntity(name,phone,email,address,date);
                    //saveDatabase
                    //if(rowId>0)
                    if(DocId!=null){
                        //vendorEntity.setId(rowId);
                        vendorEntity.setDocId(DocId);
                        vendorViewModel.updateVendor(vendorEntity);
                    }
                    else  vendorViewModel.addVendor(vendorEntity);
                    //SMS_APPS_DB.getDb(getActivity()).getVendorDao().InsertNewVendor(vendorEntity);
                    //TempDB.vendorList.add(vendorEntity);
                    Navigation.findNavController(v).navigate(R.id.action_addVendor_to_vendorList);
                }
            }
        });

        binding.LayoytID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
            }
        });
        final Bundle bundle = getArguments();
        if(bundle!=null){
            binding.SaveID.setText("Update");
            //rowId = bundle.getLong("id");
            DocId = bundle.getString("DocId");
            //vendorViewModel.fetchVendorByID(rowId).observe(getViewLifecycleOwner(), new Observer<VendorEntity>()
             vendorViewModel.fetchVendorByID(DocId).observe(getViewLifecycleOwner(), new Observer<VendorEntity>(){
                @Override
                public void onChanged(VendorEntity vendorEntity) {
                    binding.NameID.setText(vendorEntity.getName());
                    binding.EmailID.setText(vendorEntity.getEmail());
                    binding.PhoneID.setText(vendorEntity.getPhone());
                    binding.AddressID.setText(vendorEntity.getAddress());
                    binding.DateID.setText(vendorEntity.getDate());
                }
            });
        }
    }
}