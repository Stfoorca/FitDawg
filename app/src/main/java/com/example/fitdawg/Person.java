package com.example.fitdawg;

public class Person {
    private String date;
    private String weight;
    private String arm;
    private String waist;

    public Person(String date, String weight, String arm, String waist) {
        this.date = date;
        this.weight = weight;
        this.arm = arm;
        this.waist = waist;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getArm() {
        return arm;
    }

    public void setArm(String arm) {
        this.arm = arm;
    }

    public String getWaist() {
        return waist;
    }

    public void setWaist(String waist) {
        this.waist = waist;
    }
}
