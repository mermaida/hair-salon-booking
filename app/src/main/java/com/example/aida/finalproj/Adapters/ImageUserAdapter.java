package com.example.aida.finalproj.Adapters;

/**
 * Created by Aida on 26.04.2018..
 */

import android.app.Activity;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.example.aida.finalproj.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

import android.content.Intent;

import com.example.aida.finalproj.Activities.UserActivities.PhotoDetail;

/**
 * Created by Aida on 25.04.2018..
 */

public class ImageUserAdapter extends RecyclerView.Adapter<ImageUserAdapter.ViewHolder> {
    Activity activity;
    List<Uri> urls;
    public ImageView imageview;
    String uid;
    DatabaseReference ref;
    Query query;
    Uri photo;

    public ImageUserAdapter(Activity activity, List<Uri> urls) {
        this.activity = activity;
        this.urls = urls;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.images_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Glide.with(getApplicationContext()).load(urls.get(position)).into(imageview);

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position != RecyclerView.NO_POSITION) {
                    Intent intent = new Intent(getApplicationContext(), PhotoDetail.class);
                    intent.putExtra(PhotoDetail.EXTRA_PHOTO, urls.get(position));
                    getApplicationContext().startActivity(intent);
                    activity.finish();
                }
            }
        });

    }


    @Override
    public int getItemCount() {
        return urls.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout layout;

        public ViewHolder(View itemView) {
            super(itemView);
            imageview = itemView.findViewById(R.id.iv_photo);
            layout = itemView.findViewById(R.id.imageln);
        }
    }
}
