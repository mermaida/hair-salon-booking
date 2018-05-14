package com.example.aida.finalproj.Activities.Authentication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aida.finalproj.Activities.SalonActivities.SalonDashboard;
import com.example.aida.finalproj.Activities.UserActivities.UserDashboard;
import com.example.aida.finalproj.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class LoginActivity extends AppCompatActivity {

    CardView lgnbtn;
    TextView logmail, logpass, regbtn;
    ProgressDialog mDialog;
    private FirebaseAuth mAuth;
    SharedPreferences sharedpreferences;
    String uid, check;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       /* sharedpreferences = getSharedPreferences("pref", Context.MODE_PRIVATE);

        check = sharedpreferences.getString("login_id", "login not found");
        if (!check.equals("login not found")) {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("salons");
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild(check)) {
                        Intent intent = new Intent(LoginActivity.this, SalonDashboard.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(LoginActivity.this, UserDashboard.class);
                        startActivity(intent);
                        finish();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }*/

        mAuth = FirebaseAuth.getInstance();
        logmail = findViewById(R.id.logmail);
        logpass = findViewById(R.id.logpass);

        lgnbtn = findViewById(R.id.btn1);
        lgnbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                boolean isConnected = checkConnection();
                if (!isConnected)
                    Toast.makeText(LoginActivity.this, "İnternet bağlantısı yok", Toast.LENGTH_SHORT).show();
                else {
                    mDialog = new ProgressDialog(LoginActivity.this);
                    mDialog.setMessage("Lütfen bekleyiniz...");
                    mDialog.show();

                    String email = logmail.getText().toString();
                    final String password = logpass.getText().toString();

                    if (email.equals("") || password.equals("")) {
                        Toast.makeText(LoginActivity.this, "Lütfen boş alan bırakmayınız.", Toast.LENGTH_LONG).show();
                        mDialog.dismiss();
                    } else {

                        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {

                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();

                                    switch (errorCode) {

                                        case "ERROR_INVALID_EMAIL":
                                            Toast.makeText(LoginActivity.this, "Eposta yanlış", Toast.LENGTH_LONG).show();
                                            logmail.setError("Eposta yanlış");
                                            logmail.requestFocus();
                                            mDialog.dismiss();
                                            break;

                                        case "ERROR_WRONG_PASSWORD":
                                            Toast.makeText(LoginActivity.this, "Şifre yanlış", Toast.LENGTH_LONG).show();
                                            logpass.setError("Şifre yanlış");
                                            logpass.requestFocus();
                                            logpass.setText("");
                                            mDialog.dismiss();
                                            break;

                                        case "ERROR_USER_NOT_FOUND":
                                            Toast.makeText(LoginActivity.this, "Bu kullanıcı mevcut değil.", Toast.LENGTH_LONG).show();
                                            mDialog.dismiss();
                                            break;
                                    }

                                } else {
                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    uid = user.getUid();
                                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("salons");
                                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.hasChild(uid)) {
                                                //SharedPreferences.Editor editor = sharedpreferences.edit();
                                                //editor.putString("login_id", uid);
                                                //editor.commit();
                                                Intent intent = new Intent(LoginActivity.this, SalonDashboard.class);
                                                startActivity(intent);
                                                finish();
                                                mDialog.dismiss();
                                            } else {
                                                //SharedPreferences.Editor editor = sharedpreferences.edit();
                                                //editor.putString("login_id", uid);
                                                //editor.commit();
                                                Intent intent = new Intent(LoginActivity.this, UserDashboard.class);
                                                startActivity(intent);
                                                finish();
                                                mDialog.dismiss();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }
                            }
                        });
                    }
                }
            }
        });


        regbtn = findViewById(R.id.regbtn);
        regbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(getApplicationContext(), TypeActivity.class);
                startActivity(i);
            }
        });
    }

    public boolean checkConnection() {
        ConnectivityManager cm =
                (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        final boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        return isConnected;
    }
}
