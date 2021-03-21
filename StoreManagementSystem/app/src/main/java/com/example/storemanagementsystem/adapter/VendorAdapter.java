package com.example.storemanagementsystem.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.storemanagementsystem.DB.SMS_APPS_DB;
import com.example.storemanagementsystem.R;
import com.example.storemanagementsystem.entites.VendorEntity;
import com.example.storemanagementsystem.models.VendorModel;
import com.example.storemanagementsystem.vendor.VendorDetails;
import com.example.storemanagementsystem.viewmodels.VendorViewModel;

import java.util.ArrayList;
import java.util.List;

public class VendorAdapter extends RecyclerView.Adapter<VendorAdapter.VendorViewHolder> implements Filterable {
    private final  Context context;
    private final  List<VendorEntity> vendorEntities;
    private List<VendorEntity> filterVendorEntities;
    private final  VendorViewModel vendorViewModel;
    private VendorDetails vendorDetails;

    public VendorAdapter(Context context, List<VendorEntity> vendorEntities,VendorViewModel vendorViewModel) {
        this.context = context;
        this.vendorEntities = vendorEntities;
        this.vendorViewModel = vendorViewModel;
        this.filterVendorEntities = vendorEntities;
    }

    @NonNull
    @Override
    public VendorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vendorView = LayoutInflater.from(context)
                .inflate(R.layout.vendor_row,parent,false);
        return new VendorViewHolder(vendorView);
    }

    @Override
    public void onBindViewHolder(@NonNull VendorViewHolder holder, int position) {
        holder.nameVR.setText(filterVendorEntities.get(position).getName());
        holder.phoneVR.setText(filterVendorEntities.get(position).getPhone());
        holder.emailVR.setText(filterVendorEntities.get(position).getEmail());
        holder.addressVR.setText(filterVendorEntities.get(position).getAddress());
        holder.dateVR.setText(filterVendorEntities.get(position).getDate());
        //holder.imageVR.setImageURI(filterVendorEntities.get(position).getImageDownloadUrl());

        holder.menuVR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, "Coming soon!!", Toast.LENGTH_SHORT).show();
                showPopUpMenu(position,holder.menuVR);
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, vendorModels.get(position).getName(), Toast.LENGTH_SHORT).show();
                final Bundle bundle = new Bundle();
                bundle.putSerializable("Vendor",filterVendorEntities.get(position));
                //Navigation.findNavController(v).navigate(R.id.vendorHistory,bundle);
               // Toast.makeText(context, "HI", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showPopUpMenu(int position, ImageButton menuVR) {
        final PopupMenu popupMenu = new PopupMenu(context,menuVR);
        popupMenu.getMenuInflater().inflate(R.menu.vendor_row_menu,popupMenu.getMenu());
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.item_details:
                        final Bundle bundle = new Bundle();
                        //bundle.putLong("id",filterVendorEntities.get(position).getId());
                        bundle.putString("DocId",filterVendorEntities.get(position).getDocId());
                         Navigation.findNavController(menuVR)
                                .navigate(R.id.action_vendorList_to_vendorDetails,bundle);
                        break;
                    case R.id.item_edit:
                        final Bundle bundleEdit = new Bundle();
                       // bundleEdit.putLong("id",filterVendorEntities.get(position).getId());
                        bundleEdit.putString("DocId",filterVendorEntities.get(position).getDocId());
                        Navigation.findNavController(menuVR)
                                .navigate(R.id.action_vendorList_to_addVendor,bundleEdit);
                        break;
                    case R.id.item_delete:
                       showDeleteConfirmationDialog(position);
                        break;
                }
                return false;
            }
        });
    }

    private void showDeleteConfirmationDialog(int position) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setIcon(R.drawable.ic_baseline_delete_24);
        builder.setTitle("Confirm Delete");
        builder.setMessage("Are you sure to delete this contact?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final VendorEntity vendorEntity = vendorEntities.get(position);
                vendorViewModel.deleteVendor(vendorEntity);
            }
        });
        builder.setNegativeButton("Cancel",null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public int getItemCount() {
        return filterVendorEntities.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                if(constraint==null || constraint.length()==0){
                    filterVendorEntities = vendorEntities;
                }else {
                    String query = constraint.toString().toLowerCase().trim();
                    List<VendorEntity>tempList = new ArrayList<>();
                    for(VendorEntity vendor: vendorEntities){
                        if(vendor.getName().toLowerCase().contains(query) ||
                        vendor.getPhone().startsWith(query)){
                            tempList.add(vendor);
                        }
                    }
                    filterVendorEntities = tempList;
                }
                FilterResults results = new FilterResults();
                results.values = filterVendorEntities;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filterVendorEntities = (List<VendorEntity>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    static class VendorViewHolder extends RecyclerView.ViewHolder{
        TextView nameVR,phoneVR,emailVR,addressVR,dateVR;
        ImageView imageVR;
        ImageButton menuVR;
        public VendorViewHolder(@NonNull View itemView) {
            super(itemView);
            nameVR = itemView.findViewById(R.id.nameID);
            phoneVR = itemView.findViewById(R.id.phoneID);
            emailVR = itemView.findViewById(R.id.emailID);
            addressVR = itemView.findViewById(R.id.addressID);
            dateVR = itemView.findViewById(R.id.dateID);
            menuVR = itemView.findViewById(R.id.EditMenuID);
            imageVR = itemView.findViewById(R.id.imageID);
        }
    }
}
