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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aida.finalproj.Classes.DatabaseHelper;
import com.example.aida.finalproj.Classes.RetrievedAppointment;
import com.example.aida.finalproj.Classes.Service;
import com.example.aida.finalproj.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Aida on 23.04.2018..
 */

public class SalonRetrieveAdapter extends RecyclerView.Adapter<SalonRetrieveAdapter.ViewHolder> {

    Activity activity;
    List<RetrievedAppointment> appointments;
    String str;

    public SalonRetrieveAdapter(Activity activity, List<RetrievedAppointment> appointments) {
        this.activity = activity;
        this.appointments = appointments;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.appt_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.sname.setText(appointments.get(position).getSalon_name());
        holder.datetime.setText(appointments.get(position).getDate() + ", " + appointments.get(position).getTime());
        holder.price.setText("Total price: " + Double.toString(appointments.get(position).getTotal_price()));

        List <String> keys = appointments.get(position).getService_name();
        if (keys != null) {
            if (keys.size() == 1) holder.servicenames.setText(keys.get(0));
            else {
                String lastkey = keys.get(keys.size() - 1);
                keys.remove(keys.size() - 1);
                String str = Arrays.toString(keys.toArray()).replace("[", "").replace("]", "" + " and " + lastkey);
                holder.servicenames.setText(str);
            }
        }
    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView sname, datetime, servicenames, price;
        public LinearLayout layout;

        public ViewHolder(View itemView) {
            super(itemView);
            sname = itemView.findViewById(R.id.slnname);
            datetime = itemView.findViewById(R.id.datetime);
            servicenames = itemView.findViewById(R.id.services);
            price = itemView.findViewById(R.id.tprice);
            layout = itemView.findViewById(R.id.lnlayout);
        }
    }
}
