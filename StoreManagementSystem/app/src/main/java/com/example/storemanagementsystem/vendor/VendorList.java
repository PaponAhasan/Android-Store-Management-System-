package com.example.storemanagementsystem.vendor;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.storemanagementsystem.R;
import com.example.storemanagementsystem.adapter.VendorAdapter;
import com.example.storemanagementsystem.databinding.FragmentVendorListBinding;
import com.example.storemanagementsystem.DB.TempDB;
import com.example.storemanagementsystem.entites.VendorEntity;
import com.example.storemanagementsystem.viewmodels.AuthViewModel;
import com.example.storemanagementsystem.viewmodels.VendorViewModel;

import java.util.List;

public class VendorList extends Fragment {
    private FragmentVendorListBinding binding;
    private VendorViewModel vendorViewModel;
    private VendorAdapter vendorAdapter;
    private AuthViewModel authViewModel;
    private String FUEmail;
    public VendorList() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.tool_bar,menu);
        final SearchView searchView = (SearchView) menu.findItem(R.id.vendors_search).getActionView();
        searchView.setQueryHint("Search Name");
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                vendorAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       binding = FragmentVendorListBinding.inflate(inflater);
       return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        vendorViewModel = new ViewModelProvider(requireActivity()).get(VendorViewModel.class);
        //vendorAdapter = new VendorAdapter(getContext(), TempDB.vendorList);
        //final LinearLayoutManager LLM = new LinearLayoutManager(getActivity());
        //binding.vendorListID.setLayoutManager(LLM);
        //binding.vendorListID.setAdapter(vendorAdapter);
        binding.fabID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_vendorList_to_addVendor);
            }
        });
        vendorViewModel.fetchAllVendors().observe(getViewLifecycleOwner(), new Observer<List<VendorEntity>>() {
            @Override
            public void onChanged(List<VendorEntity> vendorEntities) {
                vendorAdapter = new VendorAdapter(getContext(),vendorEntities,vendorViewModel);
                final LinearLayoutManager LLM = new LinearLayoutManager(getContext());
                binding.vendorListID.setLayoutManager(LLM);
                binding.vendorListID.setAdapter(vendorAdapter);
            }
        });
    }
}