package com.example.aida.finalproj.Activities.SalonActivities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.example.aida.finalproj.Activities.UserActivities.EditUserProfile;
import com.example.aida.finalproj.Activities.UserActivities.UserProfile;
import com.example.aida.finalproj.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UpdateInfo extends AppCompatActivity {

    EditText sname, sname2, sphone, smail, spass;
    TextView cancel;
    CardView cardschange;
    CheckBox fmch, mch;
    String uid;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_info);

        sname = findViewById(R.id.sname);
        sname2 = findViewById(R.id.sname2);
        sphone = findViewById(R.id.sphone);
        smail = findViewById(R.id.smail);
        spass = findViewById(R.id.spass);
        cardschange = findViewById(R.id.cardschange);

        fmch = findViewById(R.id.fmch);
        mch = findViewById(R.id.mch);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        ref = FirebaseDatabase.getInstance().getReference().child("salons").child(uid);

        cardschange.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                final String name = sname.getText().toString();
                final String salon_name = sname2.getText().toString();
                final String phone = sphone.getText().toString();
                final String email = smail.getText().toString();
                final String password = spass.getText().toString();

                if (name.equals(""))
                    ref.child("name").setValue(name);
                if (salon_name.equals(""))
                    ref.child("salon_name").setValue(name);
                if (phone.equals(""))
                    ref.child("phone").setValue(phone);
                if (email.equals(""))
                    ref.child("email").setValue(email);
                if (password.equals(""))
                    ref.child("password").setValue(password);


                if (fmch.isChecked() && mch.isChecked()) {
                    ref.child("female").setValue(true);
                    ref.child("male").setValue(true);
                }

                else if (fmch.isChecked()) {
                    ref.child("female").setValue(true);
                    ref.child("male").setValue(false);
                }
                else if (mch.isChecked()) {
                    ref.child("female").setValue(false);
                    ref.child("male").setValue(true);
                }

                Intent intent = new Intent(UpdateInfo.this, SalonDashboard.class);
                startActivity(intent);
                finish();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UpdateInfo.this, SalonDashboard.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
