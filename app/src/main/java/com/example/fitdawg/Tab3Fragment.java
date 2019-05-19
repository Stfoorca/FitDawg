package com.example.fitdawg;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.ml.custom.FirebaseModelDataType;
import com.google.firebase.ml.custom.FirebaseModelInputOutputOptions;
import com.google.firebase.ml.custom.FirebaseModelInputs;
import com.google.firebase.ml.custom.FirebaseModelInterpreter;
import com.google.firebase.ml.custom.FirebaseModelManager;
import com.google.firebase.ml.custom.FirebaseModelOptions;
import com.google.firebase.ml.custom.FirebaseModelOutputs;
import com.google.firebase.ml.custom.model.FirebaseCloudModelSource;
import com.google.firebase.ml.custom.model.FirebaseLocalModelSource;
import com.google.firebase.ml.custom.model.FirebaseModelDownloadConditions;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.security.auth.callback.Callback;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.view.LineChartView;

public class Tab3Fragment extends Fragment{
    private static final String TAG = "Tab3Fragment";

    private AdView mAdView;
    public ProfileActivity profileActivity;
    private View view;
    public Button weekButton, monthButton, yearButton, allButton;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        view = inflater.inflate(R.layout.tab3_fragment, container, false);

        profileActivity = (ProfileActivity) getActivity();
        weekButton = view.findViewById(R.id.week);
        monthButton = view.findViewById(R.id.month);
        yearButton = view.findViewById(R.id.year);
        allButton = view.findViewById(R.id.all);

        CreateChart(true);

        mAdView = view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        return view;
    }



    public void CreateChart(boolean predict){
        LineChartView lineChartView = view.findViewById(R.id.chart);

        if(profileActivity.records==null) {
            lineChartView.setVisibility(View.INVISIBLE);
            return;
        }
        lineChartView.setVisibility(View.VISIBLE);
        List<Double> yAxisDataArm = new ArrayList<Double>();
        List<Double> yAxisDataWaist = new ArrayList<Double>();
        List<Double> yAxisDataWeight = new ArrayList<Double>();
        List<String> xAxisData = new ArrayList<>();
        for(int i=profileActivity.records.size()-1;i>=0;i--){
            yAxisDataArm.add(profileActivity.records.get(i).arm);
            yAxisDataWaist.add(profileActivity.records.get(i).waist);
            yAxisDataWeight.add(profileActivity.records.get(i).weight);
            xAxisData.add(profileActivity.records.get(i).date);
        }
        if(profileActivity.predicted!=null && predict){
            yAxisDataArm.add((double)profileActivity.predicted[1]);
            yAxisDataWaist.add((double)profileActivity.predicted[2]);
            yAxisDataWeight.add((double)profileActivity.predicted[0]);
            xAxisData.add("predicted");
            //Toast.makeText(profileActivity, "Successfully predicted!", Toast.LENGTH_LONG).show();
        }
        else if(profileActivity.predicted == null && predict){
            //Toast.makeText(profileActivity, "Couldn't predict value! Make sure to save at least 4 measurements!", Toast.LENGTH_LONG).show();
        }



        List xAxisValues = new ArrayList();
        List yAxisValuesArm = new ArrayList();
        List yAxisValuesWaist = new ArrayList();
        List yAxisValuesWeight = new ArrayList();


        for(int i=0;i<xAxisData.size(); i++){
            xAxisValues.add(i, new AxisValue(i).setLabel(xAxisData.get(i)));
        }
        for(int i=0;i<yAxisDataArm.size(); i++){
            yAxisValuesArm.add(new PointValue(i, Float.parseFloat(yAxisDataArm.get(i).toString())));
            yAxisValuesWaist.add(new PointValue(i, Float.parseFloat(yAxisDataWaist.get(i).toString())));
            yAxisValuesWeight.add(new PointValue(i, Float.parseFloat(yAxisDataWeight.get(i).toString())));
        }

        Line lineArm = new Line(yAxisValuesArm).setColor(Color.parseColor("#9C27B0"));
        Line lineWaist = new Line(yAxisValuesWaist).setColor(Color.parseColor("#0000FF"));
        Line lineWeight = new Line(yAxisValuesWeight).setColor(Color.parseColor("#00FF00"));
        lineWeight.setHasLabels(true);
        lineArm.setHasLabels(true);
        lineWaist.setHasLabels(true);

        List lines = new ArrayList();
        lines.add(lineArm);
        lines.add(lineWaist);
        lines.add(lineWeight);

        LineChartData data = new LineChartData();
        data.setLines(lines);
        lineChartView.setLineChartData(data);


        Axis yAxis = new Axis();
        yAxis.setTextColor(Color.parseColor("#03A9F4"));
        yAxis.setTextSize(16);
        data.setAxisYLeft(yAxis);

        List<AxisValue> axisX = new ArrayList<>();
        for(int i=0;i<xAxisData.size();i++){
            axisX.add(new AxisValue(i).setLabel(xAxisData.get(i)));
        }

        Axis xAxis = new Axis(axisX);
        xAxis.setHasTiltedLabels(true);
        xAxis.setTextColor(Color.parseColor("#03A9F4"));
        xAxis.setTextSize(10);
        data.setAxisXBottom(xAxis);

    }
}
