package com.example.aida.finalproj.Activities.UserActivities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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

                mDialog = new ProgressDialog(UserRegistration.this);
                mDialog.setMessage("Please wait...");
                mDialog.show();

                final String name = cname.getText().toString();
                final String phone = cphone.getText().toString();
                final String email = cmail.getText().toString();
                final String password = cpass.getText().toString();

                if (name.equals("") || phone.equals("") || email.equals("") || password.equals("")) {
                    mDialog.dismiss();
                    Toast.makeText(UserRegistration.this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
                }

                else {

                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(UserRegistration.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        String uid = user.getUid();
                                        writeNewUser(uid, name, phone, email, password);
                                        Intent intent = new Intent(UserRegistration.this, UserDashboard.class);
                                        startActivity(intent);
                                        finish();
                                    } else {

                                        String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();

                                        switch (errorCode) {

                                            case "ERROR_INVALID_CUSTOM_TOKEN":
                                                Toast.makeText(UserRegistration.this, "The custom token format is incorrect. Please check the documentation.", Toast.LENGTH_LONG).show();
                                                mDialog.dismiss();
                                                break;

                                            case "ERROR_CUSTOM_TOKEN_MISMATCH":
                                                Toast.makeText(UserRegistration.this, "The custom token corresponds to a different audience.", Toast.LENGTH_LONG).show();
                                                mDialog.dismiss();
                                                break;

                                            case "ERROR_INVALID_CREDENTIAL":
                                                Toast.makeText(UserRegistration.this, "The supplied auth credential is malformed or has expired.", Toast.LENGTH_LONG).show();
                                                mDialog.dismiss();
                                                break;

                                            case "ERROR_INVALID_EMAIL":
                                                Toast.makeText(UserRegistration.this, "The email address is badly formatted.", Toast.LENGTH_LONG).show();
                                                cmail.setError("The email address is badly formatted.");
                                                cmail.requestFocus();
                                                mDialog.dismiss();
                                                break;

                                            case "ERROR_WRONG_PASSWORD":
                                                Toast.makeText(UserRegistration.this, "The password is invalid or the user does not have a password.", Toast.LENGTH_LONG).show();
                                                cpass.setError("password is incorrect ");
                                                cpass.requestFocus();
                                                cpass.setText("");
                                                mDialog.dismiss();
                                                break;

                                            case "ERROR_USER_MISMATCH":
                                                Toast.makeText(UserRegistration.this, "The supplied credentials do not correspond to the previously signed in user.", Toast.LENGTH_LONG).show();
                                                mDialog.dismiss();
                                                break;

                                            case "ERROR_REQUIRES_RECENT_LOGIN":
                                                Toast.makeText(UserRegistration.this, "This operation is sensitive and requires recent authentication. Log in again before retrying this request.", Toast.LENGTH_LONG).show();
                                                mDialog.dismiss();
                                                break;

                                            case "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL":
                                                Toast.makeText(UserRegistration.this, "An account already exists with the same email address but different sign-in credentials. Sign in using a provider associated with this email address.", Toast.LENGTH_LONG).show();
                                                mDialog.dismiss();
                                                break;

                                            case "ERROR_EMAIL_ALREADY_IN_USE":
                                                Toast.makeText(UserRegistration.this, "The email address is already in use by another account.   ", Toast.LENGTH_LONG).show();
                                                cmail.setError("The email address is already in use by another account.");
                                                cmail.requestFocus();
                                                mDialog.dismiss();
                                                break;

                                            case "ERROR_CREDENTIAL_ALREADY_IN_USE":
                                                Toast.makeText(UserRegistration.this, "This credential is already associated with a different user account.", Toast.LENGTH_LONG).show();
                                                mDialog.dismiss();
                                                break;

                                            case "ERROR_USER_DISABLED":
                                                Toast.makeText(UserRegistration.this, "The user account has been disabled by an administrator.", Toast.LENGTH_LONG).show();
                                                mDialog.dismiss();
                                                break;

                                            case "ERROR_USER_TOKEN_EXPIRED":
                                                Toast.makeText(UserRegistration.this, "The user\\'s credential is no longer valid. The user must sign in again.", Toast.LENGTH_LONG).show();
                                                mDialog.dismiss();
                                                break;

                                            case "ERROR_USER_NOT_FOUND":
                                                Toast.makeText(UserRegistration.this, "There is no user record corresponding to this identifier. The user may have been deleted.", Toast.LENGTH_LONG).show();
                                                mDialog.dismiss();
                                                break;

                                            case "ERROR_INVALID_USER_TOKEN":
                                                Toast.makeText(UserRegistration.this, "The user\\'s credential is no longer valid. The user must sign in again.", Toast.LENGTH_LONG).show();
                                                mDialog.dismiss();
                                                break;

                                            case "ERROR_OPERATION_NOT_ALLOWED":
                                                Toast.makeText(UserRegistration.this, "This operation is not allowed. You must enable this service in the console.", Toast.LENGTH_LONG).show();
                                                mDialog.dismiss();
                                                break;

                                            case "ERROR_WEAK_PASSWORD":
                                                Toast.makeText(UserRegistration.this, "The given password is invalid.", Toast.LENGTH_LONG).show();
                                                cpass.setError("The password is invalid it must 6 characters at least");
                                                cpass.requestFocus();
                                                mDialog.dismiss();
                                                break;
                                        }
                                    }
                                }
                            });
                }
            }
        });

        mCallbackManager = CallbackManager.Factory.create();
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
    }

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

    private void handleFacebookAccessToken(AccessToken token) {
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
    }
}
