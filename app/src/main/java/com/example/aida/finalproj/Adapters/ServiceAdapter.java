package com.example.aida.finalproj.Adapters;

/**
 * Created by Aida on 10.04.2018..
 */

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.aida.finalproj.Classes.Service;
import com.example.aida.finalproj.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.facebook.FacebookSdk.getApplicationContext;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ViewHolder> {

    Activity activity;
    List<Service> services;
    DatabaseReference ref;
    FirebaseUser user;
    String uid, key;
    HashMap<Integer, String> map;
    List<Service> serviceList;
    Service service;
    Boolean choice;
    Context context;
    EditText price, duration;
    LinearLayout layout;
    ImageView image;

    public ServiceAdapter(Activity activity, List<Service> services){
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

        double p = services.get(position).getPrice();
        double d = services.get(position).getDuration();
        int d2 = (int) d;

        holder.mname.setText(services.get(position).getService_name());

        holder.price.setText("Fiyat: ₺" + Double.toString(p));
        holder.duration.setText("Süre: " + Integer.toString(d2) + "dk");

       // Log.i("imagestring", "" + services.get(position).getImage());
        Glide.with(getApplicationContext()).load(services.get(position).getImage()).into(image);

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context = view.getRootView().getContext();
              //  ContextThemeWrapper ctw = new ContextThemeWrapper(context, android.R.style.Theme_Material_Light_Dialog_Alert);
                AlertDialog.Builder alert = new AlertDialog.Builder(context, R.style.AlertDialogCustom);

                layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);

                String[] items = {"Evet", "Hayir"};

                user = FirebaseAuth.getInstance().getCurrentUser();
                uid = user.getUid();

                //TextView ques = new TextView(context);
                alert.setTitle("Bu servisi eklemek ister misiniz?");
               // layout.addView(ques);

                service = services.get(position);

                ref = FirebaseDatabase.getInstance().getReference().child("salons").child(uid).child("services");

                alert.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                switch (item) {
                                    case 0:
                                        choice = true;
                                        price = new EditText(context);
                                        price.setHint("Fıyat");
                                        price.setInputType(InputType.TYPE_CLASS_NUMBER);
                                        layout.addView(price);

                                        duration = new EditText(context);
                                        duration.setHint("Süre (dk)");
                                        duration.setInputType(InputType.TYPE_CLASS_NUMBER);
                                        layout.addView(duration);

                                        break;
                                    case 1:
                                        choice = false;
                                        if (price != null && duration != null) {
                                           layout.removeView(price);
                                           layout.removeView(duration);
                                           break;
                                       }
                                }
                            }
                        });

                alert.setView(layout);

                alert.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if(choice) ref.child("s" + (position)).child("exists").setValue(choice);
                        else ref.child("s" + (position)).child("exists").setValue(choice);

                        Log.i("choice", "" + choice);

                        String pr = price.getText().toString();
                        int pr2 = Integer.parseInt(pr);
                        ref.child("s" + (position)).child("price").setValue(pr2);

                        Log.i("choice", "" + pr);

                        String dur = duration.getText().toString();
                        double dur2 = Double.parseDouble(dur);
                        ref.child("s" + (position)).child("duration").setValue(dur2);

                        notifyDataSetChanged();
                        activity.finish();
                        activity.overridePendingTransition(0, 0);
                        activity.startActivity(activity.getIntent());
                        activity.overridePendingTransition(0, 0);

                    }
                });
                alert.setNegativeButton("Geri dön", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });

                Dialog show = alert.create();
                show.show();
                show.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
                show.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

            }
        });
    }

    @Override
    public int getItemCount() {
        return services.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mname, price, duration;
        public RelativeLayout layout;
        public ViewHolder(View itemView) {
            super(itemView);
            mname = itemView.findViewById(R.id.name_text);
            price = itemView.findViewById(R.id.text1);
            duration = itemView.findViewById(R.id.text2);
            image = itemView.findViewById(R.id.profile_image);
            layout = itemView.findViewById(R.id.relative);

        }
    }
}
