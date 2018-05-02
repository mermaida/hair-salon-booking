package com.example.aida.finalproj.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.example.aida.finalproj.Adapters.ConfirmAdapter;
import com.example.aida.finalproj.Adapters.DisplayAdapter;
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

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Aida on 13.04.2018..
 */

public class Fragment1 extends Fragment {

    RecyclerView recview;
    Salon displayedSalon;
    List<Service> displayedService;
    DatabaseReference ref;
    List<String> keys;
    String value;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_1, container, false);

        recview = view.findViewById(R.id.service_list);
        LinearLayoutManager llm = new LinearLayoutManager(this.getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recview.setLayoutManager(llm);

        value = getActivity().getIntent().getStringExtra("name");

        //Context context = getApplicationContext();

        ref = FirebaseDatabase.getInstance().getReference().child("salons");
        Query query = ref.orderByChild("salon_name").equalTo(value);

        // name = view.findViewById(R.id.salonname);

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

                // name.setText(displayedSalon.getSalon_name());

              /*  book = getView().findViewById(R.id.bookapt);
                book.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), BookAppointment.class).putExtra("id", keys.get(0));
                        startActivity(intent);
                    }
                });*/

                ref = FirebaseDatabase.getInstance().getReference().child("salons").child(keys.get(0)).child("services");
                Query query = ref.orderByChild("exists").equalTo(true);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                            Log.d("child", "PARENT: " + dataSnapshot.getKey());
                            Log.d("child2", "" + childSnapshot.child("service_name").getValue(String.class));

                            Service service = childSnapshot.getValue(Service.class);
                            Log.i("srv", "" + service.getService_name());
                            displayedService.add(service);
                        }

                        DisplayAdapter displayAdapter = new DisplayAdapter(getActivity(), displayedService);
                        recview.setAdapter(displayAdapter);
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.e("errortag", "Failed to read app title value.", error.toException());
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e("errortag", "Failed to read app title value.", error.toException());
            }
        });

        return view;
    }

    /*@Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            this.listener = (OnItemClick) context;
        }
    }*/
}
