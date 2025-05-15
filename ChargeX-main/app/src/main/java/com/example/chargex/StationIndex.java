package com.example.chargex;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class StationIndex extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_index);
    }
    public void ViewProfile(View v){
        Intent profile=new Intent(getApplicationContext(), StationProfile.class);
        startActivity(profile);
    }
    public void apply_reg(View v){
        SharedPreferences preferences = getSharedPreferences("user_data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        String username = preferences.getString("username", "");

        Station station=new Station();
        station.getAccount(username, new callback() {
            @Override
            public void onSuccess(String result) {
                station.setStatus("applied");
                String email=station.getEmail();
                boolean isPresent=false;
                for(int i=0;i<email.length();i++) {
                    if(email.charAt(i)=='@')
                        isPresent=true;
                }
                if(!isPresent) {
                    Toast.makeText(StationIndex.this,"Invalid email format.@ is missing",Toast.LENGTH_SHORT).show();
                    return;
                }

                String ending=".com";
                int index=0;
                for(int i=email.length()-4;i<email.length();i++) {
                    if(email.charAt(i)!=ending.charAt(index)){
                        Toast.makeText(StationIndex.this,"Invalid email.It should end with .com",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    index++;
                }
                String pw=station.getPassword();
                if(pw.length()<8) {
                    Toast.makeText(StationIndex.this,"Password has to be atleast 8 characters",Toast.LENGTH_SHORT).show();
                    return;
                }
                station.setData();
            }

            @Override
            public void onFailure(Exception e) {

            }
        });
    }



}