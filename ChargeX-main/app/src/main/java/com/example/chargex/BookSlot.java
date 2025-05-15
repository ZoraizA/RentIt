package com.example.chargex;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

public class BookSlot extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_slot);
    }

    public void moveStations(View v){
        SharedPreferences preferences=getSharedPreferences("user_data", Context.MODE_PRIVATE);
        String username=preferences.getString("username","customer");
        Slot slot=new Slot();
        TextView view=findViewById(R.id.slotStartTime);
        String startTime=view.getText().toString();
        view=findViewById(R.id.slotEndTime);
        String endTime=view.getText().toString();
        view=findViewById(R.id.slotDate);
        String date=view.getText().toString();


        if (!isValidTimeFormat(startTime) || !isValidTimeFormat(endTime)) {
            showToast("Invalid time format. Please use HH:mm:ss");
            return;
        }

        // Validate date format
        if (!isValidDateFormat(date)) {
            showToast("Invalid date format. Please use yyyy-MM-dd");
            return;
        }

        // Check if start time is before end time
        if (!isStartTimeBeforeEndTime(startTime, endTime)) {
            showToast("Start time must be before end time");
            return;
        }

        Intent i=new Intent(getApplicationContext(), ViewStations.class);
        i.putExtra("startTime",startTime);
        i.putExtra("endTime",endTime);
        i.putExtra("date",date);
        startActivity(i);


    }


    private boolean isValidTimeFormat(String time) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            sdf.setLenient(false);
            sdf.parse(time);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    private boolean isValidDateFormat(String date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setLenient(false);
            sdf.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    private boolean isStartTimeBeforeEndTime(String startTime, String endTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        try {
            Date start = sdf.parse(startTime);
            Date end = sdf.parse(endTime);
            return start.before(end);
        } catch (ParseException e) {
            return false;
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}