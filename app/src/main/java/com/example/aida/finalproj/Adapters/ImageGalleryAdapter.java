package com.example.aida.finalproj.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.aida.finalproj.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Aida on 25.04.2018..
 */

public class ImageGalleryAdapter extends RecyclerView.Adapter<ImageGalleryAdapter.ViewHolder> {
    Activity activity;
    List<Uri> urls;
    public ImageView imageview;
    String uid;
    DatabaseReference ref;
    Query query;
    Uri photo;

    public ImageGalleryAdapter(Activity activity, List<Uri> urls) {
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
                    Context context = view.getRootView().getContext();
                    AlertDialog.Builder alert = new AlertDialog.Builder(context, R.style.AlertDialogCustom);

                    alert.setTitle("Confirmation");
                    alert.setMessage("Remove this image?");
                    alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            uid = user.getUid();

                            ref = FirebaseDatabase.getInstance().getReference().child("salons").child(uid).child("gallery");
                            query = ref.orderByChild("image_url").equalTo(urls.get(position).toString());
                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                        String child = childSnapshot.child("image_url").getValue(String.class);
                                        Log.i("childval", "" + child);
                                        childSnapshot.getRef().removeValue();
                                        notifyDataSetChanged();
                                        activity.finish();
                                        activity.overridePendingTransition(0, 0);
                                        activity.startActivity(activity.getIntent());
                                        activity.overridePendingTransition(0, 0);
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                            Toast.makeText(getApplicationContext(), "Image removed.", Toast.LENGTH_SHORT).show();
                        }
                    });
                    alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                        }
                    });

                    alert.show();
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
