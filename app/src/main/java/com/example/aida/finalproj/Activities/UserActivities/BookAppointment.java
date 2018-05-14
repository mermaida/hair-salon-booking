package com.example.aida.finalproj.Activities.UserActivities;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aida.finalproj.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BookAppointment extends AppCompatActivity implements View.OnClickListener {

    private TextView textdate;
    private CardView selectdate;
    Button btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8;
    DatabaseReference ref, rootref;
    String date, value, durstr;
    double duration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appointment);

        value = getIntent().getStringExtra("id");
        durstr = getIntent().getStringExtra("duration");
        duration = Double.parseDouble((durstr));
        Log.i("dbkey", "" + value);

        ref = FirebaseDatabase.getInstance().getReference().child("salons").child(value).child("schedule");


        textdate = findViewById(R.id.date);
        selectdate = findViewById(R.id.carddate);

        selectdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BookAppointment.this, CalendarActivity.class).putExtra("id", value);
                intent.putExtra("duration", durstr);
                startActivity(intent);
                finish();
            }
        });

        Intent incoming = getIntent();
        date = incoming.getStringExtra("date");
        textdate.setText(date);

        btn1 = findViewById(R.id.btn1);
        btn1.setOnClickListener(this);
        btn2 = findViewById(R.id.btn2);
        btn2.setOnClickListener(this);
        btn3 = findViewById(R.id.btn3);
        btn3.setOnClickListener(this);
        btn4 = findViewById(R.id.btn4);
        btn4.setOnClickListener(this);
        btn5 = findViewById(R.id.btn5);
        btn5.setOnClickListener(this);
        btn6 = findViewById(R.id.btn6);
        btn6.setOnClickListener(this);
        btn7 = findViewById(R.id.btn7);
        btn7.setOnClickListener(this);
        btn8 = findViewById(R.id.btn8);
        btn8.setOnClickListener(this);

        if (date != null) {
            rootref = ref.child(date);
            rootref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (snapshot.hasChild("09:00")) {
                        btn1.setEnabled(false);
                        btn1.setBackgroundColor(Color.GRAY);
                    }
                    if (snapshot.hasChild("10:00")) {
                        btn2.setEnabled(false);
                        btn2.setBackgroundColor(Color.GRAY);
                    }
                    if (snapshot.hasChild("11:00")) {
                        btn3.setEnabled(false);
                        btn3.setBackgroundColor(Color.GRAY);
                    }
                    if (snapshot.hasChild("12:00")) {
                        btn4.setEnabled(false);
                        btn4.setBackgroundColor(Color.GRAY);
                    }
                    if (snapshot.hasChild("13:00")) {
                        btn5.setEnabled(false);
                        btn5.setBackgroundColor(Color.GRAY);
                    }
                    if (snapshot.hasChild("14:00")) {
                        btn6.setEnabled(false);
                        btn6.setBackgroundColor(Color.GRAY);
                    }
                    if (snapshot.hasChild("15:00")) {
                        btn7.setEnabled(false);
                        btn7.setBackgroundColor(Color.GRAY);
                    }
                    if (snapshot.hasChild("16:00")) {
                        btn8.setEnabled(false);
                        btn8.setBackgroundColor(Color.GRAY);
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {

                }
            });
        }

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn1) {
            if (date != null) {
                rootref = ref.child(date);
                rootref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                            if (duration > 60) {
                                if (snapshot.hasChild("10:00")) {
                                    Toast.makeText(BookAppointment.this, "Seçtiğiniz saat bir başka müşteri ile çakışıyor, lütfen başka bir saat seçiniz.",
                                            Toast.LENGTH_LONG).show();
                                }

                                else {
                                    Intent intent = new Intent(BookAppointment.this, ConfirmationActivity.class).putExtra("date", date);
                                    intent.putExtra("time", "09:00");
                                    intent.putExtra("id", value);
                                    startActivity(intent);
                                    finish();
                                }
                            } else {
                                Intent intent = new Intent(BookAppointment.this, ConfirmationActivity.class).putExtra("date", date);
                                intent.putExtra("time", "09:00");
                                intent.putExtra("id", value);
                                startActivity(intent);
                                finish();
                            }
                        }


                    @Override
                    public void onCancelled(DatabaseError error) {

                    }
                });
            } else Toast.makeText(BookAppointment.this, "Lütfen tarihi seçiniz",
                    Toast.LENGTH_SHORT).show();
        }

        if (v.getId() == R.id.btn2) {
            if (date != null) {
                rootref = ref.child(date);
                rootref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (duration > 60) {
                            if (snapshot.hasChild("11:00")) {
                                Toast.makeText(BookAppointment.this, "Seçtiğiniz saat bir başka müşteri ile çakışıyor, lütfen başka bir saat seçiniz.",
                                        Toast.LENGTH_LONG).show();
                            }

                            else {
                                Intent intent = new Intent(BookAppointment.this, ConfirmationActivity.class).putExtra("date", date);
                                intent.putExtra("time", "10:00");
                                intent.putExtra("id", value);
                                startActivity(intent);
                                finish();
                            }
                        } else {
                            Intent intent = new Intent(BookAppointment.this, ConfirmationActivity.class).putExtra("date", date);
                            intent.putExtra("time", "10:00");
                            intent.putExtra("id", value);
                            startActivity(intent);
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {

                    }
                });
            } else Toast.makeText(BookAppointment.this, "Lütfen tarihi seçiniz",
                    Toast.LENGTH_SHORT).show();
        }

        if (v.getId() == R.id.btn3) {
            if (date != null) {
                rootref = ref.child(date);
                rootref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (duration > 60) {
                            if (snapshot.hasChild("12:00")) {
                                Toast.makeText(BookAppointment.this, "Seçtiğiniz saat bir başka müşteri ile çakışıyor, lütfen başka bir saat seçiniz.",
                                        Toast.LENGTH_LONG).show();
                            }

                            else {
                                Intent intent = new Intent(BookAppointment.this, ConfirmationActivity.class).putExtra("date", date);
                                intent.putExtra("time", "11:00");
                                intent.putExtra("id", value);
                                startActivity(intent);
                                finish();
                            }
                        } else {
                            Intent intent = new Intent(BookAppointment.this, ConfirmationActivity.class).putExtra("date", date);
                            intent.putExtra("time", "11:00");
                            intent.putExtra("id", value);
                            startActivity(intent);
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {

                    }
                });
            } else Toast.makeText(BookAppointment.this, "Lütfen tarihi seçiniz",
                    Toast.LENGTH_SHORT).show();
        }

        if (v.getId() == R.id.btn4) {
            if (date != null) {
                rootref = ref.child(date);
                rootref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (duration > 60) {
                            if (snapshot.hasChild("13:00")) {
                                Toast.makeText(BookAppointment.this, "Seçtiğiniz saat bir başka müşteri ile çakışıyor, lütfen başka bir saat seçiniz.",
                                        Toast.LENGTH_LONG).show();
                            }

                            else {
                                Intent intent = new Intent(BookAppointment.this, ConfirmationActivity.class).putExtra("date", date);
                                intent.putExtra("time", "12:00");
                                intent.putExtra("id", value);
                                startActivity(intent);
                                finish();
                            }
                        } else {
                            Intent intent = new Intent(BookAppointment.this, ConfirmationActivity.class).putExtra("date", date);
                            intent.putExtra("time", "12:00");
                            intent.putExtra("id", value);
                            startActivity(intent);
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {

                    }
                });
            } else Toast.makeText(BookAppointment.this, "Lütfen tarihi seçiniz",
                    Toast.LENGTH_SHORT).show();
        }

        if (v.getId() == R.id.btn5) {
            if (date != null) {
                rootref = ref.child(date);
                rootref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (duration > 60) {
                            if (snapshot.hasChild("14:00")) {
                                Toast.makeText(BookAppointment.this, "Seçtiğiniz saat bir başka müşteri ile çakışıyor, lütfen başka bir saat seçiniz.",
                                        Toast.LENGTH_LONG).show();
                            }

                            else {
                                Intent intent = new Intent(BookAppointment.this, ConfirmationActivity.class).putExtra("date", date);
                                intent.putExtra("time", "13:00");
                                intent.putExtra("id", value);
                                startActivity(intent);
                                finish();
                            }
                        } else {
                            Intent intent = new Intent(BookAppointment.this, ConfirmationActivity.class).putExtra("date", date);
                            intent.putExtra("time", "13:00");
                            intent.putExtra("id", value);
                            startActivity(intent);
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {

                    }
                });
            } else Toast.makeText(BookAppointment.this, "Lütfen tarihi seçiniz",
                    Toast.LENGTH_SHORT).show();
        }

        if (v.getId() == R.id.btn6) {
            if (date != null) {
                rootref = ref.child(date);
                rootref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (duration > 60) {
                            if (snapshot.hasChild("15:00")) {
                                Toast.makeText(BookAppointment.this, "Seçtiğiniz saat bir başka müşteri ile çakışıyor, lütfen başka bir saat seçiniz.",
                                        Toast.LENGTH_LONG).show();
                            }

                            else {
                                Intent intent = new Intent(BookAppointment.this, ConfirmationActivity.class).putExtra("date", date);
                                intent.putExtra("time", "14:00");
                                intent.putExtra("id", value);
                                startActivity(intent);
                                finish();
                            }
                        } else {
                            Intent intent = new Intent(BookAppointment.this, ConfirmationActivity.class).putExtra("date", date);
                            intent.putExtra("time", "14:00");
                            intent.putExtra("id", value);
                            startActivity(intent);
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {

                    }
                });
            } else Toast.makeText(BookAppointment.this, "Lütfen tarihi seçiniz",
                    Toast.LENGTH_SHORT).show();
        }

        if (v.getId() == R.id.btn7) {
            if (date != null) {
                rootref = ref.child(date);
                rootref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (duration > 60) {
                            if (snapshot.hasChild("16:00")) {
                                Toast.makeText(BookAppointment.this, "Seçtiğiniz saat bir başka müşteri ile çakışıyor, lütfen başka bir saat seçiniz.",
                                        Toast.LENGTH_LONG).show();
                            }

                            else {
                                Intent intent = new Intent(BookAppointment.this, ConfirmationActivity.class).putExtra("date", date);
                                intent.putExtra("time", "15:00");
                                intent.putExtra("id", value);
                                startActivity(intent);
                                finish();
                            }
                        } else {
                            Intent intent = new Intent(BookAppointment.this, ConfirmationActivity.class).putExtra("date", date);
                            intent.putExtra("time", "15:00");
                            intent.putExtra("id", value);
                            startActivity(intent);
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {

                    }
                });
            } else Toast.makeText(BookAppointment.this, "Lütfen tarihi seçiniz",
                    Toast.LENGTH_SHORT).show();
        }

        if (v.getId() == R.id.btn8) {
            if (date != null) {
                rootref = ref.child(date);
                rootref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (duration > 120) {
                            if (snapshot.hasChild("18:00")) {
                                Toast.makeText(BookAppointment.this, "Seçtiğiniz saat bir başka müşteri ile çakışıyor, lütfen başka bir saat seçiniz.",
                                        Toast.LENGTH_LONG).show();
                            }

                            else {
                                Intent intent = new Intent(BookAppointment.this, ConfirmationActivity.class).putExtra("date", date);
                                intent.putExtra("time", "16:00");
                                intent.putExtra("id", value);
                                startActivity(intent);
                                finish();
                            }
                        } else {
                            Intent intent = new Intent(BookAppointment.this, ConfirmationActivity.class).putExtra("date", date);
                            intent.putExtra("time", "16:00");
                            intent.putExtra("id", value);
                            startActivity(intent);
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {

                    }
                });
            } else Toast.makeText(BookAppointment.this, "Lütfen tarihi Seçiniz",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
