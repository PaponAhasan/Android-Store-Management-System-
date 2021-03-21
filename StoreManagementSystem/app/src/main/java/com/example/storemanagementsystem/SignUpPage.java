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

import com.example.storemanagementsystem.databinding.FragmentSignUpPageBinding;
import com.example.storemanagementsystem.viewmodels.AuthViewModel;

public class SignUpPage extends Fragment {
    private FragmentSignUpPageBinding binding;
    private AuthViewModel authViewModel;
    public SignUpPage() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSignUpPageBinding.inflate(inflater);
        authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.SignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = binding.editTextTextEmailAddress.getText().toString();
                final String password = binding.editTextTextPassword.getText().toString();
                final String CPassword = binding.editTextTextPassword2.getText().toString();
                if(CPassword.equals(password)){
                    authViewModel.registerUser(email,password);
                   // Navigation.findNavController(v).navigate(R.id.action_signUpPage_to_loginPage);
                }else{
                    binding.FaildSigup.setText("Password don't match");
                }
            }
        });
        binding.SignupLayoutID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
            }
        });
        authViewModel.authentication.observe(getViewLifecycleOwner(), new Observer<AuthViewModel.Authentication>() {
            @Override
            public void onChanged(AuthViewModel.Authentication authentication) {
                if(authentication == AuthViewModel.Authentication.AUTHENTICATED){
                    if(authViewModel.VerifiedSendMessage){
                        binding.FaildSigup.setText("Email sent.Please verify your account.");
                    }
                    //Navigation.findNavController(view).navigate(R.id.action_signUpPage_to_loginPage);
                }
            }
        });
        authViewModel.errorMessLiveData.observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                //if(s.isEmpty()){
                    binding.FaildSigup.setText("Email or Password invalid");
               // }
            }
        });
    }
}