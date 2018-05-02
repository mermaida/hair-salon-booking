package com.example.aida.finalproj.Activities.UserActivities;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.aida.finalproj.Activities.SalonActivities.SalonGallery;
import com.example.aida.finalproj.Adapters.ImageGalleryAdapter;
import com.example.aida.finalproj.Adapters.ImageUserAdapter;
import com.example.aida.finalproj.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserGallery extends AppCompatActivity {

    RecyclerView imagelayout;
    List<Uri> urls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_gallery);

        String uid = getIntent().getStringExtra("id");
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("salons").child(uid).child("gallery");

        imagelayout = findViewById(R.id.imagedisplay);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        imagelayout.hasFixedSize();
        imagelayout.setLayoutManager(layoutManager);

        urls = new ArrayList<>();

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    String string_uri = childSnapshot.child("image_url").getValue(String.class);
                    Log.i("checkuri", "" + string_uri);
                    Uri image_url = Uri.parse(string_uri);
                    urls.add(image_url);
                }

                ImageUserAdapter imageAdapter = new ImageUserAdapter(UserGallery.this, urls);
                imagelayout.setAdapter(imageAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
