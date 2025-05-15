package com.example.chargex;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.HashMap;
import java.util.Map;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.arch.core.executor.ArchTaskExecutor;

public class Station  {


    private String email;
    private int machineCount;
    private String password;
    private String address;
    private double longitude;
    private double latitude;
    private String contactNumber;
    private double avg_rating;

    private String status;
    private String name;
    public Station(){
        name="";
        email="";
        contactNumber="";
        password="";
        address="";
        status="unVerified";
        avg_rating=0.0;
        latitude=0.0;
        longitude=0.0;
        machineCount=0;
    }
    public void setName(String name){
        this.name=name;
    }
    public void setEmail(String email){
        this.email=email;
    }
    public void setNumber(String number){
        this.contactNumber=number;
    }
    public void setPassword(String pw){
        this.password=pw;
    }
    public void setStatus(String status){
        this.status=status;
    }
    public void setAddress(String address){
        this.address=address;
    }
    public void setRating(double rating){
        this.avg_rating=rating;
    }

    public void setLongitude(double longitude){this.longitude=longitude;}

    public void setLatitude(double latitude){this.latitude=latitude;}

    public void setMachineCount(int machineCount){
        this.machineCount=machineCount;
    }
    public int getMachineCount(){
        return this.machineCount;
    }
    public String getName(){
        return this.name;
    }
    public String getEmail(){
        return this.email;
    }
    public String getStatus(){
        return this.status;
    }
    public String getContactNumber(){
        return this.contactNumber;
    }
    public String getAddress(){
        return this.address;
    }
    public double getRating(){
        return this.avg_rating;
    }
    public String getPassword(){
        return this.password;
    }
    public double getLongitude(){return this.longitude;}
    public double getLatitude(){return this.latitude;}

    public String Encrypt(String pw){
        char[] arr=pw.toCharArray();
        for(int i=0;i<arr.length;i++){
            if(i%3==0) {
                if(i<6)
                    arr[i]=(char)(((int) arr[i])-i);
                else
                    arr[i]=(char)(((int) arr[i])-5);
            }
            else if(i%3==1){
                if(i<5)
                    arr[i]=(char)(((int) arr[i])-3);
                else
                    arr[i]=(char)(((int) arr[i])-4);
            }
            else {
                if(i%2==0){
                    arr[i]=(char)(((int) arr[i])-2);
                }
                else
                    arr[i]=(char)(((int) arr[i])-1);
            }

        }
        pw=String.valueOf(arr);
        return pw;
    }

    public String Decrypt(String pw){
        char[] arr=pw.toCharArray();
        for(int i=0;i<arr.length;i++){
            if(i%3==0) {
                if(i<6)
                    arr[i]=(char)(((int) arr[i])+i);
                else
                    arr[i]=(char)(((int) arr[i])+5);
            }
            else if(i%3==1){
                if(i<5)
                    arr[i]=(char)(((int) arr[i])+3);
                else
                    arr[i]=(char)(((int) arr[i])+4);
            }
            else {
                if(i%2==0){
                    arr[i]=(char)(((int) arr[i])+2);
                }
                else
                    arr[i]=(char)(((int) arr[i])+1);
            }

        }
        pw=String.valueOf(arr);
        return pw;
    }

    public void getAccount(String name,callback async){

        FirebaseFirestore db=FirebaseFirestore.getInstance();
        DocumentReference doc=db.collection("Station").document(name);
        doc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot user_data=task.getResult();
                    if(user_data.exists()){
                        Map<String,Object> data=user_data.getData();
                        if(data.containsKey("name")){
                            Log.d(TAG,"name is set");
                            setName(data.get("name").toString());
                        }
                        if(data.containsKey("email")){
                            Log.d(TAG,"email is set");
                            setEmail(data.get("email").toString());
                            //Log.d(TAG,""+user.getEmail());
                        }
                        if(data.containsKey("longitude")){
                            Log.d(TAG,"longitude is set");
                            setLongitude((double)(data.get("longitude")));
                        }
                        if(data.containsKey("machineCount")){
                            Log.d(TAG,"longitude is set");
                            setMachineCount(Integer.parseInt(data.get("machineCount").toString()));
                        }
                        if(data.containsKey("latitude")){
                            Log.d(TAG,"latitude is set");
                            setLatitude((double)(data.get("latitude")));
                        }
                        if(data.containsKey("address")){
                            setAddress(data.get("address").toString());
                        }
                        if(data.containsKey("address")){
                            setAddress(data.get("address").toString());
                        }
                        if(data.containsKey("contactNumber")){
                            setNumber(data.get("contactNumber").toString());
                        }
                        if(data.containsKey("password")){
                            setPassword(Decrypt(data.get("password").toString()));
                        }
                        if(data.containsKey("status")){
                            setStatus(data.get("status").toString());
                        }
                        async.onSuccess("Data set!");
                    }
                    else{
                        async.onFailure(new Exception("Document not found!"));
                    }
                }
                else{

                    async.onFailure(task.getException());
                }
            }
        });

    }
    public void setData(){
        Map<String,Object> data=new HashMap<>();
       String email=this.getEmail();



        data.put("email",this.getEmail());

        data.put("name",this.getName());
        data.put("address",this.getAddress());


        data.put("password",Encrypt(this.getPassword()));
        data.put("contactNumber",this.getContactNumber());
        data.put("longitude",this.getLongitude());
        data.put("latitude",this.getLatitude());
        data.put("status",this.getStatus());
        data.put("machineCount",this.getMachineCount());
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        DocumentReference doc=db.collection("Station").document(this.getName());
        doc.set(data);
    }
}
