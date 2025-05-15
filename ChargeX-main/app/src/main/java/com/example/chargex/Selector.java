package com.example.chargex;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Selector extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selector);
    }
    public void customer_route(View v){
        Intent profile=new Intent(getApplicationContext(),SignUp.class);
        startActivity(profile);
    }
    public void station_route(View v){
        Intent profile=new Intent(getApplicationContext(),SellerReg.class);
        startActivity(profile);
    }
    public void admin_route(View v){
        Intent admin=new Intent(getApplicationContext(),AdminLogIn.class);
        startActivity(admin);
    }


}