package com.example.fitdawg;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;


public class AddActivity extends AppCompatActivity {

    public FirebaseAuth mAuth;
    private DatabaseReference mDatabase, newUser;
    private FirebaseUser mUser;
    private Button randomButton;
    private EditText armText, waistText, weightText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_activity);
        final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        final Date date = new Date();
        randomButton = (Button) findViewById(R.id.randomButton);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(mUser.getUid()).child("data");

        armText = findViewById(R.id.arm);
        waistText = findViewById(R.id.waist);
        weightText = findViewById(R.id.weight);


        randomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Double arm = Double.parseDouble(armText.getText().toString());
                Double waist = Double.parseDouble(waistText.getText().toString());
                Double weight = Double.parseDouble(weightText.getText().toString());

                AddDataRecordToDatabase(dateFormat.format(date), arm, waist, weight);

                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
            }
        });

    }

    private void AddDataRecordToDatabase(String date, Double arm, Double waist, Double weight){
        DataRecord dataRecord = new DataRecord(date, arm, waist, weight);
        mDatabase.child(date).setValue(dataRecord);
    }
}