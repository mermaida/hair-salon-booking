package com.example.aida.finalproj.Activities.SalonActivities;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aida.finalproj.Activities.Authentication.LoginActivity;
import com.example.aida.finalproj.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sangcomz.fishbun.FishBun;
import com.sangcomz.fishbun.adapter.image.impl.GlideAdapter;
import com.sangcomz.fishbun.define.Define;

import java.util.ArrayList;

public class SalonDashboard extends AppCompatActivity implements View.OnClickListener {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salon_dashboard);

        CardView btn1 = findViewById(R.id.btn1);
        CardView btn2 = findViewById(R.id.btn2);
        CardView btn3 = findViewById(R.id.btn3);
        CardView btn4 = findViewById(R.id.btn4);
        CardView btn5 = findViewById(R.id.btn5);
        TextView sgnout = findViewById(R.id.sgnout);
        sgnout.setOnClickListener(this);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btn1:
                Intent intent = new Intent(SalonDashboard.this, ServiceDisplay.class);
                startActivity(intent);
                break;
            case R.id.btn2:
                Intent intent2 = new Intent(SalonDashboard.this, ScheduleOverview.class);
                startActivity(intent2);
                break;
            case R.id.btn3:
                 Intent intent3 = new Intent(SalonDashboard.this, UpdateInfo.class);
                 startActivity(intent3);
                break;
            case R.id.btn4:
                Intent intent4 = new Intent(SalonDashboard.this, SalonGallery.class);
                startActivity(intent4);
                break;
            case R.id.btn5:
                Intent intent5 = new Intent(SalonDashboard.this, SelectLocation.class);
                startActivity(intent5);
                break;
            case R.id.sgnout:
                FirebaseAuth.getInstance().signOut();
                Intent intent6 = new Intent(SalonDashboard.this, LoginActivity.class);
                startActivity(intent6);
                finish();
                break;
        }
    }
}
