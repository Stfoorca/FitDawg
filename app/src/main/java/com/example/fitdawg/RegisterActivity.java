package com.example.fitdawg;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText username;
    private EditText password;
    private EditText email;
    private EditText height;
    private Button button_register;
    private Button button_login;
    private Spinner gender_spinner;
    private Spinner year_spinner;

    private DatabaseReference mDatabase, newUser;
    private FirebaseUser mUser;
    private UserProfileChangeRequest.Builder mUpcr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        username = (EditText) findViewById(R.id.signup_name_input);
        email = (EditText) findViewById(R.id.signup_email_input);
        password =(EditText) findViewById(R.id.signup_password_input);
        height = (EditText)findViewById(R.id.height);
        button_register = (Button)findViewById(R.id.button_register);
        gender_spinner = (Spinner)findViewById(R.id.gender_spinner);
        year_spinner = (Spinner)findViewById(R.id.year_spinner);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        ArrayAdapter<CharSequence> staticAdapter = ArrayAdapter.createFromResource(this, R.array.genders, android.R.layout.simple_spinner_item);
        ArrayAdapter<String> yearAdapter;
        List<String> yearList = new ArrayList<String>();
        for(int i=year;i>=year-120;i--){
            yearList.add(Integer.toString(i));
        }
        yearAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, yearList);

        staticAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gender_spinner.setAdapter(staticAdapter);
        year_spinner.setAdapter(yearAdapter);

        button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == button_register){
                    RegisterUser();
                }
            }
        });
    }
    private void AddNewUser(String userId, String username, String email, String sex, String bornYear, String height){
        User user = new User(username, email, sex, bornYear, height);
        mDatabase.child(userId).setValue(user);
    }

    public void RegisterUser(){
        String Email = email.getText().toString().trim();
        String Password = password.getText().toString().trim();
        if (TextUtils.isEmpty(Email)){
            Toast.makeText(this, "A Field is Empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(Password)){
            Toast.makeText(this, "A Field is Empty", Toast.LENGTH_SHORT).show();
            return;
        }
        mAuth.createUserWithEmailAndPassword(Email, Password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        try {
                            //check if successful
                            if (task.isSuccessful()) {
                                //User is successfully registered and logged in
                                //start Profile Activity here
                                Toast.makeText(RegisterActivity.this, "Registration successful",
                                        Toast.LENGTH_SHORT).show();
                                mUser = task.getResult().getUser();
                                newUser = mDatabase.child(mUser.getUid());

                                String gender = gender_spinner.getSelectedItem().toString();
                                //CreateUserProfileImage(mUser);
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setPhotoUri(Uri.parse("https://ocdn.eu/images/zapytaj/NWI7MDMsMCwxMmMsMCwxOzAzLDEyYywwLDAsMQ__/bd1db4a251df98795d3bb4c176248237.jpeg")).build();
                                mUser.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(RegisterActivity.this, "Profile image set successfully!",
                                                    Toast.LENGTH_SHORT).show();
                                        }

                                    }

                                });

                                AddNewUser(mUser.getUid(), username.getText().toString(), email.getText().toString().trim(), gender, year_spinner.getSelectedItem().toString(), height.getText().toString());
                                finish();

                                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                            }else{
                                Toast.makeText(RegisterActivity.this, "Couldn't register, try again",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void CreateUserProfileImage(FirebaseUser tempUser){
        Uri uri = Uri.parse("https://ocdn.eu/images/zapytaj/NWI7MDMsMCwxMmMsMCwxOzAzLDEyYywwLDAsMQ__/bd1db4a251df98795d3bb4c176248237.jpeg");
        mUpcr.setPhotoUri(uri);
        tempUser.updateProfile(mUpcr.build());
    }
}