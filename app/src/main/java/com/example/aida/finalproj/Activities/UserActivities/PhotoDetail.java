package com.example.aida.finalproj.Activities.UserActivities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.aida.finalproj.Activities.SalonActivities.SalonGallery;
import com.example.aida.finalproj.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class PhotoDetail extends AppCompatActivity {

    public static final String EXTRA_PHOTO = "PhotoDetail.PHOTO";
    private ImageView image;
    Button remove;
    String uid;
    DatabaseReference ref;
    Query query;
    Uri photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);

        image = findViewById(R.id.image);
        photo = getIntent().getParcelableExtra(EXTRA_PHOTO);

        Glide.with(this).load(photo).apply(RequestOptions.centerCropTransform()).into(image);
    }
}

/*holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(position != RecyclerView.NO_POSITION) {
                    Intent intent = new Intent(getApplicationContext(), PhotoDetail.class);
                    intent.putExtra(PhotoDetail.EXTRA_PHOTO, urls.get(position));
                    getApplicationContext().startActivity(intent);
                    activity.finish();
                }
            }
        });*/
