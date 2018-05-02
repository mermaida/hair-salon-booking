package com.example.aida.finalproj.Activities.UserActivities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.aida.finalproj.Activities.Authentication.TypeActivity;
import com.example.aida.finalproj.Activities.SalonActivities.SalonGallery;
import com.example.aida.finalproj.Adapters.NearbyAdapter;
import com.example.aida.finalproj.R;
import com.example.aida.finalproj.Classes.Salon;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.firebase.ui.database.FirebaseRecyclerAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;
import static java.lang.Math.round;

public class UserDashboard extends AppCompatActivity {

    EditText search;
    ImageButton searchbtn, userbtn;
    ImageView filter;
    RadioButton all, near, top;
    String uid, value, gnd;
    RecyclerView salon_list;
    DatabaseReference ref1, ref2;
    GeoFire geoFire;
    FirebaseDatabase database;
    double longitude, latitude;
    Query query;
    FloatingActionButton fab;
    FirebaseRecyclerAdapter<Salon, UsersViewHolder> firebaseRecyclerAdapter;
    List<Salon> locationList = new ArrayList<>();
    LocationRequest mLocationRequest;
    FusedLocationProviderClient mFusedLocationProviderClient;
    boolean mLocationPermissionGranted = false;
    final int LOCATION_PERMISSION_CODE = 1234;

    DatabaseReference test;

    @Override
    protected void onStart() {
        super.onStart();
        firebaseRecyclerAdapter.startListening();
    }

    protected void onStop() {
        super.onStop();
        firebaseRecyclerAdapter.cleanup();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);

        isGoogleAPIAvailable();
        getLocationPermission();
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getDeviceLocation();

