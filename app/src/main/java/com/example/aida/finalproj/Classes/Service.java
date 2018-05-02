package com.example.aida.finalproj.Classes;

/**
 * Created by Aida on 10.04.2018..
 */

public class Service {

    public static final String TABLE_NAME = "selected_services";

    public static final String COLUMN_SALON_ID = "_id";
    public static final String COLUMN_SERVICE_NAME = "name";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_DURATION = "duration";
    public static final String COLUMN_IMAGE = "image";



    public String service_name;
    public double price;
    public int duration;
    public Boolean exists;
    public String image;

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_SALON_ID + " TEXT,"
                    + COLUMN_SERVICE_NAME + " TEXT,"
                    + COLUMN_PRICE + " DOUBLE,"
                    + COLUMN_DURATION + " INTEGER,"
                    + COLUMN_IMAGE + " TEXT"
                    + ")";

    public Service() {
    }

    public Service(String service_name, double price, int duration, boolean exists, String image) {
        this.service_name = service_name;
        this.price = price;
        this.duration = duration;
        this.exists = exists;
        this.image = image;
    }

    public String getService_name() {
        return this.service_name;
    }

    public void setService_name(String service_name) {
        this.service_name = service_name;
    }

    public double getPrice() {
        return this.price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getDuration() {
        return this.duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public boolean getExists() {
        return this.exists;
    }

    public void setExists(boolean exists) {
        this.exists = exists;
    }

    public String getImage() {
        return this.image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
