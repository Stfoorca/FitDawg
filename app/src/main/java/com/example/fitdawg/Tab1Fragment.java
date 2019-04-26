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
    List dateList = new ArrayList();
    private List detailsList = new ArrayList();
    ArrayAdapter adapter;
    public ProfileActivity profileActivity;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.tab1_fragment, container, false);
        historyListView = (ListView) view.findViewById(R.id.historyList);
        profileActivity = (ProfileActivity) getActivity();
        adapter = new ArrayAdapter(profileActivity.getApplicationContext(), android.R.layout.simple_list_item_1, detailsList);
        historyListView.setAdapter(adapter);
        return view;
    }


    public void UpdateDataList(List<DataRecord> dataRecords){
        detailsList.clear();
        for(int i=0;i<dataRecords.size();i++){
            if(dataRecords.get(i)!=null) {
                detailsList.add("Date" + dataRecords.get(i).date + "Weight " + dataRecords.get(i).weight.toString() + " | Arm " + dataRecords.get(i).arm.toString() + "| Waist" + dataRecords.get(i).waist.toString());
            }
        }
        adapter = new ArrayAdapter(profileActivity.getApplicationContext(), android.R.layout.simple_list_item_1, detailsList);
        historyListView.setAdapter(adapter);
    }


}
