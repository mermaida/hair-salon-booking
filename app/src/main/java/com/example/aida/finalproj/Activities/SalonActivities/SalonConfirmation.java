package com.example.aida.finalproj.Activities.SalonActivities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aida.finalproj.Activities.UserActivities.ConfirmationActivity;
import com.example.aida.finalproj.Activities.UserActivities.UserDashboard;
import com.example.aida.finalproj.Activities.UserActivities.UserRegistration;
import com.example.aida.finalproj.Classes.Appointment;
import com.example.aida.finalproj.Classes.DatabaseHelper;
import com.example.aida.finalproj.Classes.Service;
import com.example.aida.finalproj.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

public class SalonConfirmation extends AppCompatActivity {

    String date, time;
    DatabaseReference ref, ref2, ref3;
    DatabaseHelper db;
    Button confirm;
    TextView tp, app;
    double totalprice, totalduration;
    List<String> servicelist;
    List<Service> list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salon_confirmation);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        date = sharedPref.getString("adddate", "date unknown");
        time = sharedPref.getString("addtime", "time unknown");

        db = new DatabaseHelper(this);
        list = new ArrayList<>();
        list.addAll(db.getAllServices());

        totalprice = 0;
        totalduration = 0;
        servicelist = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            double price = list.get(i).getPrice();
            totalprice += price;

            double duration = list.get(i).getDuration();
            totalduration += duration;

            String servicename = list.get(i).getService_name();
            servicelist.add(servicename);
        }

        tp = findViewById(R.id.tp);
        tp.setText("Total Price: ₺" + totalprice);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();

        ref = FirebaseDatabase.getInstance().getReference().child("salons").child(uid).child("schedule");

        app = findViewById(R.id.app);
        app.setText(date + " " + time);

        confirm = findViewById(R.id.confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isConnected = checkConnection();
                if (!isConnected)
                    Toast.makeText(SalonConfirmation.this, "İnternet bağlantısı yok", Toast.LENGTH_SHORT).show();
                else {
                    //ref2.setValue()
                    if (servicelist.size() != 0) {
                        if (time != null) {
                            Appointment appt = new Appointment("manual", servicelist, totalprice);
                            switch (time) {
                                case "09:00":
                                    if (totalduration <= 60)
                                        ref.child(date).child("09:00").setValue(appt);
                                    else if (totalduration > 60) {
                                        ref.child(date).child("09:00").setValue(appt);
                                        ref.child(date).child("10:00").setValue(appt);
                                    } else if (totalduration > 120) {
                                        ref.child(date).child("09:00").setValue(appt);
                                        ref.child(date).child("10:00").setValue(appt);
                                        ref.child(date).child("11:00").setValue(appt);
                                    }
                                    break;
                                case "10:00":
                                    if (totalduration <= 60)
                                        ref.child(date).child("10:00").setValue(appt);
                                    if (totalduration > 60) {
                                        ref.child(date).child("10:00").setValue(appt);
                                        ref.child(date).child("11:00").setValue(appt);
                                    } else if (totalduration > 120) {
                                        ref.child(date).child("10:00").setValue(appt);
                                        ref.child(date).child("11:00").setValue(appt);
                                        ref.child(date).child("12:00").setValue(appt);
                                    }
                                    break;
                                case "11:00":
                                    if (totalduration <= 60)
                                        ref.child(date).child("11:00").setValue(appt);
                                    if (totalduration > 60) {
                                        ref.child(date).child("11:00").setValue(appt);
                                        ref.child(date).child("12:00").setValue(appt);
                                    } else if (totalduration > 120) {
                                        ref.child(date).child("11:00").setValue(appt);
                                        ref.child(date).child("12:00").setValue(appt);
                                        ref.child(date).child("13:00").setValue(appt);
                                    }
                                    break;
                                case "12:00":
                                    if (totalduration <= 60)
                                        ref.child(date).child("12:00").setValue(appt);
                                    if (totalduration > 60) {
                                        ref.child(date).child("12:00").setValue(appt);
                                        ref.child(date).child("13:00").setValue(appt);
                                    } else if (totalduration > 120) {
                                        ref.child(date).child("12:00").setValue(appt);
                                        ref.child(date).child("13:00").setValue(appt);
                                        ref.child(date).child("14:00").setValue(appt);
                                    }
                                    break;
                                case "13:00":
                                    if (totalduration <= 60)
                                        ref.child(date).child("13:00").setValue(appt);
                                    if (totalduration > 60) {
                                        ref.child(date).child("13:00").setValue(appt);
                                        ref.child(date).child("14:00").setValue(appt);
                                    } else if (totalduration > 120) {
                                        ref.child(date).child("13:00").setValue(appt);
                                        ref.child(date).child("14:00").setValue(appt);
                                        ref.child(date).child("15:00").setValue(appt);
                                    }
                                    break;
                                case "14:00":
                                    if (totalduration <= 60)
                                        ref.child(date).child("14:00").setValue(appt);
                                    if (totalduration > 60) {
                                        ref.child(date).child("14:00").setValue(appt);
                                        ref.child(date).child("15:00").setValue(appt);
                                    } else if (totalduration > 120) {
                                        ref.child(date).child("14:00").setValue(appt);
                                        ref.child(date).child("15:00").setValue(appt);
                                        ref.child(date).child("16:00").setValue(appt);
                                    }
                                    break;
                                case "15:00":
                                    if (totalduration <= 60)
                                        ref.child(date).child("15:00").setValue(appt);
                                    if (totalduration > 60) {
                                        ref.child(date).child("15:00").setValue(appt);
                                        ref.child(date).child("16:00").setValue(appt);
                                    } else if (totalduration > 120) {
                                        ref.child(date).child("15:00").setValue(appt);
                                        ref.child(date).child("16:00").setValue(appt);
                                        ref.child(date).child("17:00").setValue(appt);
                                    }
                                    break;
                                case "16:00":
                                    if (totalduration <= 60)
                                        ref.child(date).child("16:00").setValue(appt);
                                    if (totalduration > 60) {
                                        ref.child(date).child("16:00").setValue(appt);
                                        ref.child(date).child("17:00").setValue(appt);
                                    } else if (totalduration > 120) {
                                        ref.child(date).child("16:00").setValue(appt);
                                        ref.child(date).child("17:00").setValue(appt);
                                        ref.child(date).child("18:00").setValue(appt);
                                    }
                                    break;
                            }

                            Toast.makeText(SalonConfirmation.this, "Randevu başarıyla alındı!",
                                    Toast.LENGTH_SHORT).show();

                            DatabaseHelper db = new DatabaseHelper(getApplicationContext());
                            db.clearDatabase();


                            Intent intent = new Intent(SalonConfirmation.this, SalonDashboard.class);
                            startActivity(intent);
                            finish();

                        } else Toast.makeText(SalonConfirmation.this, "Lütfen zamani seçiniz!",
                                Toast.LENGTH_SHORT).show();
                    } else Toast.makeText(SalonConfirmation.this, "Lütfen servisi seçiniz!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public boolean checkConnection() {
        ConnectivityManager cm =
                (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        final boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        return isConnected;
    }
}
