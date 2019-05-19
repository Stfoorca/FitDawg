package com.example.fitdawg;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfileChangeActivity extends AppCompatActivity {
    private DatabaseReference mDatabase, newUser;
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private EditText username;
    private EditText height;
    private Spinner gender_spinner;
    private Spinner year_spinner;
    private Button saveButton;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_change_activity);

        username = (EditText) findViewById(R.id.signup_name_input);
        height = (EditText)findViewById(R.id.height);
        gender_spinner = (Spinner)findViewById(R.id.gender_spinner);
        year_spinner = (Spinner)findViewById(R.id.year_spinner);
        saveButton = findViewById(R.id.save_button);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        final ArrayAdapter<CharSequence> staticAdapter = ArrayAdapter.createFromResource(this, R.array.genders, android.R.layout.simple_spinner_item);
        final ArrayAdapter<String> yearAdapter;
        List<String> yearList = new ArrayList<String>();
        for(int i=year;i>=year-120;i--){
            yearList.add(Integer.toString(i));
        }
        yearAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, yearList);

        staticAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);



        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentUser = dataSnapshot.getValue(User.class);
                username.setText(currentUser.name);

                gender_spinner.setAdapter(staticAdapter);
                gender_spinner.setSelection(staticAdapter.getPosition(currentUser.gender));

                year_spinner.setAdapter(yearAdapter);
                year_spinner.setSelection(yearAdapter.getPosition(currentUser.year));

                height.setText(currentUser.height);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeUserProfile();
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
            }
        });
    }

    void ChangeUserProfile(){
        User newUser = new User(username.getText().toString(), currentUser.email, gender_spinner.getSelectedItem().toString(), year_spinner.getSelectedItem().toString(), height.getText().toString());
        Map<String, Object> updates = new HashMap<>();
        updates.put("name", newUser.name);
        updates.put("height", newUser.height);
        updates.put("year", newUser.year);
        updates.put("gender", newUser.gender);
        mDatabase.updateChildren(updates);
        Toast.makeText(getBaseContext(), "Profile edited successfully!",
                Toast.LENGTH_SHORT).show();

    }
    
}
