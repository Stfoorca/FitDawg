package com.example.fitdawg;

import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG ="ProfileActivity";

    private SectionsPageAdapter mSectionsPageAdapter;

    private ViewPager mViewPager;

    public FirebaseAuth mAuth;
    public FirebaseUser user;
    public DatabaseReference mDatabase, mDatabaseUserData;
    public User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentUser = dataSnapshot.getValue(User.class);
                ((Tab2Fragment)((SectionsPageAdapter)mViewPager.getAdapter()).getItem(1)).UpdateUserProfile(currentUser);

                Log.d(TAG, "Value is: " + currentUser.email);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        });

        mDatabaseUserData = mDatabase.child("data");

        mDatabaseUserData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ProcessDataFromDB((Map<String, Object>)dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        setContentView(R.layout.profile_activity);
        Log.d(TAG, "onCreate: Starting.");

        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        tabLayout.setupWithViewPager(mViewPager);
    }

    private void setupViewPager(ViewPager viewPager){
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new Tab1Fragment(), "History");
        adapter.addFragment(new Tab2Fragment(), "Profile");
        adapter.addFragment(new Tab3Fragment(), "Charts");

        viewPager.setAdapter(adapter);
    }

    private void ProcessDataFromDB(Map<String, Object> data){
        if(data==null)
            return;
        List<DataRecord> records = new ArrayList();

        for(Map.Entry<String, Object> entry : data.entrySet()){
            Map singleData = (Map)entry.getValue();

            records.add(new DataRecord(entry.getKey() ,Double.parseDouble(singleData.get("arm").toString()),  Double.parseDouble(singleData.get("waist").toString()),  Double.parseDouble(singleData.get("weight").toString())));
        }

        if(records.size() >0) {
            ((Tab1Fragment) ((SectionsPageAdapter) mViewPager.getAdapter()).getItem(0)).UpdateDataList(records);
        }
    }

}