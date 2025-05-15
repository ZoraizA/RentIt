package com.example.chargex;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddSlot extends AppCompatActivity {
    private String stationName;
    private String selection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Station station=new Station();
        SharedPreferences preferences=getSharedPreferences("user_data",Context.MODE_PRIVATE);
        stationName=preferences.getString("username","station");
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_slot);
    }


}