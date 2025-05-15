package com.example.chargex;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.Filter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Test extends AppCompatActivity {
    private List<Station> stationList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        stationList=new ArrayList<>();
        getMachines(new callback() {


            @Override
            public void onSuccess(String result) {
                if (stationList.size() == 0) {
                    Log.d("BookSlotActivity", "Stations not found");
                } else {
                    Log.d("BookSlotActivity", "Stations Found"+stationList.size());
                }

                if (stationList.size() == 0) {
                    Log.d("BookSlotActivity", "Stations not found");
                } else {
                    Log.d("BookSlotActivity", "Stations Found");
                }
            }
            @Override
            public void onFailure(Exception e) {
                Log.d(TAG,"data fetch failed");
            }
        });
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
    }
    public void getMachines(callback async){
        FirebaseFirestore db= FirebaseFirestore.getInstance();
        db.collection("Station")
                .where(Filter.and(Filter.equalTo("status","verified"),
                        Filter.greaterThan("machineCount",0)))
                 .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            int tot_tasks=task.getResult().size();
                            Log.d(TAG,"total tasks are"+tot_tasks);
                            final int[] tasks_completed = {0};
                            for(QueryDocumentSnapshot doc:task.getResult()){
                                Station station=new Station();
                                //Log.d(TAG,"documnet retrived is:"+doc.getData().get("station").toString());
                                station.getAccount(doc.getData().get("name").toString(), new callback() {
                                    @Override
                                    public void onSuccess(String result) {
                                        stationList.add(station);
                                        tasks_completed[0]++;
                                        if(tasks_completed[0]==tot_tasks){
                                            async.onSuccess("All stations added");
                                        }
                                        else{
                                            async.onFailure(new Exception("data not fetched"));
                                        }
                                    }

                                    @Override
                                    public void onFailure(Exception e) {

                                    }
                                });
                            }
                        }
                    }
                });
    }
}