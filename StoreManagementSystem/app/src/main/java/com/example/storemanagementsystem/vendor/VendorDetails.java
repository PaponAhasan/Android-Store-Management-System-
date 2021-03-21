package com.example.storemanagementsystem.vendor;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.storemanagementsystem.MainActivity;
import com.example.storemanagementsystem.R;
import com.example.storemanagementsystem.Send_SMS;
import com.example.storemanagementsystem.databinding.FragmentVendorDetailsBinding;
import com.example.storemanagementsystem.entites.VendorEntity;
import com.example.storemanagementsystem.viewmodels.VendorViewModel;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.net.URI;


public class VendorDetails extends Fragment {
    private FragmentVendorDetailsBinding vendorDetailsBinding;
    private VendorViewModel vendorViewModel;
    private VendorEntity contact;
    private AlertDialog.Builder alertDialog;
    private AlertDialog Dialog;
    private static final int REQUEST_CALL = 111;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final int PICK_IMAGE = 1;

    public VendorDetails() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        vendorDetailsBinding = FragmentVendorDetailsBinding.inflate(inflater);
        vendorViewModel = new ViewModelProvider(requireActivity()).get(VendorViewModel.class);
        return vendorDetailsBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final Bundle bundle = getArguments();
        if(bundle!=null){
            //final long id = bundle.getLong("id");
            final String DocId = bundle.getString("DocId");
            //vendorViewModel.fetchVendorByID(id).observe(getViewLifecycleOwner(), new Observer<VendorEntity>()
             vendorViewModel.fetchVendorByID(DocId).observe(getViewLifecycleOwner(), new Observer<VendorEntity>(){
                @Override
                public void onChanged(VendorEntity vendorEntity) {
                    contact = vendorEntity;
                    vendorDetailsBinding.vendorName.setText(vendorEntity.getName());
                    vendorDetailsBinding.vendorEmail.setText(vendorEntity.getEmail());
                    vendorDetailsBinding.vendorPhone.setText(vendorEntity.getPhone());
                    vendorDetailsBinding.vendorAddress.setText(vendorEntity.getAddress());
                    vendorDetailsBinding.vendorDate.setText(vendorEntity.getDate());
                    if(vendorEntity.getImageDownloadUrl()!=null){
                        Toast.makeText(getContext(), "NOW", Toast.LENGTH_SHORT).show();
                        Picasso.get().load(vendorEntity.getImageDownloadUrl()).into(vendorDetailsBinding.imageView);
                    }
                }
            });
            vendorDetailsBinding.phoneBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String[] permission = {Manifest.permission.CALL_PHONE};
                    boolean b = ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.CALL_PHONE)
                            != PackageManager.PERMISSION_GRANTED;
                    if(b){
                       requestPermissions(permission,REQUEST_CALL);
                    }else{
                        initiateCall();
                    }
                }
            });
            vendorDetailsBinding.emailBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String ToEmail = vendorDetailsBinding.vendorEmail.getText().toString();
                    Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse("mailto:" + ToEmail));
                    startActivity(intent);

                }
            });
            vendorDetailsBinding.smsBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Bundle bundle = new Bundle();
                    String name = vendorDetailsBinding.vendorName.getText().toString();
                    String phone = vendorDetailsBinding.vendorPhone.getText().toString();
                    bundle.putString("name",name);
                    bundle.putString("phone",phone);
                    Navigation.findNavController(v).navigate(R.id.action_vendorDetails_to_send_SMS,bundle);
                }
            });
        }

        vendorDetailsBinding.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                try {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                } catch (ActivityNotFoundException e) {
                    // display error state to the user
                }

                /*Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);*/
            }
        });
    }

    //Image jnno Override Method
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == REQUEST_IMAGE_CAPTURE){
            vendorDetailsBinding.progressBar3.setVisibility(View.VISIBLE);
            if(resultCode == Activity.RESULT_OK){
                Bundle extras = data.getExtras();
                final Bitmap[] imageBitmap = {(Bitmap) extras.get("data")};
                //vendorDetailsBinding.imageView.setImageBitmap(imageBitmap[0]);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                imageBitmap[0].compress(Bitmap.CompressFormat.JPEG, 80, baos);
                byte[] bytes = baos.toByteArray();
                final StorageReference rootRef = FirebaseStorage.getInstance().getReference();
                final StorageReference photoRef = rootRef.child("Vendors_Picture/vendor_"+System.currentTimeMillis());
                UploadTask uploadTask = photoRef.putBytes(bytes);
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(getActivity(), "Image Uploaded", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

                uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }

                        // Continue with the task to get the download URL
                        return photoRef.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downloadUri = task.getResult();
                            vendorDetailsBinding.progressBar3.setVisibility(View.GONE);
                            Toast.makeText(getContext(), "NOW1", Toast.LENGTH_SHORT).show();
                            vendorViewModel.updateVendorImageUri(contact.getDocId(),downloadUri.toString());
                        } else {
                            Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
        }
    }

    private void initiateCall(){
        //Uri phoneUri = Uri.parse("tel:"+contact.getPhone());
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:"+contact.getPhone()));
        if(intent.resolveActivity(getActivity().getPackageManager())!=null){
            startActivity(intent);
        }
    }

    private void MyMessage() {
        String phone = vendorDetailsBinding.vendorPhone.getText().toString();
        if(!phone.isEmpty()){
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phone,null,"Papon",null,null);
            Toast.makeText(getActivity(), "Message SEND", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==REQUEST_CALL){
            if(grantResults.length>=0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                initiateCall();
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