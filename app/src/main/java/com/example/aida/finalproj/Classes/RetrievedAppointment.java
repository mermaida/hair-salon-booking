package com.example.aida.finalproj.Classes;

import java.util.List;

/**
 * Created by Aida on 23.04.2018..
 */

public class RetrievedAppointment {
    public String salon_name;
    public List<String> service_name;
    public double total_price;
    public String date;
    public String time;

    public RetrievedAppointment() {
    }

    public RetrievedAppointment(String salon_name, List<String> service_name, double totalprice, String date, String time) {
        this.salon_name = salon_name;
        this.service_name = service_name;
        this.total_price = totalprice;
        this.date = date;
        this.time = time;
    }

    public String getSalon_name() {
        return this.salon_name;
    }

    public void setSalon_name(String salon_name) {
        this.salon_name = salon_name;
    }

    public List<String> getService_name() {
        return this.service_name;
    }

    public void setService_name(List<String> service_name) {
        this.service_name = service_name;
    }

    public double getTotal_price() {
        return this.total_price;
    }

    public void setTotal_price(double total_price) {
        this.total_price = total_price;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return this.time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
