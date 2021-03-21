package com.example.storemanagementsystem.viewmodels;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.storemanagementsystem.entites.VendorEntity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthViewModel extends AndroidViewModel {
    public MutableLiveData<Authentication> authentication = new MutableLiveData<>();
    private FirebaseAuth auth;
    public FirebaseUser currentUser;
    public boolean VerifiedSendMessage = false;
    public MutableLiveData<Boolean>isLoadingLiveData = new MutableLiveData<>();
    public MutableLiveData<String>errorMessLiveData = new MutableLiveData<>();
    public AuthViewModel(@NonNull Application application) {
        super(application);
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        isLoadingLiveData.postValue(false);
        if(currentUser!=null)authentication.postValue(Authentication.AUTHENTICATED);
        else authentication.postValue(Authentication.UNAUTHENTICATED);
    }

    public void registerUser(String email,String password){
        isLoadingLiveData.postValue(true);
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    currentUser = auth.getCurrentUser();
                    authentication.postValue(Authentication.AUTHENTICATED);
                    currentUser.sendEmailVerification()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        VerifiedSendMessage = true;
                                        //Toast.makeText(getApplication(), "Email sent.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                    isLoadingLiveData.postValue(false);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                isLoadingLiveData.postValue(false);
                errorMessLiveData.postValue(e.getLocalizedMessage());
            }
        });
    }

    public void LoginUser(String email,String password){
        isLoadingLiveData.postValue(true);
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    currentUser = auth.getCurrentUser();
                    authentication.postValue(Authentication.AUTHENTICATED);
                    isLoadingLiveData.postValue(false);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                isLoadingLiveData.postValue(false);
                errorMessLiveData.postValue(e.getLocalizedMessage());
            }
        });
    }

    public  void Logout(){
        if(currentUser!=null){
            auth.signOut();
            authentication.postValue(Authentication.UNAUTHENTICATED);
        }
    }

    public enum Authentication{
        AUTHENTICATED,UNAUTHENTICATED
    }
}
