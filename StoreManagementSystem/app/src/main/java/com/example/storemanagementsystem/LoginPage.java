package com.example.storemanagementsystem;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.storemanagementsystem.databinding.FragmentLoginPageBinding;
import com.example.storemanagementsystem.viewmodels.AuthViewModel;

public class LoginPage extends Fragment {
    private FragmentLoginPageBinding binding;
    private AuthViewModel authViewModel;
    public LoginPage() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginPageBinding.inflate(inflater);
        authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
        binding.FaildLogin.setText("");
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = binding.editTextTextEmailAddress.getText().toString();
                final String password = binding.editTextTextPassword.getText().toString();
                if(!email.isEmpty() && !password.isEmpty()) authViewModel.LoginUser(email,password);
                else{
                    binding.FaildLogin.setText("Please enter email or password.");
                }
                //Navigation.findNavController(v).navigate(R.id.action_loginPage_to_dashboard);
            }
        });
        binding.LoginLayoutID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
            }
        });
        binding.Signuptxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_loginPage_to_signUpPage);
            }
        });
        authViewModel.errorMessLiveData.observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if(!s.isEmpty() && binding.FaildLogin != null){
                    binding.FaildLogin.setText("Email or Password invalid");
                }
            }
        });
        authViewModel.isLoadingLiveData.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean status) {
                if(binding.ProgressBar != null){
                    if(status) binding.ProgressBar.setVisibility(View.VISIBLE);
                    else binding.ProgressBar.setVisibility(View.GONE);
                }
            }
        });
        authViewModel.authentication.observe(getViewLifecycleOwner(), new Observer<AuthViewModel.Authentication>() {
            @Override
            public void onChanged(AuthViewModel.Authentication authentication) {
                if(authViewModel.currentUser !=null && authViewModel.currentUser.isEmailVerified()) {
                    binding.FaildLogin.setText("");
                    if (authentication == AuthViewModel.Authentication.AUTHENTICATED) {
                       // Navigation.findNavController(view).navigate(R.id.action_loginPage_to_store_Dashboard);
                        Navigation.findNavController(view).navigate(R.id.action_loginPage_to_dashboard);
                    }
                }else if(authViewModel.currentUser !=null && !authViewModel.currentUser.isEmailVerified()){
                    binding.FaildLogin.setText("Your email is not verified yet.Please verify it");
                    //Toast.makeText(getContext(), "Your email is not verified yet.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        binding.editTextTextEmailAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.FaildLogin != null)
                binding.FaildLogin.setText("");
            }
        });
        binding.editTextTextPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.FaildLogin != null)
                binding.FaildLogin.setText("");
            }
        });
    }
}