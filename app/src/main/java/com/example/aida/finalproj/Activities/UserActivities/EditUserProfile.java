package com.example.aida.finalproj.Activities.UserActivities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.aida.finalproj.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditUserProfile extends AppCompatActivity {

    EditText cname, cphone, cmail, cpass;
    CardView cardchange;
    TextView cancel;
    String uid;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_profile);

        cname = findViewById(R.id.editcname);
        cphone = findViewById(R.id.editcphone);
        cmail = findViewById(R.id.editcmail);
        cpass = findViewById(R.id.editcpass);

        cardchange = findViewById(R.id.cardchange);

        cancel = findViewById(R.id.cancelbtn);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        ref = FirebaseDatabase.getInstance().getReference().child("users").child(uid);

        cardchange.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                final String name = cname.getText().toString();
                final String phone = cphone.getText().toString();
                final String email = cmail.getText().toString();
                final String password = cpass.getText().toString();

                if (name.equals(""))
                    ref.child("name").setValue(name);
                if (phone.equals(""))
                    ref.child("phone").setValue(phone);
                if (email.equals(""))
                    ref.child("email").setValue(email);
                if (password.equals(""))
                    ref.child("password").setValue(password);

                Intent intent = new Intent(EditUserProfile.this, UserProfile.class);
                startActivity(intent);
                finish();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditUserProfile.this, UserProfile.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
