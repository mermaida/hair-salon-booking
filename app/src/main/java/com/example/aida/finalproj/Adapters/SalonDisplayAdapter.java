package com.example.aida.finalproj.Adapters;

/**
 * Created by Aida on 11.04.2018..
 */

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.aida.finalproj.Activities.Authentication.LoginActivity;
import com.example.aida.finalproj.Classes.DatabaseHelper;
import com.example.aida.finalproj.Classes.Service;
import com.example.aida.finalproj.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

public class SalonDisplayAdapter extends RecyclerView.Adapter<SalonDisplayAdapter.ViewHolder> {

    Activity activity;
    List<Service> services;
    private DatabaseHelper db;
    ImageView image;

    public SalonDisplayAdapter(Activity activity, List<Service> services){
        this.activity = activity;
        this.services = services;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.name.setText(services.get(position).getService_name());
        holder.price.setText(Double.toString(services.get(position).getPrice()));
        holder.duration.setText(Double.toString(services.get(position).getDuration()));

        Glide.with(getApplicationContext()).load(services.get(position).getImage()).into(image);

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Context context = view.getRootView().getContext();
                AlertDialog.Builder alert = new AlertDialog.Builder(context, R.style.AlertDialogCustom);

                alert.setTitle("Confirmation");
                alert.setMessage("Add service to basket?");

                db = new DatabaseHelper(getApplicationContext());

                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Intent intent = new Intent(view.getContext(), ConfirmationActivity.class);



                        String service_name = services.get(position).getService_name();
                        double price = services.get(position).getPrice();
                        int duration = (int) services.get(position).getDuration();
                        String imagedb = services.get(position).getImage();

                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        String uid = user.getUid();


                            boolean exists = db.checkExists(service_name);

                            if (!exists)
                                db.insertService(uid, service_name, price, duration, imagedb);
                            else
                                Toast.makeText(getApplicationContext(), "You already added this service.", Toast.LENGTH_SHORT).show();
                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });

                alert.show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return services.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name, price, duration;
        public RelativeLayout layout;
        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name_text);
            price = itemView.findViewById(R.id.text1);
            duration = itemView.findViewById(R.id.text2);
            image = itemView.findViewById(R.id.profile_image);
            layout = itemView.findViewById(R.id.relative);
        }
    }
}

