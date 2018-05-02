package com.example.aida.finalproj.Classes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;

import com.example.aida.finalproj.Activities.Authentication.LoginActivity;
import com.example.aida.finalproj.R;

/**
 * Created by Aida on 27.04.2018..
 */

public class SplashScreen extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Show the splash screen
        setContentView(R.layout.splash_screen);
        // Find the progress bar
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(SplashScreen.this, LoginActivity.class);
                startActivity(i);

                // close this activity
                finish();
            }
        }, 3000);

        /*SharedPreferences sharedpreferences = getSharedPreferences("pref", Context.MODE_PRIVATE);

        if(sharedpreferences.getString("tag", "notok").equals("notok")){

            // add tag in SharedPreference here..
            SharedPreferences.Editor edit = sharedpreferences.edit();
            edit.putString("tag", "ok");
            edit.commit();

            // your logic of splash will go here.
            setContentView(R.layout.splash_screen);

        }else if(sharedpreferences.getString("tag", null).equals("ok")){
            Intent i = new Intent(SplashScreen.this, LoginActivity.class);
            startActivity(i);
            finish();
        }*/

    }
}