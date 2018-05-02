package com.example.aida.finalproj.Activities.UserActivities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.aida.finalproj.Adapters.ConfirmAdapter;
import com.example.aida.finalproj.Adapters.DisplayAdapter;
import com.example.aida.finalproj.Adapters.ViewPagerAdapter;
import com.example.aida.finalproj.Classes.Salon;
import com.example.aida.finalproj.Classes.Service;
import com.example.aida.finalproj.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SelectService extends AppCompatActivity {

    String value;
    TabLayout tabLayout;
    FloatingActionButton fab;
    ImageView gallery;
    List<Uri> urls;
    TextView viewgallery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_service);

        value = getIntent().getStringExtra("id");

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("id", value);
        editor.commit();

        tabLayout = findViewById(R.id.tablayout);

        final ViewPager viewPager = findViewById(R.id.viewpager);
        final ViewPagerAdapter adapter = new ViewPagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectService.this, ConfirmationActivity.class);
                intent.putExtra("id", value);
                startActivity(intent);
            }
        });

        gallery = findViewById(R.id.gallery);

        urls = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("salons").child(value).child("gallery");
        Query query = ref.orderByChild("image_url");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    String string_uri = childSnapshot.child("image_url").getValue(String.class);
                    Log.i("checkuri", "" + string_uri);
                    Uri image_url = Uri.parse(string_uri);
                    urls.add(image_url);
                }
                if (urls.size() == 0)
                    Glide.with(SelectService.this).load(R.drawable.dashlogo).apply(RequestOptions.centerCropTransform()).into(gallery);
                else
                    Glide.with(SelectService.this).load(urls.get(0)).apply(RequestOptions.centerCropTransform()).into(gallery);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        gallery.setColorFilter(Color.rgb(123, 123, 123), android.graphics.PorterDuff.Mode.MULTIPLY);
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        viewgallery = findViewById(R.id.viewgallery);
        viewgallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectService.this, UserGallery.class).putExtra("id", value);
                startActivity(intent);
            }
        });

    }


       /* recview = findViewById(R.id.service_list);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recview.setLayoutManager(llm);




        value = getIntent().getStringExtra("name");

        ref = FirebaseDatabase.getInstance().getReference().child("salons");
        Query query = ref.orderByChild("salon_name").equalTo(value);


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

                book = findViewById(R.id.bookapt);
                book.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(SelectService.this, BookAppointment.class).putExtra("id", keys.get(0));
                        startActivity(intent);
                    }
                });

                ref = FirebaseDatabase.getInstance().getReference().child("salons").child(keys.get(0)).child("services");
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                            Log.d("child", "PARENT: " + dataSnapshot.getKey());
                            Log.d("child2", "" + childSnapshot.child("service_name").getValue(String.class));

                            Service service = childSnapshot.getValue(Service.class);
                            Log.i("srv", "" + service.getService_name());
                            displayedService.add(0, service);
                        }

                      //  name.setText(displayedSalon.getSalon_name());
                        DisplayAdapter displayAdapter = new DisplayAdapter(SelectService.this, displayedService);
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
*/

}
