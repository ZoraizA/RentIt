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
import android.widget.Toast;

public class StationProfile extends AppCompatActivity {
    private Station station;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        station= new Station();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_profile);
        set();
    }

    public void set() {
        SharedPreferences preferences = getSharedPreferences("user_data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        String username = preferences.getString("username", "");

        station.getAccount(username, new callback() {
            @Override
            public void onSuccess(String result) {
                TextView dummy = findViewById(R.id.stationName);
                if (station.getName() != null && !station.getName().equals("")) {
                    dummy.setText(station.getName());
                }
                dummy = findViewById(R.id.stationEmail);
                if (station.getEmail() != null&& !station.getName().equals("")) {
                    dummy.setText(station.getEmail());
                }

                dummy = findViewById(R.id.stationAddress);
                if (station.getAddress() != null && !station.getAddress().equals("") ) {
                    dummy.setText(station.getAddress());
                }
                dummy = findViewById(R.id.stationPhone);
                if (station.getContactNumber() != null&& !station.getContactNumber().equals("")) {
                    dummy.setText(station.getContactNumber());
                }
            }

            @Override
            public void onFailure(Exception e) {

            }
        });
    }
    public void update(View v){
        TextView t=findViewById(R.id.stationName);
        station.setName(t.getText().toString());
        t=findViewById(R.id.stationAddress);
        station.setAddress(t.getText().toString());
        t=findViewById(R.id.stationEmail);
        station.setEmail(t.getText().toString());
        t=findViewById(R.id.stationPhone);
        station.setNumber(t.getText().toString());
        String email=station.getEmail();
        String phoneno=station.getContactNumber();
        if(phoneno.length()!=11){
            Toast.makeText(StationProfile.this,"Phone number is 11 digits.",Toast.LENGTH_SHORT).show();
            return;
        }
        boolean isPresent=false;
        for(int i=0;i<email.length();i++) {
            if(email.charAt(i)=='@')
                isPresent=true;
        }
        if(!isPresent) {
            Toast.makeText(StationProfile.this,"Invalid email format.@ is missing",Toast.LENGTH_SHORT).show();
            return;
        }

        String ending=".com";
        int index=0;
        for(int i=email.length()-4;i<email.length();i++) {
            if(email.charAt(i)!=ending.charAt(index)){
                Toast.makeText(StationProfile.this,"Invalid email.It should end with .com",Toast.LENGTH_SHORT).show();
                return;
            }
            index++;
        }
        String pw=station.getPassword();
        if(pw.length()<8) {
            Toast.makeText(StationProfile.this,"Password has to be atleast 8 characters",Toast.LENGTH_SHORT).show();
            return;
        }
        station.setData();
        Intent idx=new Intent(getApplicationContext(),StationIndex.class);
        startActivity(idx);

    }
    public void setLocation(View v){
        Intent loc=new Intent(getApplicationContext(),setLocation.class);
        startActivity(loc);
    }



}