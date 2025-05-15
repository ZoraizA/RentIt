package com.example.chargex;

import static android.content.ContentValues.TAG;

import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.HashMap;
import java.util.Map;

public class User extends Person {
    private int noOfBookings;

    public User(){
        super();
        noOfBookings=0;
    }

    public void setNoOfBookings(int noOfBookings) {
        this.noOfBookings = noOfBookings;
    }

    public void getAccount(String email,callback async){

        FirebaseFirestore db=FirebaseFirestore.getInstance();
        DocumentReference doc=db.collection("Person").document(email);
        doc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot user_data=task.getResult();
                    if(user_data.exists()){
                        Map<String,Object> data=user_data.getData();
                        if(data.containsKey("name")){
                            Log.d(TAG,"name is set");
                            User.super.setName(data.get("name").toString());
                        }
                        if(data.containsKey("email")){
                            Log.d(TAG,"email is set");
                            User.super.setEmail(data.get("email").toString());
                            //Log.d(TAG,""+user.getEmail());
                        }
                        if(data.containsKey("cnic")){
                            Log.d(TAG,"cnic is set");
                            User.super.setCNIC(data.get("cnic").toString());
                        }
                        if(data.containsKey("DoB")){
                            User.super.setDoB(data.get("DoB").toString());
                        }
                        if(data.containsKey("password")){
                            User.super.setPassword(Decrypt(data.get("password").toString()));
                        }
                        if(data.containsKey("contactNumber")){
                            User.super.setNumber(data.get("contactNumber").toString());
                        }
                        if(data.containsKey("noOfBookings")){
                            setNoOfBookings(Integer.parseInt(data.get("noOfBookings").toString()));
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
        Map <String,Object> data=new HashMap<>();

        data.put("email",this.getEmail());
        data.put("name",this.getName());
        data.put("cnic",this.getCnic());


        data.put("password",Encrypt(this.getPassword()));

        data.put("DoB",this.getDateOfBirth());
        data.put("contactNumber",this.getContactNumber());
        data.put("noOfBookings",this.noOfBookings);
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        DocumentReference doc=db.collection("Person").document(this.getEmail());
        doc.set(data);
    }
}
