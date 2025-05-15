package com.example.chargex;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

public class Slot {
    private String user;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private String station;
    private Integer machine_id;
    private String status;

    private double price;

    public Slot(){
        startTime=LocalTime.now();
        endTime=LocalTime.now();
        date=LocalDate.now();
        user="";
        station="";
        machine_id=0;
        price=0;
        status="unpaid";
    }

    public void setUser(String name){this.user=name;}
    public void setStatus(String status){this.status=status;}
    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }
    public void setDate(LocalDate date){this.date=date;}
    public void setStation(String station){this.station=station;}
    public void setMachine_id(Integer machine_id){this.machine_id=machine_id;}
    public void setPrice(double price){this.price=price;}

    public double getPrice(){return this.price;}
    public String getStatus(){return this.status;}

    public String getStation(){return this.station;}
    public Integer getMachine_id(){return this.machine_id;}
    public LocalTime getStartTime() {
        return this.startTime;
    }

    public LocalTime getEndTime() {
        return this.endTime;
    }
    public LocalDate getDate(){return this.date;}
    public String getUser(){return this.user;
    }
    public void setData(){
        Map<String,Object> data=new HashMap<>();
        //int id=this.getId();
        data.put("user",this.getUser());

        data.put("startTime",this.getStartTime().toString());
        data.put("endTime",this.getEndTime().toString());
        data.put("machine_id",this.getMachine_id());
        data.put("station",this.getStation());
        data.put("date",this.getDate().toString());
        data.put("price",this.getPrice());
        data.put("status",this.getStatus());

        FirebaseFirestore db=FirebaseFirestore.getInstance();
        DocumentReference doc=db.collection("Station"). document(this.getStation()
        ).collection("ChargingMachines").document(String.valueOf(this.getMachine_id()))
                .collection("slots").document(this.getStartTime().toString()+this.getDate().toString());
        doc.set(data);
    }


}
