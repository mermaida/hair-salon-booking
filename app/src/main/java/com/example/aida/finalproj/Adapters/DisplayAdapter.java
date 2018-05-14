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

import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

public class DisplayAdapter extends RecyclerView.Adapter<DisplayAdapter.ViewHolder> {

    Activity activity;
    List<Service> services;
    private DatabaseHelper db;
    ImageView image;

    public DisplayAdapter(Activity activity, List<Service> services){
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
        holder.price.setText("Fiyat: ₺" + Double.toString(services.get(position).getPrice()));
        holder.duration.setText("Süre: " + Double.toString(services.get(position).getDuration()) + "dk");

        Glide.with(getApplicationContext()).load(services.get(position).getImage()).into(image);

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Context context = view.getRootView().getContext();
                AlertDialog.Builder alert = new AlertDialog.Builder(context, R.style.AlertDialogCustom);

                alert.setTitle("Ekle");
                alert.setMessage("Sepete eklemek ister misiniz?");

                db = new DatabaseHelper(getApplicationContext());

                alert.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                       // Intent intent = new Intent(view.getContext(), ConfirmationActivity.class);

                        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        String id = sharedPref.getString("id", "Current id not recognized");
                        Log.i("did", id);

                        String service_name = services.get(position).getService_name();
                        double price = services.get(position).getPrice();
                        int duration = (int) services.get(position).getDuration();
                        String imagedb = services.get(position).getImage();

                        String checked = db.checkId(id);

                        if(checked.equals(id) || checked.equals("")) {

                            boolean exists = db.checkExists(service_name);

                            if (!exists)
                                db.insertService(id, service_name, price, duration, imagedb);
                            else
                                Toast.makeText(getApplicationContext(), "Bu servis sepete zaten eklendi.", Toast.LENGTH_SHORT).show();
                        }
                        else Toast.makeText(getApplicationContext(), "Başka bir salondan servis eklenmez!", Toast.LENGTH_LONG).show();
                    }
                });

                alert.setNegativeButton("İptal", new DialogInterface.OnClickListener() {
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

