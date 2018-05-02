package com.example.aida.finalproj.Activities.SalonActivities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.aida.finalproj.Activities.UserActivities.ConfirmationActivity;
import com.example.aida.finalproj.Adapters.DisplayAdapter;
import com.example.aida.finalproj.Adapters.SalonDisplayAdapter;
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

import java.util.ArrayList;
import java.util.List;

public class AddAppointment extends AppCompatActivity {

    RecyclerView recview;
    Salon displayedSalon;
    List<Service> displayedService;
    DatabaseReference ref;
    List<String> keys;
    String value, date, time;
    Button cont;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_appointment);

        recview = findViewById(R.id.addview);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recview.setLayoutManager(llm);

        cont = findViewById(R.id.cont);
        cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddAppointment.this, SalonConfirmation.class);
                intent.putExtra("adddate", date);
                intent.putExtra("addtime", time);
                startActivity(intent);
            }
        });

        //Context context = getApplicationContext();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();

        displayedService = new ArrayList<>();

        ref = FirebaseDatabase.getInstance().getReference().child("salons").child(uid);

        // name = view.findViewById(R.id.salonname);

        ref = FirebaseDatabase.getInstance().getReference().child("salons").child(uid).child("services");
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

                SalonDisplayAdapter displayAdapter = new SalonDisplayAdapter(AddAppointment.this, displayedService);
                recview.setAdapter(displayAdapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e("errortag", "Failed to read app title value.", error.toException());
            }
        });
    }
}


