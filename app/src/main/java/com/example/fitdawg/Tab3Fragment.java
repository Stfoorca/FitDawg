package com.example.fitdawg;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.view.LineChartView;

public class Tab3Fragment extends Fragment{
    private static final String TAG = "Tab3Fragment";

    private Button btnTEST3;
    public ProfileActivity profileActivity;
    private View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        view = inflater.inflate(R.layout.tab3_fragment, container, false);
        btnTEST3 = (Button) view.findViewById(R.id.btnTEST3);
        profileActivity = (ProfileActivity) getActivity();

        CreateChart();
        btnTEST3.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "TESTING BUTTON CLICK 3", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    public void CreateChart(){
        if(profileActivity.records==null) {
            System.out.println(profileActivity.records);
            return;
        }
        LineChartView lineChartView = view.findViewById(R.id.chart);
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

        //String[] xAxisData = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
        //Float[] yAxisData= {50.0f, 56.0f, 61.0f, 67.0f, 72.0f, 79.0f, 73.0f, 65.0f, 58.0f, 55.0f};

        List xAxisValues = new ArrayList();
        List yAxisValuesArm = new ArrayList();
        List yAxisValuesWaist = new ArrayList();
        List yAxisValuesWeight = new ArrayList();

        Line lineArm = new Line(yAxisValuesArm).setColor(Color.parseColor("#9C27B0"));
        Line lineWaist = new Line(yAxisValuesWaist).setColor(Color.parseColor("#0000FF"));
        Line lineWeight = new Line(yAxisValuesWeight).setColor(Color.parseColor("#00FF00"));


        for(int i=0;i<xAxisData.size(); i++){
            xAxisValues.add(i, new AxisValue(i).setLabel(xAxisData.get(i)));
        }
        for(int i=0;i<yAxisDataArm.size(); i++){
            yAxisValuesArm.add(new PointValue(i, Float.parseFloat(yAxisDataArm.get(i).toString())));
            yAxisValuesWaist.add(new PointValue(i, Float.parseFloat(yAxisDataWaist.get(i).toString())));
            yAxisValuesWeight.add(new PointValue(i, Float.parseFloat(yAxisDataWeight.get(i).toString())));
        }

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

    }
}
