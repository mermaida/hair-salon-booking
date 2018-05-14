package com.example.aida.finalproj.Activities.SalonActivities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.aida.finalproj.Activities.Authentication.LoginActivity;
import com.example.aida.finalproj.Activities.UserActivities.UserRegistration;
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

                boolean isConnected = checkConnection();
                if (!isConnected)
                    Toast.makeText(SalonRegistration.this, "İnternet bağlantısı yok", Toast.LENGTH_SHORT).show();
                else {

                    mDialog = new ProgressDialog(SalonRegistration.this);
                    mDialog.setMessage("Lütfen bekleyiniz...");
                    mDialog.show();

                    name = sname.getText().toString();
                    salon_name = sname2.getText().toString();
                    phone = sphone.getText().toString();
                    email = smail.getText().toString();
                    password = spass.getText().toString();

                    if (name.equals("") || salon_name.equals("") || phone.equals("") || email.equals("") || password.equals("")) {
                        mDialog.dismiss();
                        Toast.makeText(SalonRegistration.this, "Lütfen boş alan bırakmayınız.", Toast.LENGTH_SHORT).show();
                    } else {

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

                                            mDialog.dismiss();
                                            Intent intent = new Intent(SalonRegistration.this, SelectLocation.class);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();

                                            switch (errorCode) {


                                                case "ERROR_INVALID_EMAIL":
                                                    Toast.makeText(SalonRegistration.this, "Lütfen eposta formati kullanınız.", Toast.LENGTH_LONG).show();
                                                    smail.setError("Lütfen eposta formati kullanınız.");
                                                    smail.requestFocus();
                                                    mDialog.dismiss();
                                                    break;


                                                case "ERROR_EMAIL_ALREADY_IN_USE":
                                                    Toast.makeText(SalonRegistration.this, "E-posta adresi zaten mevcut", Toast.LENGTH_LONG).show();
                                                    smail.setError("E-posta adresi zaten mevcut");
                                                    smail.requestFocus();
                                                    mDialog.dismiss();
                                                    break;


                                                case "ERROR_WEAK_PASSWORD":
                                                    Toast.makeText(SalonRegistration.this, "Şifre en az 6 karakter içermeli", Toast.LENGTH_LONG).show();
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
        map.put("s0", new Service("Yıkama", 0, 0, false, image1));
        map.put("s1", new Service("Kesim", 0, 0, false, image2));
        map.put("s2", new Service("Boyama", 0, 0, false, image3));
        map.put("s3", new Service("Saç ekleme", 0, 0, false, image4));
        map.put("s4", new Service("Fön", 0, 0, false, image5));
        map.put("s5", new Service("Bakım", 0, 0, false, image6));
        map.put("s6", new Service("Özel gün", 0, 0, false, image7));
        map.put("s7", new Service("Makyaj", 0, 0, false, image8));
        map.put("s8", new Service("Erkek saç", 0, 0, false, image9));
        map.put("s9", new Service("Sakal", 0, 0, false, image10));

        ref.child("salons").child(uid).setValue(salon);
        ref.child("salons").child(uid).child("services").setValue(map);

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
