package com.example.journeyjournal.presentation.Registration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.journeyjournal.R;
import com.example.journeyjournal.presentation.login.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegistrationActivity extends AppCompatActivity {
    //    Declare A Variable
    private EditText RegistrationName;
    private EditText RegistrationEmail;
    private EditText RegistrationPassword;
    private TextView RegisterUser;
    private ProgressBar RegistrationLoading;
    private FirebaseAuth FAuth;
    private TextView BackToLogin;

    /*on_create call back*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        //Call A Function Inside OnCreate Method
        initViews();
        initSignInAction();
        initSignUpAction();
    }
    private void initViews(){
        //Initialize Id in global variable
        RegistrationName=findViewById(R.id.et_registration_name_field);
        RegistrationEmail=findViewById(R.id.et_registration_email_field);
        RegistrationPassword=findViewById(R.id.et_registration_password_field);
        RegisterUser=findViewById(R.id.registration_sign_btn);
        RegistrationLoading=findViewById(R.id.Loading);
        FAuth=FirebaseAuth.getInstance();
        BackToLogin=findViewById(R.id.registration_signIn_btn);

    }
    //    Add Action On SignIn Button Click
    private void initSignInAction(){
        BackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSignInClicked();
            }
        });
    }
    //    Move the Activity From Register To Login
    private void onSignInClicked(){
        Intent intent = new Intent(RegistrationActivity.this,LoginActivity.class);
        startActivity(intent);
        finish();
    }
    //    Add Action On SignUn Button Click
    private void initSignUpAction(){
        RegisterUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSignUpClicked();
            }
        });
    }

    private void onSignUpClicked(){
        // On SignUp Click Progress Bar Set Visible
        RegistrationLoading.setVisibility(View.VISIBLE);
        //Set The Value Of Layout Inside Variable
        String UserName =  RegistrationName.getText().toString().trim();
        String UserEmail = RegistrationEmail.getText().toString().trim();
        String UserPassword = RegistrationPassword.getText().toString().trim();
       //Check The Input Field Is Empty or Not
        if(TextUtils.isEmpty(UserName) || TextUtils.isEmpty(UserEmail) || TextUtils.isEmpty(UserPassword)){
            Toast.makeText(RegistrationActivity.this,"Please Fill All Input Field",Toast.LENGTH_SHORT).show();

        }else {
            FAuth.createUserWithEmailAndPassword(UserEmail,UserPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful() ){
                        //Check Whether the Register Data Add Successfully Or Not with Toast Message
                        RegistrationLoading.setVisibility(View.GONE);
                        Toast.makeText(RegistrationActivity.this, "User Register Successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegistrationActivity.this,LoginActivity.class);
                        startActivity(intent);
                        finish();

                    }else {
                        //Progress Bar Set Gone
                        RegistrationLoading.setVisibility(View.GONE);
//                        Toast Message if IsNotSuccessFully
                        Toast.makeText(RegistrationActivity.this, "Fail To Register", Toast.LENGTH_SHORT).show();
                        Log.d("RegistrationActivity", "onComplete:  " + task.getException().getMessage());
                    }
                }
            });
        }
    }
}



































