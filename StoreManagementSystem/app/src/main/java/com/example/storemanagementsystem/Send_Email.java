package com.example.storemanagementsystem;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.storemanagementsystem.databinding.FragmentSendEmailBinding;

public class Send_Email extends Fragment {
    private FragmentSendEmailBinding binding;
    public Send_Email() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       binding = FragmentSendEmailBinding.inflate(inflater);
       return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.SendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ToEmail = binding.ToText.getText().toString();
                String Subject = binding.SubjectText.getText().toString();
                String Message = binding.MessageText.getText().toString();
                Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse("mailto:" + ToEmail));
                //intent.putExtra(Intent.EXTRA_SUBJECT, Subject);
                //intent.putExtra(Intent.EXTRA_TEXT, Message);
                //if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(intent);
                //}
            }
        });
    }
}