        test = FirebaseDatabase.getInstance().getReference().child("users");
        test.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
//                    String child = childSnapshot.child("appointments").getValue(String.class);
                    //ref.child(childSnapshot.getKey()).child("child");
                    Log.i("keyval", "" + childSnapshot.getKey());
                    DatabaseReference ref2 = test.child(childSnapshot.getKey()).child("appointments");
                    ref2.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                String rating = childSnapshot.child("rating").getValue(String.class);
                                Log.i("ratingval", "" + rating);
                            }
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


        database = FirebaseDatabase.getInstance();
        ref1 = database.getReference().child("salons_location");
        ref2 = database.getReference().child("salons");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        geoFire = new GeoFire(ref1);

        search = findViewById(R.id.search_field);
        searchbtn = findViewById(R.id.search_btn);

        userbtn = findViewById(R.id.userprof);
        userbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserDashboard.this, UserProfile.class);
                startActivity(intent);
                finish();
            }
        });

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserDashboard.this, ConfirmationActivity.class);
                startActivity(intent);
            }
        });

        gnd = getIntent().getStringExtra("gender");
        if(gnd == null) gnd = "";

        salon_list = findViewById(R.id.result_list);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        salon_list.setLayoutManager(llm);
        Log.i("query", query + "");

        all = findViewById(R.id.all);
        near = findViewById(R.id.near);
        top = findViewById(R.id.top);

        filter = findViewById(R.id.filter);
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserDashboard.this, GenderActivity.class);
                startActivity(intent);
            }
        });

        if (all.isChecked()) {
            if (gnd.equals("female")) {
                query = ref2.orderByChild("female").equalTo(true);
                declareAdapter(query);
                salon_list.setAdapter(firebaseRecyclerAdapter);
            }
            else if (gnd.equals("male")) {
                query = ref2.orderByChild("male").equalTo(true);
                declareAdapter(query);
                salon_list.setAdapter(firebaseRecyclerAdapter);
            }
            else {
                query = ref2.orderByChild("salon_name");
                declareAdapter(query);
                salon_list.setAdapter(firebaseRecyclerAdapter);
            }
        }

        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (gnd.equals("female")) {
                    query = ref2.orderByChild("female").equalTo(true);
                    declareAdapter(query);
                    salon_list.setAdapter(firebaseRecyclerAdapter);
                }
                else if (gnd.equals("male")) {
                    query = ref2.orderByChild("male").equalTo(true);
                    declareAdapter(query);
                    salon_list.setAdapter(firebaseRecyclerAdapter);
                }
                else {
                    query = ref2.orderByChild("salon_name");
                    declareAdapter(query);
                    salon_list.setAdapter(firebaseRecyclerAdapter);
                }
            }
        });

        near.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayNearby();
            }
        });

        top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Query query = ref2.orderByChild("rating").startAt(3.0).limitToLast(20);
                reverseAdapter(query);
                salon_list.setAdapter(firebaseRecyclerAdapter);
            }
        });

        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchText = search.getText().toString();
                firebaseUserSearch(searchText);
            }
        });
    }

    private void displayNearby() {

        geoFire.setLocation(uid, new GeoLocation(latitude, longitude), new GeoFire.CompletionListener() {
            @Override
            public void onComplete(String key, DatabaseError error) {
                if (error != null) {
                    System.err.println("There was an error saving the location to GeoFire: " + error);
                } else {
                    System.out.println("Location saved on server successfully!");
                }
            }
        });

        GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(latitude, longitude), 3);
        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, final GeoLocation location) {
                locationList = new ArrayList<>();
                Query query = ref2.orderByKey().equalTo(key);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Salon loc = snapshot.getValue(Salon.class);
                            locationList.add(0, loc);
                        }

                        NearbyAdapter nearbyAdapter = new NearbyAdapter(UserDashboard.this, locationList);
                        salon_list.setAdapter(nearbyAdapter);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onKeyExited(String key) {
            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {
            }

            @Override
            public void onGeoQueryReady() {
            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });
    }

    public void declareAdapter(Query query) {

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Salon, UsersViewHolder>(
                Salon.class,
                R.layout.list_layout,
                UsersViewHolder.class,
                query
        ) {
            @Override
            protected void populateViewHolder(UsersViewHolder viewHolder, Salon model, final int position) {
                viewHolder.setDetails(getApplicationContext(), model.getSalon_name(), model.getAddress(), model.getRating());
                Log.d("name", "name: " + model.getSalon_name());

                viewHolder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        DatabaseReference ref = firebaseRecyclerAdapter.getRef(position);
                        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Salon salon = dataSnapshot.getValue(Salon.class);
                                value = salon.getSalon_name();
                                Intent intent = new Intent(view.getContext(), SelectService.class).putExtra("name", value);
                                intent.putExtra("id", dataSnapshot.getKey());
                                view.getContext().startActivity(intent);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                });
            }

        };
    }

    public void reverseAdapter(Query query) {

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Salon, UsersViewHolder>(
                Salon.class,
                R.layout.list_layout,
                UsersViewHolder.class,
                query
        ) {
            @Override
            protected void populateViewHolder(UsersViewHolder viewHolder, Salon model, final int position) {
                viewHolder.setDetails(getApplicationContext(), model.getSalon_name(), model.getAddress(), model.getRating());
                Log.d("name", "name: " + model.getSalon_name());

                viewHolder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        DatabaseReference ref = firebaseRecyclerAdapter.getRef(position);
                        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Salon salon = dataSnapshot.getValue(Salon.class);
                                value = salon.getSalon_name();
                                Intent intent = new Intent(view.getContext(), SelectService.class).putExtra("name", value);
                                view.getContext().startActivity(intent);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                });
            }

            @Override
            public Salon getItem(int position) {
                return super.getItem(getItemCount() - 1 - position);
            }
        };
    }

    private void firebaseUserSearch(String searchText) {

        Query firebaseSearchQuery = ref2.orderByChild("salon_name").startAt(searchText).endAt(searchText + "\uf8ff");

        firebaseSearchQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    Log.d("child", "PARENT: " + dataSnapshot.getKey());
                    Log.d("child2", "" + childSnapshot.child("salon_name").getValue(String.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                // Log.e(TAG, "Failed to read app title value.", error.toException());
            }
        });

        declareAdapter(firebaseSearchQuery);
        salon_list.setAdapter(firebaseRecyclerAdapter);

    }

    public static class UsersViewHolder extends RecyclerView.ViewHolder {

        View view;
        TextView name, address, rating;
        ImageView image;

        public UsersViewHolder(View itemView) {
            super(itemView);
            view = itemView;
        }

        public void setDetails(Context ctx, String sname, String saddress, double srating) {

            name = view.findViewById(R.id.name_text);
            address = view.findViewById(R.id.text2);
            rating = view.findViewById(R.id.text1);

            name.setText(sname);
            address.setText(saddress);
            rating.setText("★ " + Double.toString(srating));

            image = itemView.findViewById(R.id.profile_image);

            String url = "https://firebasestorage.googleapis.com/v0/b/finalproj-4653f.appspot.com/o/pet06-512.png?alt=media&token=aa2226c1-3d01-4e00-990c-9792914609c3";
            Glide.with(ctx).load(url).into(image);
        }
    }

    public void isGoogleAPIAvailable() {
        int availability = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
        if (availability == ConnectionResult.SUCCESS) {
            Log.i("Google API", "Succefully installed");
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(availability)) {
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(this, availability, LOCATION_PERMISSION_CODE);
            Log.i("Google Api", "Fixable Error");
        } else
            Toast.makeText(this, "Google API Is not available", Toast.LENGTH_LONG).show();
    }

    private void getLocationPermission() {

        String[] permissions = {
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_NETWORK_STATE,
                android.Manifest.permission.INTERNET,

        };
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this.getApplicationContext(), android.Manifest.permission.ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this.getApplicationContext(), android.Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED) {
                        mLocationPermissionGranted = true;

                    } else {
                        ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_CODE);
                    }
                } else {
                    ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_CODE);

                }
            } else {
                ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_CODE);
            }

        } else {
            ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_CODE);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case LOCATION_PERMISSION_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            Log.i("Location", "Permission Failed");
                            mLocationPermissionGranted = false;
                        }

                    }
                    Log.i("Location", "Permission Granted");
                    mLocationPermissionGranted = true;
                }
            }
        }
    }

    public void getDeviceLocation() {
        try {
            if (mLocationPermissionGranted) {
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(UserDashboard.this, "First enable LOCATION ACCESS in settings.", Toast.LENGTH_LONG).show();
                    return;
                }


//                Task task = mFusedLocationProviderClient.getLastLocation();
//                task.addOnCompleteListener(this, new OnCompleteListener() {
//                    @Override
//                    public void onComplete(@NonNull Task task) {
//                        if (task.isSuccessful() ) {
//                            Location currentLocation = (Location) task.getResult();
//                            Latitude = currentLocation.getLatitude();
//                            Longitude = currentLocation.getLongitude();
//                            Log.i("current Locationss", Latitude + Longitude + "");
//                        }
//                    }
//                });
                checkForLocationRequest();
                mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest, new LocationCallback() {

                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        latitude = locationResult.getLastLocation().getLatitude();
                        longitude = locationResult.getLastLocation().getLongitude();
                    }

                }, Looper.myLooper());
            }
        } catch (Exception e) {
            Log.e("LocationException", e.getMessage());
        }
    }

    public void checkForLocationRequest() {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(1234);
        mLocationRequest.setFastestInterval(123);
    }
}
