package com.example.aida.finalproj.Activities.SalonActivities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aida.finalproj.Activities.UserActivities.BookAppointment;
import com.example.aida.finalproj.Activities.UserActivities.CalendarActivity;
import com.example.aida.finalproj.Classes.Appointment;
import com.example.aida.finalproj.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ScheduleOverview extends AppCompatActivity {

    private TextView textdate;
    private CardView selectdate;
    private ListView listview;
    String date, time;
    ArrayList<String> list;
    TextView username, srvname, price;
    String user_id, usernames;
    List<String> users, services;
    double prices;
    Long millidate;
    Button cancel, viewprof, add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_overview);

        textdate = findViewById(R.id.date);
        selectdate = findViewById(R.id.carddate);
        listview = findViewById(R.id.listview);

        list = new ArrayList<>();
        list.add("09:00");
        list.add("10:00");
        list.add("11:00");
        list.add("12:00");
        list.add("13:00");
        list.add("14:00");
        list.add("15:00");
        list.add("16:00");

        ArrayAdapter<String> arrayAdapter =
                new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, list);
        // Set The Adapter
        listview.setAdapter(arrayAdapter);


        selectdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScheduleOverview.this, SalonCalendarActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Intent incoming = getIntent();
        date = incoming.getStringExtra("date");

        if (date == null) {
            date = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(new Date());
            textdate.setText(date);
        }
        else {
            textdate.setText(date);
        }

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View v, int position, long l)
            {
                time = list.get(position);

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                final String uid = user.getUid();

                users = new ArrayList<>();

                //if haschild for checking if the value exists, if not set textview invisible and show no appointment for today

                final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("salons").child(uid).child("schedule").child(date);
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild(time)) {

                            LinearLayout mainLayout = findViewById(R.id.lnlyt);

                            // inflate the layout of the popup window
                            LayoutInflater inflater = (LayoutInflater)
                                    getSystemService(LAYOUT_INFLATER_SERVICE);
                            final View popupView = inflater.inflate(R.layout.popup_window, null);

                            // create the popup window
                            int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                            int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                            boolean focusable = true; // lets taps outside the popup also dismiss it
                            final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

                            popupWindow.showAtLocation(mainLayout, Gravity.CENTER, 0, 0);

                            username = popupView.findViewById(R.id.username);
                            srvname = popupView.findViewById(R.id.srvname);
                            price = popupView.findViewById(R.id.priceamnt);


                            popupView.setOnTouchListener(new View.OnTouchListener() {
                                @Override
                                public boolean onTouch(View v, MotionEvent event) {
                                    popupWindow.dismiss();
                                    return true;
                                }
                            });

                            DatabaseReference ref3 = ref.child(time);

                            ref3.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Log.i("keyenter", "i entered here");
                                    Log.i("does the value exist?", "" +dataSnapshot.getValue());

                           /* user_id = snapshot.child("user_id").getValue(String.class);
                            users.add(user_id);
                            services = snapshot.child("service_name").getValue(ArrayList.class);
                            prices = snapshot.child("total_price").getValue(String.class);*/

                                    Appointment appointment = dataSnapshot.getValue(Appointment.class);
                                    user_id = appointment.getUser_id();
                                    services = appointment.getService_name();
                                    prices = appointment.getTotal_price();

                                    users.add(0, user_id);

                                    viewprof = popupView.findViewById(R.id.viewbtn);
                                    viewprof.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent intent = new Intent(ScheduleOverview.this, ViewUserProfile.class).putExtra("userid", users.get(0));
                                            startActivity(intent);
                                            popupWindow.dismiss();
                                        }
                                    });

                                    cancel = popupView.findViewById(R.id.cancelbtn);
                                    cancel.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Query query = ref.orderByChild("user_id").equalTo(users.get(0));
                                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                                        String time1 = childSnapshot.getKey();
                                                        ref.child(time1).removeValue();

                                                        SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy");
                                                        try {
                                                            Date dateobj = dt.parse(date);
                                                            SimpleDateFormat dt1 = new SimpleDateFormat("yyyy/M/d");
                                                            String date1 = dt1.format(dateobj);
                                                            Date dateobj1 = dt1.parse(date1);
                                                            millidate = dateobj1.getTime();
                                                        } catch (ParseException e) {
                                                            e.printStackTrace();
                                                        }

                                                        DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("appointments");
                                                        ref2.child(Long.toString(millidate)).removeValue();
                                                    }
                                                }
                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });
                                            Toast.makeText(ScheduleOverview.this, "Randevu iptal edildi.",
                                                    Toast.LENGTH_SHORT).show();
                                            popupWindow.dismiss();
                                        }
                                    });


                                    if (services != null) {
                                        for (int i = 0; i < services.size(); i++) {
                                            srvname.setText("Servisler: " + services.get(i) + ", ");
                                        }
                                        price.setText("Toplam fiyat " + prices);
                                    }

                                    if (users.size() != 0) {

                                        DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference().child("users").child(users.get(0));
                                        ref2.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                usernames = dataSnapshot.child("name").getValue(String.class);
                                                username.setText("Müşteri: " + usernames);
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                        }
                        else {
                            LinearLayout mainLayout = findViewById(R.id.lnlyt);
                            // inflate the layout of the popup window
                            LayoutInflater inflater = (LayoutInflater)
                                    getSystemService(LAYOUT_INFLATER_SERVICE);
                            View popupView = inflater.inflate(R.layout.popup_window_add, null);

                            // create the popup window
                            int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                            int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                            boolean focusable = true; // lets taps outside the popup also dismiss it
                            final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

                            popupWindow.showAtLocation(mainLayout, Gravity.CENTER, 0, 0);

                            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString("adddate", date);
                            editor.putString("addtime", time);
                            editor.commit();


                            add = popupView.findViewById(R.id.addbtn);
                            add.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(ScheduleOverview.this, AddAppointment.class);
                                    startActivity(intent);
                                    popupWindow.dismiss();
                                }
                            });

                            popupView.setOnTouchListener(new View.OnTouchListener() {
                                @Override
                                public boolean onTouch(View v, MotionEvent event) {
                                    popupWindow.dismiss();
                                    return true;
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
        });


    }
}
