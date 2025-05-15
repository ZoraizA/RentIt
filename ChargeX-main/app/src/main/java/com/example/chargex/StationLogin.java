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

public class StationLogin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_login);
    }
    public void logIn(View v){
        Station station=new Station();
        TextView input=findViewById(R.id.stationName);
        if(input== null)
        {
            Toast.makeText(StationLogin.this,"Enter name", Toast.LENGTH_SHORT).show();
            return;
        }
        if (input.getText() == null || input.getText().equals("")) {
            Toast.makeText(StationLogin.this,"Enter name", Toast.LENGTH_SHORT).show();
            return;
        }
        String name=input.getText().toString();
        if(name.length()==0){
            Toast.makeText(StationLogin.this,"Enter name", Toast.LENGTH_SHORT).show();
            return;
        }
        station.getAccount(input.getText().toString(), new callback() {
            @Override
            public void onSuccess(String result) {
                TextView input=findViewById(R.id.stationPassword);
                String pw=input.toString();
                /*if(pw.length()==0){
                    Toast.makeText(StationLogin.this,"Enter password", Toast.LENGTH_SHORT).show();
                    return;
                }*/
                Log.d(TAG,"logIn done");
                Log.d(TAG,"pw is:"+station.getPassword());
                if(station.getPassword().equals(input.getText().toString())){
                    SharedPreferences preferences = getSharedPreferences("user_data", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("loggedIn", "True");
                    editor.putString("role","station");
                    editor.putString("username",station.getName());
                    editor.apply();
                    if(station.getStatus().equals("verified")){
                        Intent dashboard=new Intent(getApplicationContext(), StationDashboard.class);
                        startActivity(dashboard);
                    }
                    else {
                        Intent index = new Intent(getApplicationContext(), StationIndex.class);
                        startActivity(index);
                    }
                }
                else{
                    Toast.makeText(StationLogin.this,"Account not found", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Exception e) {
                TextView input=findViewById(R.id.stationPassword);
                String pw=input.toString();
                if(pw.length()==0){
                    Toast.makeText(StationLogin.this,"Enter password", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(StationLogin.this,"Account not found", Toast.LENGTH_SHORT).show();
                Log.d(TAG,"logIn failed");
            }
        });


    }
    public void Register(View v){
        Intent register=new Intent(getApplicationContext(),SellerReg.class);
        startActivity(register);
    }


}