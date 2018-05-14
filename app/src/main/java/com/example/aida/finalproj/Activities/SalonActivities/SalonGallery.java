package com.example.aida.finalproj.Activities.SalonActivities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.aida.finalproj.Activities.UserActivities.UserRegistration;
import com.example.aida.finalproj.R;
import com.example.aida.finalproj.Adapters.ImageGalleryAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sangcomz.fishbun.FishBun;
import com.sangcomz.fishbun.adapter.image.impl.GlideAdapter;
import com.sangcomz.fishbun.define.Define;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SalonGallery extends AppCompatActivity {

    Button takephoto, opengallery;
    RecyclerView imagelayout;
    UploadTask uploadTask;
    StorageReference storageRef;
    DatabaseReference ref, ref2;
    List<Uri> uris;
    String uid;
    File file;
    Uri fileUri;
    ProgressDialog dialog;
    static ImageGalleryAdapter imageAdapter;
    public static final int CAMERA_PIC_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salon_gallery);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        ref2 = FirebaseDatabase.getInstance().getReference().child("salons").child(uid).child("gallery");

        takephoto = findViewById(R.id.takephoto);
        opengallery = findViewById(R.id.opengallery);

        takephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());
                Intent photo = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                file = new File(getApplicationContext().getExternalCacheDir(),
                        String.valueOf(System.currentTimeMillis()) + ".jpg");
                fileUri = Uri.fromFile(file);
                photo.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                if (photo.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(photo, CAMERA_PIC_REQUEST);
                }
            }
        });

        opengallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FishBun.with(SalonGallery.this)
                        .setImageAdapter(new GlideAdapter())
                        .startAlbum();
            }
        });

        imagelayout = findViewById(R.id.images);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        imagelayout.hasFixedSize();
        imagelayout.setLayoutManager(layoutManager);

        uris = new ArrayList<>();

        ref2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    String string_uri = childSnapshot.child("image_url").getValue(String.class);
                    Log.i("checkuri", "" + string_uri);
                    Uri image_url = Uri.parse(string_uri);
                    uris.add(image_url);
                }

                imageAdapter = new ImageGalleryAdapter(SalonGallery.this, uris);
                imagelayout.setAdapter(imageAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent imageData) {
        super.onActivityResult(requestCode, resultCode, imageData);
        switch (requestCode) {
            case Define.ALBUM_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    // path = imageData.getStringArrayListExtra(Define.INTENT_PATH);
                    // you can get an image path(ArrayList<String>) on <0.6.2

                    boolean isConnected = checkConnection();
                    if (!isConnected)
                        Toast.makeText(SalonGallery.this, "İnternet bağlantısı yok", Toast.LENGTH_SHORT).show();
                    else {

                        ArrayList<Uri> path = imageData.getParcelableArrayListExtra(Define.INTENT_PATH);
                        Log.i("listsize", "" + path.size());
                        // you can get an image path(ArrayList<Uri>) on 0.6.2 and later

                        storageRef = FirebaseStorage.getInstance().getReference();

                        dialog = new ProgressDialog(SalonGallery.this);
                        dialog.setMessage("Lütfen bekleyiniz...");
                        dialog.show();

                        for (int i = 0; i < path.size(); i++) {

                            Uri selectedImage = path.get(i);

                            StorageReference testref = storageRef.child("images/" + selectedImage.getLastPathSegment());
                            Log.i("imagepath", "" + selectedImage + " lastpathsegment " + selectedImage.getLastPathSegment());
                            uploadTask = testref.putFile(selectedImage);

                            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    ref = FirebaseDatabase.getInstance().getReference().child("salons").child(uid).child("gallery").push().child("image_url");
                                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                                    ref.setValue(downloadUrl.toString());
                                    uris = new ArrayList<>();
                                    ref2.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                                String string_uri = childSnapshot.child("image_url").getValue(String.class);
                                                Log.i("checkuri", "" + string_uri);
                                                Uri image_url = Uri.parse(string_uri);
                                                uris.add(image_url);
                                            }

                                            imageAdapter = new ImageGalleryAdapter(SalonGallery.this, uris);
                                            imagelayout.setAdapter(imageAdapter);

                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                    dialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "Resim başarıyla yüklendi!", Toast.LENGTH_SHORT).show();
                                }

                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    dialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "Yükleme başarısız.", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }
                break;

            case CAMERA_PIC_REQUEST:
                if (resultCode == RESULT_OK) {

                    boolean isConnected = checkConnection();
                    if (!isConnected)
                        Toast.makeText(SalonGallery.this, "İnternet bağlantısı yok", Toast.LENGTH_SHORT).show();
                    else {

                        dialog = new ProgressDialog(SalonGallery.this);
                        dialog.setMessage("Lütfen bekleyiniz...");
                        dialog.show();

                        storageRef = FirebaseStorage.getInstance().getReference();

                        StorageReference testref = storageRef.child("images/" + fileUri.getLastPathSegment());
                        Log.i("imagepath", "" + fileUri + " lastpathsegment " + fileUri.getLastPathSegment());
                        uploadTask = testref.putFile(fileUri);

                        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                ref = FirebaseDatabase.getInstance().getReference().child("salons").child(uid).child("gallery").push().child("image_url");
                                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                                ref.setValue(downloadUrl.toString());
                                Toast.makeText(getApplicationContext(), "Resim başarıyla yüklendi!", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                SalonGallery.this.finish();
                                overridePendingTransition(0, 0);
                                startActivity(SalonGallery.this.getIntent());
                                overridePendingTransition(0, 0);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                Toast.makeText(getApplicationContext(), "Yükleme başarısız.", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        });

                    }
                }
                break;
        }
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
