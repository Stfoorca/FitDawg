package com.example.fitdawg;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class Tab2Fragment extends Fragment{
    private static final String TAG = "Tab1Fragment";

    private Button btnTEST2;
    private Button logoutBtn;
    public ProfileActivity profAct;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.tab2_fragment, container, false);

        profAct = (ProfileActivity) getActivity();

        btnTEST2 = (Button) view.findViewById(R.id.btnTEST2);
        logoutBtn = (Button) view.findViewById(R.id.logout);

        btnTEST2.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "TESTING BUTTON CLICK 2", Toast.LENGTH_SHORT).show();
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(profAct.user != null){
                    profAct.mAuth.signOut();
                    startActivity(new Intent(getActivity(), MainActivity.class));
                    Toast.makeText(getActivity(), "Logout successful!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }



}
