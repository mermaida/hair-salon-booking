package com.example.aida.finalproj.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
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

import com.bumptech.glide.Glide;
import com.example.aida.finalproj.Classes.DatabaseHelper;
import com.example.aida.finalproj.Classes.Service;
import com.example.aida.finalproj.R;

import java.util.ArrayList;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Aida on 15.04.2018..
 */

public class ConfirmAdapter extends RecyclerView.Adapter<ConfirmAdapter.ViewHolder> {

    Activity activity;
    List<Service> services;
    DatabaseHelper db;
    Context context;
    ImageView image;

    public ConfirmAdapter(Activity activity, List<Service> services){
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
        holder.price.setText("Price: ₺" + Double.toString(services.get(position).getPrice()));
        holder.duration.setText("Duration: " + Double.toString(services.get(position).getDuration()));

        Log.i("imagess", "" + services.get(position).getImage());

        Glide.with(getApplicationContext()).load(services.get(position).getImage()).into(image);

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                context = view.getRootView().getContext();
                AlertDialog.Builder alert = new AlertDialog.Builder(context, R.style.AlertDialogCustom);

                alert.setTitle("Delete");
                alert.setMessage("Remove service from basket?");

                db = new DatabaseHelper(getApplicationContext());

                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        db.deleteService(services.get(position));
                        services.remove(services.get(position));
                        notifyDataSetChanged();
                        activity.finish();
                        activity.overridePendingTransition(0, 0);
                        activity.startActivity(activity.getIntent());
                        activity.overridePendingTransition(0, 0);
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