package com.example.chargex;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class StationDashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_dashboard);
    }
    public void addMachine(View v){
        Intent i=new Intent(getApplicationContext(),AddMachine.class);
        startActivity(i);
    }
    public void addSlot(View v){
        Intent i=new Intent(getApplicationContext(), AddSlot.class);
        startActivity(i);
    }

}