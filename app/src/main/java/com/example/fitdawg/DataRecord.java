package com.example.fitdawg;

public class DataRecord implements Cloneable {
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

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}

