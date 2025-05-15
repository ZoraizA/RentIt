package com.example.chargex;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.Filter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class showMachinesUser extends AppCompatActivity {
    int count;
    List<ChargingMachine> machineList;
    double Latitude;
    double Longitude;
    private String date;
    private String endTime;
    private String startTime;
    private String station;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Latitude=getIntent().getDoubleExtra("latitude",31.5497);
        Longitude=getIntent().getDoubleExtra("longitude",78.3436);
        date=getIntent().getStringExtra("date");
        startTime=getIntent().getStringExtra("startTime");
        endTime=getIntent().getStringExtra("endTime");
        Log.d(TAG,"Longitude is:"+Longitude);
        Log.d(TAG,"Latitude is:"+Latitude);
        count=0;
        machineList=new ArrayList<>();
        final String Name[] = new String[1];
        StationRecord record=new StationRecord();
        super.onCreate(savedInstanceState);
        getStation(Name, new callback() {
            @Override
            public void onSuccess(String result) {
                Log.d(TAG,"Selected station is:"+Name[0]);
                record.setName(Name[0]);
                station=Name[0];
                record.getMachines(new callback() {

                    @Override
                    public void onSuccess(String result) {
                        Log.d(TAG,"machines fetched are"+record.machineList.size());
                        machineList=record.getMachineList();
                        updateUI();

                    }

                    @Override
                    public void onFailure(Exception e) {
                        Log.d(TAG,"Machine fetched failed");
                    }
                });
            }

            @Override
            public void onFailure(Exception e) {
                Log.d(TAG,"Selected not found:"+Name[0]);
            }
        });



        Log.d(TAG,"Machine fetched start");

        Log.d(TAG,"Machine fetched end");

        setContentView(R.layout.activity_show_machines_user);
    }
    public void next(View v){
        count++;
        updateUI();
    }
    public void previous(View v){
        count--;
        updateUI();
    }


    @SuppressLint("SetTextI18n")
    private void updateUI(){
        if(count<machineList.size() && count>=0) {
            TextView v = findViewById(R.id.stationName);
            v.setText(machineList.get(count).getStation().getName());
            v = findViewById(R.id.machineID);
            v.setText("ID: " + machineList.get(count).getId());
            v = findViewById(R.id.machineSpeed);
            v.setText("Speed" + machineList.get(count).getChargingSpeed());
            v = findViewById(R.id.machinePrice);
            v.setText("Rate: " + machineList.get(count).getPrice());
            v = findViewById(R.id.stationAddress);
            v.setText("Address: " + machineList.get(count).getStation().getAddress());
        }
        else{
            Intent i=new Intent(getApplicationContext(), CustomerIndex.class);
            startActivity(i);
        }
    }



    public void getStation(final String Name[],callback async){

        FirebaseFirestore db=FirebaseFirestore.getInstance();
        db.collection("Station").where(Filter.and(Filter.equalTo("latitude",Latitude),
                        Filter.equalTo("longitude",Longitude)))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            int tot_tasks=task.getResult().size();
                            final int[] tasks_completed = {0};
                            for(QueryDocumentSnapshot document:task.getResult()){
                                Station station=new Station();
                                Log.d(TAG,"staion name is"+document.getData().get("name").toString());
                                Name[0] =document.getData().get("name").toString();

                            }
                            async.onSuccess("station fetched");

                        }
                        else{
                            async.onFailure(task.getException());
                        }
                    }
                });
    }
    public void addSlot(View v){
        SharedPreferences preferences=getSharedPreferences("user_data", Context.MODE_PRIVATE);
        Slot slot=new Slot();
        slot.setUser(preferences.getString("username","customer"));
        slot.setMachine_id(machineList.get(count).getId());
        slot.setStation(machineList.get(count).getStation().getName());
        slot.setPrice(machineList.get(count).getPrice());
        slot.setDate(LocalDate.parse(date));
        slot.setStartTime(LocalTime.parse(startTime));
        slot.setEndTime(LocalTime.parse(endTime));
        slot.setData();
        Intent i=new Intent(getApplicationContext(), BookingIndex.class);
        startActivity(i);
    }

    public void checkout(View v){
        Intent i=new Intent(getApplicationContext(), Checkout.class);
        i.putExtra("date",date);
        i.putExtra("startTime",startTime);
        i.putExtra("endTime",endTime);
        i.putExtra("station",station);
        i.putExtra("machineId",machineList.get(count).getId());
        i.putExtra("rate",machineList.get(count).getPrice());
        startActivity(i);
    }
}