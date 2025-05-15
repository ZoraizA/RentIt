package com.example.chargex;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class BookingIndex extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_index);
    }
    public void UnPaidBookings(View v){
        Intent i=new Intent(getApplicationContext(), MyBookings.class);
        startActivity(i);
    }

}