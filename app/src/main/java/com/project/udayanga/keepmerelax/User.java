package com.project.udayanga.keepmerelax;

/**
 * Created by Udayanga on 6/25/2016.
 */
public class User {
    String name,password,dob,gender;
    int low,peak;

    public User() {

    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getDob() {
        return dob;
    }

    public String getGender() {
        return gender;
    }

    public int getLow() {
        return low;
    }

    public int getPeak() {
        return peak;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setLow(int low) {
        this.low = low;
    }

    public void setPeak(int peak) {
        this.peak = peak;
    }

    public User(String name, String password, String dob, String gender, int low, int peak) {
        this.name = name;
        this.password = password;
        this.dob = dob;
        this.gender = gender;
        this.low = low;
        this.peak = peak;
    }
}
