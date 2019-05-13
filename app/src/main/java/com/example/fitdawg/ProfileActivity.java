package com.example.fitdawg;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";

    private SectionsPageAdapter mSectionsPageAdapter;

    private ViewPager mViewPager;
    private Button logoutBtn;

    public List<DataRecord> records = new ArrayList();
    public FirebaseAuth mAuth;
    public FirebaseUser user;
    public DatabaseReference mDatabase, mDatabaseUserData;
    public User currentUser;

    public static ProfileActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());

        mDatabaseUserData = mDatabase.child("data");

        mDatabaseUserData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ProcessDataFromDB((Map<String, Object>) dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentUser = dataSnapshot.getValue(User.class);
                if(records.size()>0) {
                    currentUser.weight = records.get(records.size()-1).weight.toString();
                }
                else{
                    currentUser.weight = "0";
                }
                ((Tab2Fragment) ((SectionsPageAdapter) mViewPager.getAdapter()).getItem(1)).UpdateUserProfile(currentUser);

                Log.d(TAG, "Value is: " + currentUser.email);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.", databaseError.toException());
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

    private void setupViewPager(ViewPager viewPager) {
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new Tab1Fragment(), "History");
        adapter.addFragment(new Tab2Fragment(), "Profile");
        adapter.addFragment(new Tab3Fragment(), "Charts");

        viewPager.setAdapter(adapter);
    }

    private void ProcessDataFromDB(Map<String, Object> data) {
        if (data == null)
            return;
        records = new ArrayList();

        for (Map.Entry<String, Object> entry : data.entrySet()) {
            Map singleData = (Map) entry.getValue();

            records.add(new DataRecord(entry.getKey(), Double.parseDouble(singleData.get("arm").toString()), Double.parseDouble(singleData.get("waist").toString()), Double.parseDouble(singleData.get("weight").toString())));
        }

        if (records.size() > 0) {
            Collections.sort(records, new Comparator<DataRecord>() {
                @Override
                public int compare(DataRecord o1, DataRecord o2) {
                    return o2.getDate().compareTo(o1.getDate());
                }
            });
            ((Tab1Fragment) ((SectionsPageAdapter) mViewPager.getAdapter()).getItem(0)).UpdateDataList(records);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                if (user != null) {

                    Context ctx = getApplicationContext();
                    UtilsClipCodes.saveSharedSetting(ctx, "LOADED", false);
                    mAuth.signOut();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));

                    Toast.makeText(ProfileActivity.this, "Logout successful!", Toast.LENGTH_SHORT).show();

                    finish();
                }
                return true;
            case R.id.about:
                Toast.makeText(ProfileActivity.this, "FitDawg 2019\n\nDamian Szkudlarek\nPaweł Przybyłowski\nBartosz Ptak", Toast.LENGTH_LONG).show();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void UpdateProfileImage(FirebaseUser mUser, Uri imageURI){
        Picasso.get().load(imageURI).into(((Tab2Fragment) ((SectionsPageAdapter) mViewPager.getAdapter()).getItem(1)).tab2profileImage);

    }

}