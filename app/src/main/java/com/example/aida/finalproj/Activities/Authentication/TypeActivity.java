package com.example.aida.finalproj.Activities.Authentication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.example.aida.finalproj.R;
import com.example.aida.finalproj.Activities.SalonActivities.SalonRegistration;
import com.example.aida.finalproj.Activities.UserActivities.UserRegistration;

/**
 * Created by Aida on 24.02.2018..
 */

public class TypeActivity extends AppCompatActivity {

    ImageView img_cust, img_own;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type);

        img_cust = findViewById(R.id.iconcust);
        img_cust.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(getApplicationContext(), UserRegistration.class);
                startActivity(i);
            }
        });

        img_own = findViewById(R.id.iconown);
        img_own.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(getApplicationContext(), SalonRegistration.class);
                startActivity(i);
            }
        });
    }
}
