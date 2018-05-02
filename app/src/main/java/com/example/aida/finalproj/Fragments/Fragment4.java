package com.example.aida.finalproj.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.aida.finalproj.Activities.Authentication.LoginActivity;
import com.example.aida.finalproj.Activities.UserActivities.EditUserProfile;
import com.example.aida.finalproj.Activities.UserActivities.UserProfile;
import com.example.aida.finalproj.Classes.Salon;
import com.example.aida.finalproj.Classes.User;
import com.example.aida.finalproj.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by Aida on 23.04.2018..
 */

public class Fragment4 extends Fragment {

    TextView name, email, phone, signout;
    DatabaseReference ref;
    User displayedUser;
    Button editbtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_4, container, false);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();

        ref = FirebaseDatabase.getInstance().getReference().child("users").child(uid);

        name = view.findViewById(R.id.username);
        phone = view.findViewById(R.id.userphone);
        email = view.findViewById(R.id.useremail);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("child", "PARENT: " + dataSnapshot.getKey());
                Log.d("child2", "" + dataSnapshot.child("name").getValue(String.class));

                displayedUser = dataSnapshot.getValue(User.class);

                    /*String key = dataSnapshot.getKey();
                    Log.i("parent id", "" + key);*/

                name.setText(displayedUser.getName());
                phone.setText("Phone: " + displayedUser.getPhone());
                email.setText("Email: " + displayedUser.getEmail());

            }


              /*  book = getView().findViewById(R.id.bookapt);
                book.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), BookAppointment.class).putExtra("id", keys.get(0));
                        startActivity(intent);
                    }
                });*/


            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e("errortag", "Failed to read app title value.", error.toException());
            }
        });

        editbtn = view.findViewById(R.id.editbtn);
        editbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EditUserProfile.class);
                startActivity(intent);
            }
        });

        signout = view.findViewById(R.id.signoutbtn);
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        return view;
    }
}
