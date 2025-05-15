package com.example.chargex;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChargingMachine {
    private int id;
    private String status;
    private double chargingSpeed;
    private double price;
    private List<Slot> slotsList;
    private Station station;
    public ChargingMachine(){
        status="Free";
        chargingSpeed=0.0;
        price=500;
        slotsList= new ArrayList<>();
    }
    public void setId(int Id){
        this.id=Id;
    }
    public void setStatus(String status){
        this.status=status;
    }
    public void setChargingSpeed(double chargingSpeed){
        this.chargingSpeed=chargingSpeed;
    }
    public void setStation(Station station){
        this.station=station;
    }
    public void setPrice(double Price){this.price=Price;}

    public int getId(){
        return this.id;
    }
    public double getPrice(){return this.price;}
    public String getStatus(){
        return this.status;
    }
    public double getChargingSpeed(){
        return this.chargingSpeed;
    }
    public Station getStation(){
        return this.station;
    }
    public void setData(){
        Map<String,Object> data=new HashMap<>();
        //int id=this.getId();
        data.put("id",this.getId());

        data.put("status",this.getStatus());
        data.put("chargingSpeed",this.getChargingSpeed());
        data.put("price",this.getPrice());
        data.put("station",this.getStation().getName());

        FirebaseFirestore db=FirebaseFirestore.getInstance();
        DocumentReference doc=db.collection("Station"). document(this.getStation().getName()
                ).collection("ChargingMachines").document(String.valueOf(this.getId()));
        doc.set(data);

        Station station=new Station();
        station.getAccount(this.getStation().getName().toString(), new callback() {
            @Override
            public void onSuccess(String result) {
                station.setMachineCount(station.getMachineCount()+1);
                station.setData();
            }

            @Override
            public void onFailure(Exception e) {

            }
        });

    }
    /*public void setSlotData(String station){
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        for(int i=0;i<slotsList.size();i++){
            Map<String ,Object> data=new HashMap<>();
            data.put("startTime",slotsList.get(i).getStartTime());
            data.put("endTime",slotsList.get(i).getEndTime());
            DocumentReference doc=db.collection("Station").document(station)
                    .collection("ChargingMachines").document(String.valueOf(this.getId()))
                    .collection("slots").document(slotsList.get(i).getStartTime());
            doc.set(data);
        }
    }*/
    public void addSlot(Slot slot){
        this.slotsList.add(slot);
    }


}
