package com.example.aida.finalproj.Adapters;

import android.app.Activity;
import android.app.Dialog;
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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aida.finalproj.Classes.DatabaseHelper;
import com.example.aida.finalproj.Classes.RetrievedAppointment;
import com.example.aida.finalproj.Classes.Salon;
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

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    Activity activity;
    List<RetrievedAppointment> appointments;
    String str;
    Dialog rankDialog;
    RatingBar ratingBar;
    String rtng, name, sid;
    Date dateobj, dateobj1;
    Long millidate;
    DatabaseReference ref, ref2;
    double result;

    public HistoryAdapter(Activity activity, List<RetrievedAppointment> appointments) {
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
        holder.price.setText("Total price: ₺" + Double.toString(appointments.get(position).getTotal_price()));

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



        String date = appointments.get(position).getDate();
        SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy");
        try {
            dateobj = dt.parse(date);
            SimpleDateFormat dt1 = new SimpleDateFormat("yyyy/M/d");
            String date1 = dt1.format(dateobj);
            dateobj1 = dt1.parse(date1);
            millidate = dateobj1.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                ref = FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("appointments").child(millidate.toString());

                rankDialog = new Dialog(activity);
                rankDialog.setContentView(R.layout.rank_dialog);
                rankDialog.setCancelable(true);
                ratingBar = rankDialog.findViewById(R.id.dialog_ratingbar);
                ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                    public void onRatingChanged(RatingBar ratingBar, float rating,
                                                boolean fromUser) {

                        rtng = String.valueOf(rating);
                    }
                });

                //ratingBar.setRating(userRankValue);
                // text.setText(name);

                Button updateButton = rankDialog.findViewById(R.id.rank_dialog_button);
                updateButton.setOnClickListener(new View.OnClickListener()

                {
                    @Override
                    public void onClick(View v) {
                        name = appointments.get(position).getSalon_name();
                        ref2 = FirebaseDatabase.getInstance().getReference().child("salons");
                        Query query = ref2.orderByChild("salon_name").equalTo(name);
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                    Log.i("valuetest", "i entered here");
                                    double old = childSnapshot.child("rating").getValue(Double.class);
                                    double current = Double.parseDouble(rtng);

                                    Log.i("valuetest", "old result: " + old);
                                    Log.i("valuetest", "current result: " + current);

                                    result = (old + current) / 2;
                                    Log.i("valuetest", "final result: " + result);

                                    sid = childSnapshot.getKey();
                                    Log.i("valuetest", "salon_id: " + sid);
                                    ref2.child(sid).child("rating").setValue(result);
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        ref.child("rating").setValue(rtng);

                        rankDialog.dismiss();
                    }
                });

                rankDialog.show();

            }
        });
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
