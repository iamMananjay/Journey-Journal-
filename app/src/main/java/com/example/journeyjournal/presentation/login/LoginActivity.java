package com.example.journeyjournal.presentation.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.journeyjournal.presentation.DashboardActivity;
import com.example.journeyjournal.R;
import com.example.journeyjournal.presentation.Registration.RegistrationActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

//    Declare A Variable
    private final static String tagName = "LoginActivity";
//    private TextView appName;
    private EditText email_field;
    private EditText password_field;
    private TextView signIn;
    private TextView signUp;
    private TextView ForgotPassword;
    private ProgressBar LoginLoading;
    private FirebaseAuth fAuth;

    /*on_create call back*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.d(tagName, "onCreate is called.... ");
//        Call A Function Inside OnCreate Method
        initViews();
//        assignAppName();
        initSignInAction();
        initSignUpAction();
    }
    /*on_start call back*/
    @Override
    protected void onStart() {
        super.onStart();
//        Debug Message
        Log.d(tagName, "onStart is called.... ");
    }
    /*on_Resume call back*/
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(tagName, "onResume is called.... ");
    }
    /*on_Pause call back*/
    @Override
    protected void onPause() {
        super.onPause();
        Log.d(tagName, "onPause is called.... ");
    }
    /*on_Destroy call back*/
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(tagName, "onDestroy is called.... ");
    }
    /*on_BackPressed call back*/
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    private void initViews(){
//        Initialize Id In global variable
//        appName = findViewById(R.id.tv_app_first_name);
        email_field=findViewById(R.id.et_email_field);
        password_field=findViewById(R.id.et_password_field);
        signIn=findViewById(R.id.sign_btn);
        signUp=findViewById(R.id.signup_btn);
        ForgotPassword=findViewById(R.id.forget_password_btn);
        LoginLoading=findViewById(R.id.Loading);
        fAuth= FirebaseAuth.getInstance();

    }
//    Set The Title Name Of The Application
//    private  void assignAppName(){
//        appName.setText(R.string.firstName);
//    }

//    Add Action On SignIn Button Click
    private  void initSignInAction(){
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Call A Function At Onclick
                onSignInClicked();
                Log.d(tagName, "onClick: singIn clicked");
            }
        });
    }
    private  void onSignInClicked(){
//        On SignIn Click Progress Bar Set Visible
        LoginLoading.setVisibility(View.VISIBLE);
//        Set The Value Of Layout Inside Variable
        String email = email_field.getText().toString().trim();
        String password = password_field.getText().toString().trim();
//        Check The Input Field Is Empty or Not
        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please Entry The Input Field", Toast.LENGTH_SHORT).show();
            return;
        }else {
            fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
//                    Check Whether the Login Data Add Successfully Or Not with Toast Message
                    if(task.isSuccessful()){
                        LoginLoading.setVisibility(View.GONE);
                        Toast.makeText(LoginActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this,DashboardActivity.class);
                        startActivity(intent);
                        finish();
                    }else {
                        //Progress Bar Set Gone
                        LoginLoading.setVisibility(View.GONE);
                        //Toast Message if IsNotSuccessFully
                        Toast.makeText(LoginActivity.this, "Fail To Login", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
//    Add Action On SignUp Button Click
    private  void initSignUpAction(){
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Call A Function At Onclick
                onSignUpClicked();
            }
        });
    }
//    Move the Activity From Login To SiGNIn
    private void onSignUpClicked(){
        Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
        startActivity(intent);
        finish();
    }
}