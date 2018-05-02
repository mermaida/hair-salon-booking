package com.example.aida.finalproj.Activities.SalonActivities;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.aida.finalproj.Adapters.PlaceAutoCompleteAdapter;
import com.example.aida.finalproj.Classes.Library;
import com.example.aida.finalproj.R;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SelectLocation extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener {

    Library library;
    GoogleMap mMap;
    Button sendBtn;
    ImageButton clearbtn;
    boolean mLocationPermissionGranted = false;
    private final int LOCATION_PERMISSION_CODE = 1234;
    FusedLocationProviderClient mFusedLocationProviderClient;
    public Double Latitude;
    private GeoDataClient mGeoDataClient;
    AutoCompleteTextView mAutocompleteView;
    public Double Longitude;
    PlaceAutoCompleteAdapter mAdapter;
    private static final LatLngBounds BOUNDS_GREATER_SYDNEY = new LatLngBounds(
            new LatLng(-34.041458, 150.790100), new LatLng(-33.682247, 151.383362));
    double latitude, longitude;
    GeoFire geofire;
    String uid, address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        getLocationPermission();
//        getDeviceLocation();
        mGeoDataClient = Places.getGeoDataClient(this, null);
        mAutocompleteView = (AutoCompleteTextView) findViewById(R.id.autocomplete);
        mAdapter = new PlaceAutoCompleteAdapter(this, mGeoDataClient, BOUNDS_GREATER_SYDNEY, null);
        mAutocompleteView.setAdapter(mAdapter);
        mAutocompleteView.setOnItemClickListener(mAutocompleteClickListener);
        sendBtn = (Button) findViewById(R.id.send_address);
        sendBtn.setOnClickListener(this);
        library = new Library(this);
        library.statusCheck(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        clearbtn = findViewById(R.id.button_clear);
        clearbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAutocompleteView.setText("");
            }
        });


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
//                        getDeviceLocation();

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


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        getDeviceLocation();
    }

    public void moveCamera(LatLng latLng) {

        mMap.addMarker(new MarkerOptions().position(latLng).title("Your Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));

    }

    public void getDeviceLocation() {
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try {
            if (mLocationPermissionGranted) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }

                mFusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        Longitude = location.getLongitude();
                        Latitude = location.getLatitude();
                        moveCamera(new LatLng(Latitude, Longitude));

                    }
                });
            }

        } catch (Exception e) {
            Log.e("LocationException", e.getMessage());
        }
    }

    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            /*
             Retrieve the place ID of the selected item from the Adapter.
             The adapter stores each Place suggestion in a AutocompletePrediction from which we
             read the place ID and title.
              */
            final AutocompletePrediction item = mAdapter.getItem(position);
            final String placeId = item.getPlaceId();
            final CharSequence primaryText = item.getPrimaryText(null);

            Log.i("Select place", "Autocomplete item selected: " + primaryText);

            /*
             Issue a request to the Places Geo Data Client to retrieve a Place object with
             additional details about the place.
              */
            Task<PlaceBufferResponse> placeResult = mGeoDataClient.getPlaceById(placeId);
            placeResult.addOnCompleteListener(mUpdatePlaceDetailsCallback);

            Toast.makeText(getApplicationContext(), "Clicked: " + primaryText,
                    Toast.LENGTH_SHORT).show();
            Log.i("getPlace", "Called getPlaceById to get Place details for " + placeId);
        }
    };

    private OnCompleteListener<PlaceBufferResponse> mUpdatePlaceDetailsCallback
            = new OnCompleteListener<PlaceBufferResponse>() {
        @Override
        public void onComplete(Task<PlaceBufferResponse> task) {
            try {
                PlaceBufferResponse places = task.getResult();

                // Get the Place object from the buffer.
                final Place place = places.get(0);
                address = place.getAddress().toString();
                LatLng latlng = place.getLatLng();
                latitude = latlng.latitude;
                longitude = latlng.longitude;
               /* User user = (User) getApplicationContext();
                user.setLatitude(place.getLatLng().latitude);
                user.setLongitude(place.getLatLng().longitude);
                Log.i("Placauto", place.getAddress().toString());
                user.setPasteryAddress(place.getAddress().toString());*/

                moveCamera(new LatLng(place.getLatLng().latitude, place.getLatLng().longitude));
//        mrecyclerView.setAdapter(adapter);


            } catch (Exception e) {
            }
        }
    };

    @Override
    public void onClick(View view) {
        if (view == sendBtn) {
            if (address != null || address != "") {
                geofire = new GeoFire(FirebaseDatabase.getInstance().getReference().child("salons_location"));
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                uid = user.getUid();
                geofire.setLocation(uid, new GeoLocation(latitude, longitude), new GeoFire.CompletionListener() {
                    @Override
                    public void onComplete(String key, DatabaseError error) {
                        if (error != null) {
                            System.err.println("There was an error saving the location to GeoFire: " + error);
                        } else {
                            System.out.println("Location saved on server successfully!");
                        }
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("salons").child(uid).child("address");
                        ref.setValue(address);
                    }
                });
                Intent intent = new Intent(SelectLocation.this, SalonDashboard.class);
                startActivity(intent);
                finish();
            }
            else
                Toast.makeText(SelectLocation.this, "Please enter a location", Toast.LENGTH_SHORT).show();
        }
    }
}


/*import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.aida.finalproj.R;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.List;

public class SelectLocation extends FragmentActivity {

    CardView cardloc;
    double latitude, longitude;
    GeoFire geofire;
    String uid, address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_location);

        // adrs = findViewById(R.id.address);
        cardloc = findViewById(R.id.cardloc);

        geofire = new GeoFire(FirebaseDatabase.getInstance().getReference().child("salons_location"));


        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
                .build();
        autocompleteFragment.setFilter(typeFilter);

        ((EditText)autocompleteFragment.getView().findViewById(R.id.place_autocomplete_search_input)).setTextColor(Color.parseColor("#FFFFFF"));
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.i("place", "Place: " + place.getName());
                address = place.getAddress().toString();
                LatLng latlng = place.getLatLng();
                latitude = latlng.latitude;
                longitude = latlng.longitude;
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i("errorplace", "An error occurred: " + status);
            }
        });


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();


        cardloc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                geofire.setLocation(uid, new GeoLocation(latitude, longitude), new GeoFire.CompletionListener() {
                    @Override
                    public void onComplete(String key, DatabaseError error) {
                        if (error != null) {
                            System.err.println("There was an error saving the location to GeoFire: " + error);
                        } else {
                            System.out.println("Location saved on server successfully!");
                        }
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("salons").child(uid).child("address");
                        ref.setValue(address);
                    }
                });
                Intent intent = new Intent(SelectLocation.this, SalonDashboard.class);
                startActivity(intent);
                finish();
            }
        });
    }
}


//   AIzaSyAcp0pkS_PGqCvAnqWaKj4xttJ0J-lHg-M

*/