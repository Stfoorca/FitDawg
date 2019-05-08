package com.example.fitdawg;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Tab1Fragment extends Fragment{
    private static final String TAG = "Tab1Fragment";
    private ListView historyListView;
    private ArrayList<Person> detailsList = new ArrayList<>();
    PersonListAdapter adapter;
    public ProfileActivity profileActivity;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.tab1_fragment, container, false);
        historyListView = (ListView) view.findViewById(R.id.historyList);
        profileActivity = (ProfileActivity) getActivity();
        adapter = new PersonListAdapter(profileActivity.getApplicationContext(), R.layout.adapter_view, detailsList);
        historyListView.setAdapter(adapter);
        return view;
    }


    public void UpdateDataList(List<DataRecord> dataRecords){
        detailsList.clear();
        for(int i=0;i<dataRecords.size();i++){
            if(dataRecords.get(i)!=null) {
                detailsList.add(new Person(dataRecords.get(i).date,dataRecords.get(i).weight.toString(),dataRecords.get(i).arm.toString(),dataRecords.get(i).waist.toString()));
            }
        }
        adapter = new PersonListAdapter(profileActivity.getApplicationContext(), R.layout.adapter_view, detailsList);
        historyListView.setAdapter(adapter);
    }


}
