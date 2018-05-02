package com.example.aida.finalproj.Activities.UserActivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.example.aida.finalproj.Activities.Authentication.LoginActivity;
import com.example.aida.finalproj.R;

/**
 * Created by Aida on 24.02.2018..
 */

public class GenderActivity extends AppCompatActivity {

    CheckBox ch1, ch2;
    Button btn;
    String gnd;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gender);

        ch1 = findViewById(R.id.chf);
        ch2 = findViewById(R.id.chm);

        btn = findViewById(R.id.cont);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ch1.isChecked() && ch2.isChecked()) {
                    intent = new Intent(GenderActivity.this, UserDashboard.class);
                }
                else if (ch1.isChecked()) {
                    gnd = "female";
                    intent = new Intent(GenderActivity.this, UserDashboard.class).putExtra("gender", gnd);
                }
                else if (ch2.isChecked()) {
                    gnd = "male";
                    intent = new Intent(GenderActivity.this, UserDashboard.class).putExtra("gender", gnd);
                }
                startActivity(intent);
            }
        });

    }
}
