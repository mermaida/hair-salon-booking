package com.example.aida.finalproj.Activities.SalonActivities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.aida.finalproj.Activities.Authentication.LoginActivity;
import com.example.aida.finalproj.Classes.Salon;
import com.example.aida.finalproj.Classes.Service;
import com.example.aida.finalproj.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class SalonRegistration extends AppCompatActivity {

    FirebaseAuth auth;
    EditText sname, sname2, sphone, smail, spass;
    CardView cardreg;
    CheckBox fmch, mch;
    String name, salon_name, phone, email, password;
    ProgressDialog mDialog;
    boolean fem, m;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sreg);

        auth = FirebaseAuth.getInstance();
        ref = FirebaseDatabase.getInstance().getReference();

        sname = findViewById(R.id.sname);
        sname2 = findViewById(R.id.sname2);
        sphone = findViewById(R.id.sphone);
        smail = findViewById(R.id.smail);
        spass = findViewById(R.id.spass);
        cardreg = findViewById(R.id.cardreg);

        fmch = findViewById(R.id.fmch);
        mch = findViewById(R.id.mch);


        cardreg.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                mDialog = new ProgressDialog(SalonRegistration.this);
                mDialog.setMessage("Please wait...");
                mDialog.show();

                name = sname.getText().toString();
                salon_name = sname2.getText().toString();
                phone = sphone.getText().toString();
                email = smail.getText().toString();
                password = spass.getText().toString();

                if (name.equals("") || salon_name.equals("") || phone.equals("") || email.equals("") || password.equals("")) {
                    mDialog.dismiss();
                    Toast.makeText(SalonRegistration.this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
                }

                else {

                    auth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(SalonRegistration.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser user = auth.getCurrentUser();
                                        String uid = user.getUid();

                                        if (fmch.isChecked()) {
                                            fem = true;
                                            m = false;
                                            writeNewUser(uid, name, salon_name, phone, email, password, fem, m);
                                        }
                                        if (mch.isChecked()) {
                                            fem = false;
                                            m = true;
                                            writeNewUser(uid, name, salon_name, phone, email, password, fem, m);
                                        }

                                        if (fmch.isChecked() && mch.isChecked()) {
                                            fem = true;
                                            m = true;
                                            writeNewUser(uid, name, salon_name, phone, email, password, fem, m);
                                        }

                                        Intent intent = new Intent(SalonRegistration.this, SelectLocation.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();

                                        switch (errorCode) {

                                            case "ERROR_INVALID_CUSTOM_TOKEN":
                                                Toast.makeText(SalonRegistration.this, "The custom token format is incorrect. Please check the documentation.", Toast.LENGTH_LONG).show();
                                                mDialog.dismiss();
                                                break;

                                            case "ERROR_CUSTOM_TOKEN_MISMATCH":
                                                Toast.makeText(SalonRegistration.this, "The custom token corresponds to a different audience.", Toast.LENGTH_LONG).show();
                                                mDialog.dismiss();
                                                break;

                                            case "ERROR_INVALID_CREDENTIAL":
                                                Toast.makeText(SalonRegistration.this, "The supplied auth credential is malformed or has expired.", Toast.LENGTH_LONG).show();
                                                mDialog.dismiss();
                                                break;

                                            case "ERROR_INVALID_EMAIL":
                                                Toast.makeText(SalonRegistration.this, "The email address is badly formatted.", Toast.LENGTH_LONG).show();
                                                smail.setError("The email address is badly formatted.");
                                                smail.requestFocus();
                                                mDialog.dismiss();
                                                break;

                                            case "ERROR_WRONG_PASSWORD":
                                                Toast.makeText(SalonRegistration.this, "The password is invalid or the user does not have a password.", Toast.LENGTH_LONG).show();
                                                spass.setError("Password is incorrect.");
                                                spass.requestFocus();
                                                spass.setText("");
                                                mDialog.dismiss();
                                                break;

                                            case "ERROR_USER_MISMATCH":
                                                Toast.makeText(SalonRegistration.this, "The supplied credentials do not correspond to the previously signed in user.", Toast.LENGTH_LONG).show();
                                                mDialog.dismiss();
                                                break;

                                            case "ERROR_REQUIRES_RECENT_LOGIN":
                                                Toast.makeText(SalonRegistration.this, "This operation is sensitive and requires recent authentication. Log in again before retrying this request.", Toast.LENGTH_LONG).show();
                                                mDialog.dismiss();
                                                break;

                                            case "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL":
                                                Toast.makeText(SalonRegistration.this, "An account already exists with the same email address but different sign-in credentials. Sign in using a provider associated with this email address.", Toast.LENGTH_LONG).show();
                                                mDialog.dismiss();
                                                break;

                                            case "ERROR_EMAIL_ALREADY_IN_USE":
                                                Toast.makeText(SalonRegistration.this, "The email address is already in use by another account.   ", Toast.LENGTH_LONG).show();
                                                smail.setError("The email address is already in use by another account.");
                                                smail.requestFocus();
                                                mDialog.dismiss();
                                                break;

                                            case "ERROR_CREDENTIAL_ALREADY_IN_USE":
                                                Toast.makeText(SalonRegistration.this, "This credential is already associated with a different user account.", Toast.LENGTH_LONG).show();
                                                mDialog.dismiss();
                                                break;

                                            case "ERROR_USER_DISABLED":
                                                Toast.makeText(SalonRegistration.this, "The user account has been disabled by an administrator.", Toast.LENGTH_LONG).show();
                                                mDialog.dismiss();
                                                break;

                                            case "ERROR_USER_TOKEN_EXPIRED":
                                                Toast.makeText(SalonRegistration.this, "The user\\'s credential is no longer valid. The user must sign in again.", Toast.LENGTH_LONG).show();
                                                mDialog.dismiss();
                                                break;

                                            case "ERROR_USER_NOT_FOUND":
                                                Toast.makeText(SalonRegistration.this, "There is no user record corresponding to this identifier. The user may have been deleted.", Toast.LENGTH_LONG).show();
                                                mDialog.dismiss();
                                                break;

                                            case "ERROR_INVALID_USER_TOKEN":
                                                Toast.makeText(SalonRegistration.this, "The user\\'s credential is no longer valid. The user must sign in again.", Toast.LENGTH_LONG).show();
                                                mDialog.dismiss();
                                                break;

                                            case "ERROR_OPERATION_NOT_ALLOWED":
                                                Toast.makeText(SalonRegistration.this, "This operation is not allowed. You must enable this service in the console.", Toast.LENGTH_LONG).show();
                                                mDialog.dismiss();
                                                break;

                                            case "ERROR_WEAK_PASSWORD":
                                                Toast.makeText(SalonRegistration.this, "The given password is invalid.", Toast.LENGTH_LONG).show();
                                                spass.setError("Password must be at least 6 characters.");
                                                spass.requestFocus();
                                                mDialog.dismiss();
                                                break;


                                        }
                                    }
                                }
                            });
                }
            }
        });
    }


    private void writeNewUser(String uid, String name, String salon_name, String phone, String email, String password, boolean fem, boolean m) {
        Salon salon = new Salon(name, salon_name, phone, email, password, "", 3, fem, m);

        String image1 = "https://firebasestorage.googleapis.com/v0/b/finalproj-4653f.appspot.com/o/services%2Fwash.png?alt=media&token=2a0ce69c-fd11-4538-85ad-745c40ca46b6";
        String image2 = "https://firebasestorage.googleapis.com/v0/b/finalproj-4653f.appspot.com/o/services%2Fhaircut.png?alt=media&token=141352c6-96f7-4850-be08-5cb2411f857b";
        String image3 = "https://firebasestorage.googleapis.com/v0/b/finalproj-4653f.appspot.com/o/services%2Fwash.png?alt=media&token=2a0ce69c-fd11-4538-85ad-745c40ca46b6";
        String image4 = "https://firebasestorage.googleapis.com/v0/b/finalproj-4653f.appspot.com/o/services%2Fextension.png?alt=media&token=a31660d2-1527-43ba-9913-f472e55066b0";
        String image5 = "https://firebasestorage.googleapis.com/v0/b/finalproj-4653f.appspot.com/o/services%2Fstraight.png?alt=media&token=5639d6a4-7496-4ba3-bc3c-353d95d6ebf8";
        String image6 = "https://firebasestorage.googleapis.com/v0/b/finalproj-4653f.appspot.com/o/services%2Ftreatment.png?alt=media&token=28377710-f04a-436a-8483-2bd4a6eacda7";
        String image7 = "https://firebasestorage.googleapis.com/v0/b/finalproj-4653f.appspot.com/o/services%2Fevent.png?alt=media&token=9cc9eb77-ae6f-451f-a070-b4b165384f3f";
        String image8 = "https://firebasestorage.googleapis.com/v0/b/finalproj-4653f.appspot.com/o/services%2Fmakeup.png?alt=media&token=d8508ceb-40ca-45cb-9c93-96d839a33f90";
        String image9 = "https://firebasestorage.googleapis.com/v0/b/finalproj-4653f.appspot.com/o/services%2Ftrim.png?alt=media&token=fcde8c7b-9d69-4014-8d8c-66cee3a656fd";
        String image10 = "https://firebasestorage.googleapis.com/v0/b/finalproj-4653f.appspot.com/o/services%2Fbeard.png?alt=media&token=c942c21b-e253-40a2-acec-142650041a58";



        Map<String, Service> map = new HashMap<>();
        map.put("s0", new Service("Wash", 0, 0, false, image1));
        map.put("s1", new Service("Cut", 0, 0, false, image2));
        map.put("s2", new Service("Dye", 0, 0, false, image3));
        map.put("s3", new Service("Extensions", 0, 0, false, image4));
        map.put("s4", new Service("Straight", 0, 0, false, image5));
        map.put("s5", new Service("Treatment", 0, 0, false, image6));
        map.put("s6", new Service("Event", 0, 0, false, image7));
        map.put("s7", new Service("Makeup", 0, 0, false, image8));
        map.put("s8", new Service("Trim", 0, 0, false, image9));
        map.put("s9", new Service("Beard", 0, 0, false, image10));

        ref.child("salons").child(uid).setValue(salon);
        ref.child("salons").child(uid).child("services").setValue(map);

    }
}
