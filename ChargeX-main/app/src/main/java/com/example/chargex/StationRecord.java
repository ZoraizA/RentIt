package com.example.chargex;

import android.content.Intent;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class StationRecord extends Station{
    List<ChargingMachine> machineList;

    public StationRecord(){
        super();
        machineList=new ArrayList<>();
    }
    public List<ChargingMachine> getMachineList(){return this.machineList;}

    public void getMachines(callback async){
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        db.collection("Station").document(this.getName()).
                collection("ChargingMachines").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            final int[] stations_fetched = {0};
                            int tot_tasks=task.getResult().size();
                            final int[] done = {0};
                            for(QueryDocumentSnapshot doc: task.getResult()){
                                ChargingMachine machine=new ChargingMachine();
                                if(doc.contains("id")){
                                    machine.setId(Integer.parseInt( doc.getData().get("id").toString()));
                                }
                                if(doc.contains("chargingSpeed")){
                                    machine.setChargingSpeed(Double.parseDouble(doc.
                                            getData().get("chargingSpeed").toString()));
                                }
                                if(doc.contains("price")){
                                    machine.setPrice(Double.parseDouble(
                                            doc.getData().get("price").toString()
                                    ));
                                }
                                if(doc.contains("station")){
                                    stations_fetched[0] =1;
                                    Station station=new Station();
                                    station.getAccount(doc.getData().get("station").toString(),
                                            new callback() {
                                                @Override
                                                public void onSuccess(String result) {
                                                    stations_fetched[0] =0;
                                                    machine.setStation(station);
                                                    machineList.add(machine);
                                                    done[0]++;
                                                    if(tot_tasks==done[0]){
                                                        async.onSuccess("All machines fetched");
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Exception e) {

                                                }
                                            });
                                }
                                else{
                                    machineList.add(machine);
                                    done[0]++;
                                }

                            }

                        }
                        else{
                            async.onFailure(new Exception("Task failed"));
                        }
                    }
                });
    }

    public Station chooseChargingStation(){
        return new Station();
    }

    public void sortStations(){

    }
}
