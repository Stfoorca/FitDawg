package com.example.fitdawg;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseUser;

public class ProfileChangeActivity extends AppCompatActivity {
    FirebaseUser mUser;

    private final int GALLERY_REQUEST_CODE = 9165;
    private Button imageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_change_activity);
        imageButton = (Button)findViewById(R.id.chooseImage);


        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }


    
}
