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

public class RetrievedAdapter extends RecyclerView.Adapter<RetrievedAdapter.ViewHolder> {

    Activity activity;
    List<RetrievedAppointment> appointments;
    String str, date1, salon_id, time, uid;
    Date dateobj, dateobj1;
    Long millidate;
    DatabaseReference ref;

    public RetrievedAdapter(Activity activity, List<RetrievedAppointment> appointments) {
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
        if (keys.size() == 1) holder.servicenames.setText(keys.get(0));
        else {
            String lastkey = keys.get(keys.size() - 1);
            keys.remove(keys.size() - 1);
            String str = Arrays.toString(keys.toArray()).replace("[", "").replace("]", "" + " and " + lastkey);
            holder.servicenames.setText(str);
        }

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Context context = view.getRootView().getContext();
                AlertDialog.Builder alert = new AlertDialog.Builder(context, R.style.AlertDialogCustom);

                alert.setTitle("Cancel");
                alert.setMessage("Are you sure you want to cancel this appointment?");

                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        String date = appointments.get(position).getDate();
                        SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy");
                        try {
                            dateobj = dt.parse(date);
                            SimpleDateFormat dt1 = new SimpleDateFormat("yyyy/M/d");
                            date1 = dt1.format(dateobj);
                            dateobj1 = dt1.parse(date1);
                            millidate = dateobj1.getTime();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        time = appointments.get(position).getTime();

                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        uid = user.getUid();

                        ref = FirebaseDatabase.getInstance().getReference().child("salons");
                        Query query = ref.orderByChild("salon_name").equalTo(appointments.get(position).getSalon_name());
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                    salon_id = childSnapshot.getKey();
                                    Log.i("salonid", "" + salon_id + " date " + date1);
                                    //ref.child(salon_id).child("schedule").child(date1).child(time).removeValue();
                                    Query query = ref.child(salon_id).child("schedule").child(date1).orderByChild("user_id").equalTo(uid);
                                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                                String time1 = childSnapshot.getKey();
                                                ref.child(salon_id).child("schedule").child(date1).child(time1).removeValue();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });



                        DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("appointments");
                        ref2.child(Long.toString(millidate)).removeValue();

                        notifyDataSetChanged();
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
