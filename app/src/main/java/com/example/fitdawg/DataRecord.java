package com.example.fitdawg;

public class DataRecord {
    Double arm, waist, weight;
    String date;

    public DataRecord(){}

    public DataRecord(String date, Double arm, Double waist, Double weight){
        this.date = date;
        this.arm=arm;
        this.waist=waist;
        this.weight=weight;
    }

    public String getDate() {
        return date;
    }
}

