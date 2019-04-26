package com.example.fitdawg;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.android.gms.ads.MobileAds;

public class MainActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private Button button;
    private Button button_register;
    private DatabaseReference mFirebaseDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MobileAds.initialize(this, "ca-app-pub-9970869944453043~8418312543");

        email = (EditText)findViewById(R.id.login_email_input);
        password = (EditText)findViewById(R.id.login_password_input);
        mAuth = FirebaseAuth.getInstance();
//        currentUser = mAuth.getCurrentUser();
        button = (Button)findViewById(R.id.login);
        button_register = (Button)findViewById(R.id.register);
        mFirebaseDatabase = FirebaseDatabase.getInstance().getReference().child("users");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == button){
                    LoginUser();
                }
            }
        });
        button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == button_register){
                    startActivity(new Intent(getApplicationContext(),
                            RegisterActivity.class));
                }
            }
        });



    }
    public void LoginUser(){
        String Email = email.getText().toString().trim();
        String Password = password.getText().toString().trim();
        mAuth.signInWithEmailAndPassword(Email, Password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            currentUser = mAuth.getCurrentUser();
                            finish();

                            startActivity(new Intent(getApplicationContext(),
                                    ProfileActivity.class));
                        }else {
                            Toast.makeText(MainActivity.this, "couldn't activity_main",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


}