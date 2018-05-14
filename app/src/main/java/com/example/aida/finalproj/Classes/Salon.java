package com.example.aida.finalproj.Classes;

import java.util.Map;

/**
 * Created by Aida on 06.04.2018..
 */

public class Salon {

    public String name;
    public String salon_name;
    public String phone;
    public String email;
    public String password;
    public String address;
    public double rating;
    public boolean female;
    public boolean male;

    public Salon() {
    }

    public Salon(String name, String salon_name, String phone, String email, String password, String address, double rating, boolean female, boolean male) {
        this.name = name;
        this.salon_name = salon_name;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.address = address;
        this.rating = rating;
        this.female = female;
        this.male = male;

    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSalon_name() {
        return this.salon_name;
    }

    public void setSalon_name(String salon_name) {
        this.salon_name = salon_name;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() { return this.password; }

    public double getRating()  {
        return this.rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public boolean getFemale() {
        return this.female;
    }

    public void setFemale(boolean female) {
        this.female = female;
    }

    public boolean getMale() {
        return this.male;
    }

    public void setMale(boolean male) {
        this.male = male;
    }


}
