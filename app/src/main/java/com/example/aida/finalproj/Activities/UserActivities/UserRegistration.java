package com.example.aida.finalproj.Activities.UserActivities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.aida.finalproj.Activities.Authentication.LoginActivity;
import com.example.aida.finalproj.Activities.SalonActivities.SalonRegistration;
import com.example.aida.finalproj.Classes.User;
import com.example.aida.finalproj.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Aida on 24.02.2018..
 */

public class UserRegistration extends FragmentActivity {

    private FirebaseAuth mAuth;
    EditText cname, cphone, cmail, cpass;
    CardView cardreg;
    private LoginButton fbbtn;
    private DatabaseReference mDatabase;
    CallbackManager mCallbackManager;
    ProgressDialog mDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creg);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        cname = findViewById(R.id.cname);
        cphone = findViewById(R.id.cphone);
        cmail = findViewById(R.id.cmail);
        cpass = findViewById(R.id.cpass);
        cardreg = findViewById(R.id.cardreg);

        cardreg.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                boolean isConnected = checkConnection();
                if (!isConnected)
                    Toast.makeText(UserRegistration.this, "İnternet bağlantısı yok", Toast.LENGTH_SHORT).show();
                else {

                    mDialog = new ProgressDialog(UserRegistration.this);
                    mDialog.setMessage("Lütfen bekleyiniz...");
                    mDialog.show();

                    final String name = cname.getText().toString();
                    final String phone = cphone.getText().toString();
                    final String email = cmail.getText().toString();
                    final String password = cpass.getText().toString();

                    if (name.equals("") || phone.equals("") || email.equals("") || password.equals("")) {
                        mDialog.dismiss();
                        Toast.makeText(UserRegistration.this, "Lütfen boş alan bırakmayınız", Toast.LENGTH_SHORT).show();
                    } else {

                        mAuth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(UserRegistration.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            FirebaseUser user = mAuth.getCurrentUser();
                                            String uid = user.getUid();
                                            writeNewUser(uid, name, phone, email, password);
                                            mDialog.dismiss();
                                            Intent intent = new Intent(UserRegistration.this, UserDashboard.class);
                                            startActivity(intent);
                                            finish();
                                        } else {

                                            String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();

                                            switch (errorCode) {

                                                case "ERROR_INVALID_EMAIL":
                                                    Toast.makeText(UserRegistration.this, "Lütfen eposta formati kullanınız.", Toast.LENGTH_LONG).show();
                                                    cmail.setError("Lütfen eposta formati kullanınız.");
                                                    cmail.requestFocus();
                                                    mDialog.dismiss();
                                                    break;


                                                case "ERROR_EMAIL_ALREADY_IN_USE":
                                                    Toast.makeText(UserRegistration.this, "E-posta adresi zaten mevcut", Toast.LENGTH_LONG).show();
                                                    cmail.setError("E-posta adresi zaten mevcut");
                                                    cmail.requestFocus();
                                                    mDialog.dismiss();
                                                    break;


                                                case "ERROR_WEAK_PASSWORD":
                                                    Toast.makeText(UserRegistration.this, "Şifre en az 6 karakter içermeli", Toast.LENGTH_LONG).show();
                                                    cpass.setError("Şifre en az 6 karakter içermeli");
                                                    cpass.requestFocus();
                                                    mDialog.dismiss();
                                                    break;
                                            }
                                        }
                                    }
                                });
                    }
                }
            }
        });
    }


       /* mCallbackManager = CallbackManager.Factory.create();
        fbbtn = findViewById(R.id.fblogin);

        fbbtn.setReadPermissions("email", "public_profile");
        fbbtn.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                // Log.d(TAG, "facebook:onCancel");
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                //  Log.d(TAG, "facebook:onError", error);
                // ...
            }
        });
    }*/

        /*@Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            // Pass the activity result back to the Facebook SDK
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }

    }*/

    private void writeNewUser(String uid, String name, String phone, String email, String password) {
        User user = new User(name, phone, email, password);

        mDatabase.child("users").child(uid).setValue(user);
    }

    /*private void handleFacebookAccessToken(AccessToken token) {
      //  Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                          //  Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                        } else {
                            // If sign in fails, display a message to the user.
                            // Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(UserRegistration.this, "Registration failed.",
                                    Toast.LENGTH_SHORT).show();


                        }

                        // ...
                    }
                });
    }*/

    public boolean checkConnection() {
        ConnectivityManager cm =
                (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        final boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        return isConnected;
    }
}
