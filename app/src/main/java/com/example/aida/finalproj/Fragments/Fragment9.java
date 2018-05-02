package com.example.aida.finalproj.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.aida.finalproj.Adapters.HistoryAdapter;
import com.example.aida.finalproj.Adapters.RetrievedAdapter;
import com.example.aida.finalproj.Adapters.SalonRetrieveAdapter;
import com.example.aida.finalproj.Classes.RetrievedAppointment;
import com.example.aida.finalproj.Classes.Salon;
import com.example.aida.finalproj.Classes.Service;
import com.example.aida.finalproj.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Aida on 23.04.2018..
 */

public class Fragment9 extends Fragment {
    RecyclerView recview;
    Salon displayedSalon;
    List<Service> displayedService;
    DatabaseReference ref;
    List<String> services;
    String value;
    String service, date, time, salon_id, name;
    Long millidate, millidatedb;
    Date date1, date2;

    double price;
    List<RetrievedAppointment> retrievedAppointmentList;
    //RetrievedAppointment retrievedAppointment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_5, container, false);

        recview = view.findViewById(R.id.appt_list);
        LinearLayoutManager llm = new LinearLayoutManager(this.getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recview.setLayoutManager(llm);

        String uid = getActivity().getIntent().getStringExtra("userid");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Date dt = new Date();
        String today = sdf.format(dt);

        try {
            date1 = sdf.parse(today);
            millidate = date1.getTime();
            Log.i("timedate", "today: " + millidate);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        services = new ArrayList<>();

        ref = FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("appointments");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                retrievedAppointmentList = new ArrayList<>();
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    Log.d("child", "PARENT: " + dataSnapshot.getKey());
                    Log.d("child2", "" + childSnapshot.child("salon_id").getValue(String.class));

                    //salon_id = childSnapshot.child("salon_id").getValue(String.class);
                    date = childSnapshot.getKey();
                    String sdate = date.replaceAll("-", "/");

                    millidatedb = Long.parseLong(sdate);
                    Log.i("timedate", "apptday: " + millidatedb);

                    Date dateobj = new Date(millidatedb);
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    String millitodate = sdf.format(dateobj);

                    if (millidate > millidatedb) {

                        final RetrievedAppointment retrievedAppointment = childSnapshot.getValue(RetrievedAppointment.class);
                        retrievedAppointment.setDate(millitodate);

                        retrievedAppointmentList.add(retrievedAppointment);

                        Log.d("testlist", "" + retrievedAppointmentList.size());
                        Log.d("test1", "service " + retrievedAppointmentList.get(0).getSalon_name());
                        // Log.d("test1", "service2 " + retrievedAppointmentList.get(1).getSalon_name());
                        SalonRetrieveAdapter historyAdapter = new SalonRetrieveAdapter(getActivity(), retrievedAppointmentList);
                        recview.setAdapter(historyAdapter);
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e("errortag", "Failed to read app title value.", error.toException());
            }


        });

        return view;
    }
}
