package com.example.chargex;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class CustomerIndex extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_index);
    }
    public void Profile(View v){
        Intent i=new Intent(getApplicationContext(),Profile.class);
        startActivity(i);
    }
    public void bookSlot(View v){
        Intent i=new Intent(getApplicationContext(), BookSlot.class);
        startActivity(i);
    }
    public void checkout(View v){
        Intent i=new Intent(getApplicationContext(), Checkout.class);
        startActivity(i);
    }


}