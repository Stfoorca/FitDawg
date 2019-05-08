package com.example.fitdawg;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class PersonListAdapter extends ArrayAdapter<Person> {

    private Context nContext;
    private int nResource;


    public PersonListAdapter(Context context, int resource, ArrayList<Person> objects) {
        super(context, resource, objects);
        nContext = context;
        nResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @Nullable ViewGroup parent) {
        String date = getItem(position).getDate();
        String weight = getItem(position).getWeight();
        String arm = getItem(position).getArm();
        String waist = getItem(position).getWaist();


        Person person = new Person(date,weight,arm, waist);

        LayoutInflater inflater = LayoutInflater.from(nContext);
        convertView = inflater.inflate(nResource, parent, false);

        TextView TVdate = (TextView) convertView.findViewById(R.id.adapterDate);
        TextView TVweight = (TextView) convertView.findViewById(R.id.adapterWeight);
        TextView TVarm = (TextView) convertView.findViewById(R.id.adapterArm);
        TextView TVwaist = (TextView) convertView.findViewById(R.id.adapterWaist);


        TVdate.setText(date);
        TVweight.setText(weight);
        TVarm.setText(arm);
        TVwaist.setText(waist);

        return convertView;

    }
}
