package com.example.aida.finalproj.Activities.Authentication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
                mDialog = new ProgressDialog(LoginActivity.this);
                mDialog.setMessage("Please wait...");
                mDialog.show();

                String email = logmail.getText().toString();
                final String password = logpass.getText().toString();

                if(email.equals("") || password.equals("")) {
                    Toast.makeText(LoginActivity.this, "Please fill out both fields.", Toast.LENGTH_LONG).show();
                    mDialog.dismiss();
                }

                else {

                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {

                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();

                                switch (errorCode) {

                                    case "ERROR_INVALID_CUSTOM_TOKEN":
                                        Toast.makeText(LoginActivity.this, "The custom token format is incorrect. Please check the documentation.", Toast.LENGTH_LONG).show();
                                        mDialog.dismiss();
                                        break;

                                    case "ERROR_CUSTOM_TOKEN_MISMATCH":
                                        Toast.makeText(LoginActivity.this, "The custom token corresponds to a different audience.", Toast.LENGTH_LONG).show();
                                        mDialog.dismiss();
                                        break;

                                    case "ERROR_INVALID_CREDENTIAL":
                                        Toast.makeText(LoginActivity.this, "The supplied auth credential is malformed or has expired.", Toast.LENGTH_LONG).show();
                                        mDialog.dismiss();
                                        break;

                                    case "ERROR_INVALID_EMAIL":
                                        Toast.makeText(LoginActivity.this, "The email address is badly formatted.", Toast.LENGTH_LONG).show();
                                        logmail.setError("The email address is badly formatted.");
                                        logmail.requestFocus();
                                        mDialog.dismiss();
                                        break;

                                    case "ERROR_WRONG_PASSWORD":
                                        Toast.makeText(LoginActivity.this, "The password is invalid or the user does not have a password.", Toast.LENGTH_LONG).show();
                                        logpass.setError("password is incorrect ");
                                        logpass.requestFocus();
                                        logpass.setText("");
                                        mDialog.dismiss();
                                        break;

                                    case "ERROR_USER_MISMATCH":
                                        Toast.makeText(LoginActivity.this, "The supplied credentials do not correspond to the previously signed in user.", Toast.LENGTH_LONG).show();
                                        mDialog.dismiss();
                                        break;

                                    case "ERROR_REQUIRES_RECENT_LOGIN":
                                        Toast.makeText(LoginActivity.this, "This operation is sensitive and requires recent authentication. Log in again before retrying this request.", Toast.LENGTH_LONG).show();
                                        mDialog.dismiss();
                                        break;

                                    case "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL":
                                        Toast.makeText(LoginActivity.this, "An account already exists with the same email address but different sign-in credentials. Sign in using a provider associated with this email address.", Toast.LENGTH_LONG).show();
                                        mDialog.dismiss();
                                        break;

                                    case "ERROR_EMAIL_ALREADY_IN_USE":
                                        Toast.makeText(LoginActivity.this, "The email address is already in use by another account.   ", Toast.LENGTH_LONG).show();
                                        logmail.setError("The email address is already in use by another account.");
                                        logmail.requestFocus();
                                        mDialog.dismiss();
                                        break;

                                    case "ERROR_CREDENTIAL_ALREADY_IN_USE":
                                        Toast.makeText(LoginActivity.this, "This credential is already associated with a different user account.", Toast.LENGTH_LONG).show();
                                        mDialog.dismiss();
                                        break;

                                    case "ERROR_USER_DISABLED":
                                        Toast.makeText(LoginActivity.this, "The user account has been disabled by an administrator.", Toast.LENGTH_LONG).show();
                                        mDialog.dismiss();
                                        break;

                                    case "ERROR_USER_TOKEN_EXPIRED":
                                        Toast.makeText(LoginActivity.this, "The user\\'s credential is no longer valid. The user must sign in again.", Toast.LENGTH_LONG).show();
                                        mDialog.dismiss();
                                        break;

                                    case "ERROR_USER_NOT_FOUND":
                                        Toast.makeText(LoginActivity.this, "There is no user record corresponding to this identifier. The user may have been deleted.", Toast.LENGTH_LONG).show();
                                        mDialog.dismiss();
                                        break;

                                    case "ERROR_INVALID_USER_TOKEN":
                                        Toast.makeText(LoginActivity.this, "The user\\'s credential is no longer valid. The user must sign in again.", Toast.LENGTH_LONG).show();
                                        mDialog.dismiss();
                                        break;

                                    case "ERROR_OPERATION_NOT_ALLOWED":
                                        Toast.makeText(LoginActivity.this, "This operation is not allowed. You must enable this service in the console.", Toast.LENGTH_LONG).show();
                                        mDialog.dismiss();
                                        break;

                                    case "ERROR_WEAK_PASSWORD":
                                        Toast.makeText(LoginActivity.this, "The given password is invalid.", Toast.LENGTH_LONG).show();
                                        logpass.setError("The password is invalid it must 6 characters at least");
                                        logpass.requestFocus();
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
}
