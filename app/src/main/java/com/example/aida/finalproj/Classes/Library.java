package com.example.aida.finalproj.Classes;

/**
 * Created by Aida on 26.04.2018..
 */

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

/**
 * Created by user on 4/1/2018.
 */

public class Library {

    static Activity activity;

    public Library(Activity activity){
        this.activity = activity;
    }
    public void statusCheck(Activity activity) {
        final LocationManager manager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps(activity);

        }

    }


    public void buildAlertMessageNoGps(final Activity activity) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        activity.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    public void isGoogleAPIAvailable(Activity activity, int LOCATION_PERMISSION_CODE) {
        int availability = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(activity);
        if (availability == ConnectionResult.SUCCESS) {
            Log.i("Google API", "Succefully installed");
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(availability)) {
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(activity, availability, LOCATION_PERMISSION_CODE);
            Log.i("Google Api", "Fixable Error");
        } else
            Toast.makeText(activity, "Google API Is not available", Toast.LENGTH_LONG).show();
    }

    public static void DialogInterface(String content, String beginning, final exitDecision exitDecision) {
        final AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            builder = new AlertDialog.Builder(activity, R.style.EvoTemasi);
            builder = new AlertDialog.Builder(activity);

        } else {
            builder = new AlertDialog.Builder(activity);
        }

        final AlertDialog dialog = builder.setTitle(beginning)
                .setMessage(content)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        exitDecision.decision(true);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        exitDecision.decision(false);
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.WHITE);
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.WHITE);
            }
        });
    }

    public interface exitDecision {
        void decision(boolean bool);
    }


}
