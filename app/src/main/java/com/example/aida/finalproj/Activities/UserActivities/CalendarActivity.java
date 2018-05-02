package com.example.aida.finalproj.Activities.UserActivities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CalendarView;

import com.example.aida.finalproj.R;

public class CalendarActivity extends AppCompatActivity {

    String value, durstr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        value = getIntent().getStringExtra("id");
        durstr = getIntent().getStringExtra("duration");

        CalendarView calendarView = findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView CalendarView, int year, int month, int dayOfMonth) {
                String date = year + "/" + (month+1) + "/"+ dayOfMonth ;
                Intent intent = new Intent(CalendarActivity.this, BookAppointment.class);
                intent.putExtra("date", date);
                intent.putExtra("id", value);
                intent.putExtra("duration", durstr);
                startActivity(intent);
                finish();
            }
        });
    }
}
