package com.example.aida.finalproj.Classes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Aida on 21.04.2018..
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "services_db";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        // create notes table
        db.execSQL(Service.CREATE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + Service.TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    public long insertService(String uid, String service_name, double price, int duration, String image) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(Service.COLUMN_SALON_ID, uid);
        values.put(Service.COLUMN_SERVICE_NAME, service_name);
        values.put(Service.COLUMN_PRICE, price);
        values.put(Service.COLUMN_DURATION, duration);
        values.put(Service.COLUMN_IMAGE, image);

        // insert row
        long id = db.insert(Service.TABLE_NAME, null, values);
        if(id != -1)
            Toast.makeText(getApplicationContext(), "New row added, row id: " + id, Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getApplicationContext(), "Something wrong", Toast.LENGTH_SHORT).show();

        // close db connection
        db.close();

        return id;
    }


    public List<Service> getAllServices() {
        List<Service> list = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + Service.TABLE_NAME + " ORDER BY " +
                Service.COLUMN_SERVICE_NAME + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Service service = new Service();
                service.setService_name(cursor.getString(cursor.getColumnIndex(Service.COLUMN_SERVICE_NAME)));
                service.setPrice(cursor.getDouble(cursor.getColumnIndex(Service.COLUMN_PRICE)));
                service.setDuration(cursor.getInt(cursor.getColumnIndex(Service.COLUMN_DURATION)));
                service.setImage(cursor.getString(cursor.getColumnIndex(Service.COLUMN_IMAGE)));

                list.add(service);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return list;
    }

    public void deleteService(Service service) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Service.TABLE_NAME, Service.COLUMN_SERVICE_NAME + " = ?",
                new String[]{String.valueOf(service.getService_name())});
        db.close();
    }

    public boolean checkExists(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        String Query = "Select * from " + Service.TABLE_NAME + " where " + Service.COLUMN_SERVICE_NAME + " = \"" + name + "\";";
        Cursor cursor = db.rawQuery(Query, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        db.close();
        return true;
    }

    public String checkId(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String result = id;
        String Query = "Select " + Service.COLUMN_SALON_ID + " from " + Service.TABLE_NAME;
        Cursor cursor = db.rawQuery(Query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return result;
        }
        else {
            cursor.moveToFirst();
            result = cursor.getString(0);
            cursor.close();
            db.close();
            return result;
        }
    }

    public String fetchId() {
        SQLiteDatabase db = this.getReadableDatabase();
        String result = "";
        String Query = "Select " + Service.COLUMN_SALON_ID + " from " + Service.TABLE_NAME;
        Cursor cursor = db.rawQuery(Query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return result;
        }
        else {
            cursor.moveToFirst();
            result = cursor.getString(0);
            cursor.close();
            db.close();
            return result;
        }
    }

    public void clearDatabase() {
        String clearDBQuery = "DELETE FROM " + Service.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(clearDBQuery);
    }
}