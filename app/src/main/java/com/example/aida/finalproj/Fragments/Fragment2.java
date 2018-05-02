package com.example.aida.finalproj.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.aida.finalproj.Classes.Salon;
import com.example.aida.finalproj.Classes.Service;
import com.example.aida.finalproj.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aida on 13.04.2018..
 */

public class Fragment2 extends Fragment {

    RecyclerView recview;
    TextView salonname, name, address, phone, email, rating;
    Salon displayedSalon;
    List<Service> displayedService;
    DatabaseReference ref;
    List<String> keys;
    Button map;
    String value;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_2, container, false);

        value = getActivity().getIntent().getStringExtra("name");
        Log.i("intentvalue", ""+value);

        ref = FirebaseDatabase.getInstance().getReference().child("salons");
        Query query = ref.orderByChild("salon_name").equalTo(value);

        salonname = view.findViewById(R.id.salon_name);
        name = view.findViewById(R.id.ownername);
        address = view.findViewById(R.id.address);
        phone = view.findViewById(R.id.phone);
        email = view.findViewById(R.id.email);
        rating = view.findViewById(R.id.rating);

        map = view.findViewById(R.id.seemap);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    Log.d("child", "PARENT: " + dataSnapshot.getKey());
                    Log.d("child2", "" + childSnapshot.child("salon_name").getValue(String.class));

                    displayedService = new ArrayList<>();
                    displayedSalon = childSnapshot.getValue(Salon.class);

                    String key = childSnapshot.getKey();
                    Log.i("parent id", "" + key);
                    keys = new ArrayList<>();
                    keys.add(0, key);
                }

                salonname.setText(displayedSalon.getSalon_name());
                name.setText("Owner: " + displayedSalon.getName());
                address.setText("Address: "+ displayedSalon.getAddress());
                phone.setText("Phone: " + displayedSalon.getPhone());
                email.setText("Email: " + displayedSalon.getEmail());

                double rate = displayedSalon.getRating();
                String stringrate = Double.toString(rate);
                rating.setText("★ " + stringrate);

                map.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Uri mapUri = Uri.parse("geo:0,0?q=" + Uri.encode(displayedSalon.getAddress()));
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, mapUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        startActivity(mapIntent);
                    }
                });

              /*  book = getView().findViewById(R.id.bookapt);
                book.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), BookAppointment.class).putExtra("id", keys.get(0));
                        startActivity(intent);
                    }
                });*/
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
