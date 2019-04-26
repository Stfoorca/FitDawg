package com.example.fitdawg;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

public class Tab2Fragment extends Fragment{
    private static final String TAG = "Tab2Fragment";

    private Button logoutBtn, addButton;
    private TextView tab2username, tab2email, tab2gender, tab2age, tab2height, tab2bmi, tab2weight;
    private ImageView tab2profileImage;
    public ProfileActivity profileActivity;
    private FirebaseUser mUser;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.tab2_fragment, container, false);

        profileActivity = (ProfileActivity) getActivity();

        tab2username = (TextView)view.findViewById(R.id.username);
        tab2email = (TextView) view.findViewById(R.id.email);
        tab2gender = (TextView)view.findViewById(R.id.gender);
        tab2age = (TextView)view.findViewById(R.id.age);
        tab2profileImage = (ImageView)view.findViewById(R.id.profileImage);
        tab2height = (TextView)view.findViewById(R.id.height);
        tab2bmi = (TextView)view.findViewById(R.id.bmi);
        tab2weight = view.findViewById(R.id.weight);

        mUser = profileActivity.mAuth.getCurrentUser();

        addButton = (Button) view.findViewById(R.id.addButton);
        logoutBtn = (Button) view.findViewById(R.id.logout);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getActivity(), AddActivity.class));
            }
        });

        return view;
    }


    public void UpdateUserProfile(User user){

        tab2username.setText(user.name);
        tab2email.setText("Email:   " + user.email);
        tab2gender.setText("Gender:     " + user.gender);
        tab2age.setText("Age:   " + Integer.toString(Calendar.getInstance().get(Calendar.YEAR) - Integer.parseInt(user.year)));
        tab2height.setText("Height:     " + user.height);
        tab2weight.setText("Weight:     " + user.weight);
        if(user.weight!="0")
            tab2bmi.setText("BMI:   " + (Float.parseFloat(user.weight)/(Float.parseFloat(user.height)*Float.parseFloat(user.height))));
        else tab2bmi.setText("Brak pomiaru wagi");
        Picasso.get().load(mUser.getPhotoUrl()).into(tab2profileImage);
    }
}
