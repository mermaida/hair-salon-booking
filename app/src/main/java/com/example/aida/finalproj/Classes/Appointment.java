package com.example.aida.finalproj.Classes;

import java.util.List;

/**
 * Created by Aida on 22.04.2018..
 */

public class Appointment {
    public String user_id;
    public List<String> service_name;
    public double total_price;

    public Appointment() {
    }

    public Appointment(String user_id, List<String> service_name, double totalprice) {
        this.user_id = user_id;
        this.service_name = service_name;
        this.total_price = totalprice;
    }

    public String getUser_id() {
        return this.user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
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

}
