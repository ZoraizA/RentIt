package com.example.chargex;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class MyBookings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bookings);
    }
    public List<Slot> GetSlots(){
        List<Slot> slots=new ArrayList<>();
        //FirebaseFirestore db= FirebaseFirestore db
        return slots;
    }

}