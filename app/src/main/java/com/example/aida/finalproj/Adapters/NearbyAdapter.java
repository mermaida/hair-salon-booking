package com.example.aida.finalproj.Adapters;

/**
 * Created by Aida on 08.04.2018..
 */

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.aida.finalproj.R;
import com.example.aida.finalproj.Classes.Salon;
import com.example.aida.finalproj.Activities.UserActivities.SelectService;

import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

public class NearbyAdapter extends RecyclerView.Adapter<NearbyAdapter.ViewHolder> {

    Activity activity;
    List<Salon> salons;

    public NearbyAdapter(Activity activity, List<Salon> salons){
        this.activity = activity;
        this.salons = salons;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mname.setText(salons.get(position).getSalon_name());
        holder.address.setText(salons.get(position).getAddress());
        holder.rating.setText("★ " + Double.toString(salons.get(position).getRating()));
    }

    @Override
    public int getItemCount() {
        return salons.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mname, address, rating;
        public ImageView image;
        public ViewHolder(View itemView) {
            super(itemView);
            mname = itemView.findViewById(R.id.name_text);
            address = itemView.findViewById(R.id.text2);
            rating = itemView.findViewById(R.id.text1);
            image = itemView.findViewById(R.id.profile_image);

            String url = "https://firebasestorage.googleapis.com/v0/b/finalproj-4653f.appspot.com/o/pet06-512.png?alt=media&token=aa2226c1-3d01-4e00-990c-9792914609c3";
            Glide.with(getApplicationContext()).load(url).into(image);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    // get position
                    int pos = getAdapterPosition();

                    // check if item still exists
                    if(pos != RecyclerView.NO_POSITION){
                        Salon clickedDataItem = salons.get(pos);
                        String value = clickedDataItem.getSalon_name();
                        Intent intent = new Intent(v.getContext(), SelectService.class).putExtra("name", value);
                        v.getContext().startActivity(intent);
                    }
                }
            });

        }
    }
}
