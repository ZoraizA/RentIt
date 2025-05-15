package com.example.chargex;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class AddMachine extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_machine);
    }
    public void addMachine(View v){
        ChargingMachine machine=new ChargingMachine();
        Station station=new Station();
        TextView view=findViewById(R.id.machineID);
        machine.setId(Integer.parseInt(view.getText().toString()));
        view=findViewById(R.id.machineSpeed);
        machine.setChargingSpeed(Double.parseDouble(view.getText().toString()));
        SharedPreferences preferences=getSharedPreferences("user_data", Context.MODE_PRIVATE);
        Log.d(TAG,"station name is"+preferences.getString("username","station"));
        station.getAccount(preferences.getString("username", "station"), new callback() {
            @Override
            public void onSuccess(String result) {
                Log.d(TAG,"succeded");
                machine.setStation(station);
                machine.setData();
                dashboard();

            }

            @Override
            public void onFailure(Exception e) {
                Log.d(TAG,"succeded");
            }
        });


    }
    public void dashboard(){
        Intent i=new Intent(getApplicationContext(), StationDashboard.class);
        startActivity(i);
    }


}