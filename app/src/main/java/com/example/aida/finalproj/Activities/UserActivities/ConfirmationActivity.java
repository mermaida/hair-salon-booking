package com.example.aida.finalproj.Activities.UserActivities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aida.finalproj.Classes.Appointment;
import com.example.aida.finalproj.Classes.DatabaseHelper;
import com.example.aida.finalproj.Classes.Service;
import com.example.aida.finalproj.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ConfirmationActivity extends AppCompatActivity {

    String value, date, stime, sdate, uid, dur, name;
    DatabaseReference ref, ref2, ref3;
    DatabaseHelper db;
    Button time, confirm;
    TextView tp, app;
    double totalprice, totalduration;
    List<String> servicelist;
    List<Service> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        db = new DatabaseHelper(this);
        value = db.fetchId();
        date = getIntent().getStringExtra("date");
        stime = getIntent().getStringExtra("time");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        app = findViewById(R.id.app);

        if (date == null || stime == null) {
            app.setText("Tarih ve zaman seçilmedi.");
        } else {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                Date sdate = sdf.parse(date);
                long millis = sdate.getTime();
                String millidate = Long.toString(millis);
                ref2 = FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("appointments").child(millidate);
                app.setText(date + " " + stime);
            } catch (java.text.ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }


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
        tp.setText("Toplam fiyat: ₺" + totalprice);

        ref = FirebaseDatabase.getInstance().getReference().child("salons").child(value).child("schedule");
        ref3 = FirebaseDatabase.getInstance().getReference().child("salons").child(value);

        ref3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                name = dataSnapshot.child("salon_name").getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        dur = Double.toString(totalduration);

        time = findViewById(R.id.time);
        confirm = findViewById(R.id.confirm);
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ConfirmationActivity.this, BookAppointment.class).putExtra("id", value);
                intent.putExtra("duration", dur);
                startActivity(intent);
            }
        });


        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isConnected = checkConnection();
                if (!isConnected)
                    Toast.makeText(ConfirmationActivity.this, "İnternet bağlantısı yok", Toast.LENGTH_SHORT).show();
                else {
                    //ref2.setValue()
                    Log.i("listsize", "" + list.size());
                    Log.i("listsize", "" + servicelist.size());
                    if (servicelist.size() != 0) {
                        if (stime != null) {
                            Appointment appt = new Appointment(uid, servicelist, totalprice);
                            ref2.setValue(appt);
                            ref2.child("salon_name").setValue(name);
                            ref2.child("time").setValue(stime);
                            ref2.child("user_id").removeValue();
                            switch (stime) {
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

                            Toast.makeText(ConfirmationActivity.this, "Randevu başarıyla alındı!",
                                    Toast.LENGTH_SHORT).show();

                            DatabaseHelper db = new DatabaseHelper(getApplicationContext());
                            db.clearDatabase();


                            Intent intent = new Intent(ConfirmationActivity.this, UserDashboard.class);
                            startActivity(intent);
                            finish();
                        } else Toast.makeText(ConfirmationActivity.this, "Lütfen zamani seçiniz!",
                                Toast.LENGTH_SHORT).show();
                    } else Toast.makeText(ConfirmationActivity.this, "Lütfen servisi seçiniz!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });


        // ref.child(date).child("13:00").setValue(true);

        //confirmAdapter.notifyDataSetChanged();

        //SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        // String uid = sharedPref.getString("uid", "Not Available");
        // String value = sharedPref.getString("service", "Not Available");
       /*

        String pricestr = sharedPref.getString("price", "Not Available");
        String durstr = sharedPref.getString("duration", "Not Available");

        Log.i("stringvalue", ""+value);

        Double price = Double.parseDouble(pricestr);
        Double dd = Double.parseDouble(durstr);
        int duration = dd.intValue();

        Service service = new Service(value, price, duration, true, null);

        list.add(service);

        */

        /*ref = FirebaseDatabase.getInstance().getReference().child("salons").child(uid).child("services");
        Query query = ref.orderByChild("service_name").equalTo(value);

        list = new ArrayList<>();
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               //
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    Log.d("child", "PARENT: " + dataSnapshot.getKey());
                    Log.d("servicechild", "" + childSnapshot.child("service_name").getValue(String.class));

                    Service service = childSnapshot.getValue(Service.class);
                   // Log.d("servicechild", "" + dataSnapshot.getValue(String.class));
                    list.add(service);


//                  String price = childSnapshot.child("price").getValue(String.class);

                    //Log.d("servicechild", "" + price);
                    String key = childSnapshot.getKey();
                    Log.i("parent id", "" + key);
                   // list.add(0, service);
                }

                ConfirmAdapter confirmAdapter = new ConfirmAdapter(ConfirmationActivity.this, list);
                recview.setAdapter(confirmAdapter);
            }

            public void onCancelled(DatabaseError error) {

            }
        });*/
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
