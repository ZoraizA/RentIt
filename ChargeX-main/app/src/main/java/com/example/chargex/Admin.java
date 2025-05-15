package com.example.chargex;

import static android.content.ContentValues.TAG;

import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Map;

public class Admin extends Person{
    String address;
    public Admin(){
        super();
        address="";
    }

    public void setAddress(String address){
        this.address=address;
    }
    public String getAddress(){
        return this.address;
    }


    public void getAccount(String email,callback async){

        FirebaseFirestore db=FirebaseFirestore.getInstance();
        DocumentReference doc=db.collection("Admin").document(email);
        doc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot user_data=task.getResult();
                    if(user_data.exists()){
                        Map<String,Object> data=user_data.getData();
                        if(data.containsKey("name")){
                            Log.d(TAG,"name is set");
                            Admin.super.setName(data.get("name").toString());
                        }
                        if(data.containsKey("email")){
                            Log.d(TAG,"email is set");
                            Admin.super.setEmail(data.get("email").toString());
                            //Log.d(TAG,""+user.getEmail());
                        }
                        if(data.containsKey("cnic")){
                            Log.d(TAG,"cnic is set");
                            Admin.super.setCNIC(data.get("cnic").toString());
                        }
                        if(data.containsKey("DoB")){
                            Admin.super.setDoB(data.get("DoB").toString());
                        }
                        if(data.containsKey("password")){
                            Admin.super.setPassword(Decrypt(data.get("password").toString()));
                        }
                        if(data.containsKey("contactNumber")){
                            Admin.super.setNumber(data.get("contactNumber").toString());
                        }
                        if(data.containsKey("address")){
                           setAddress(data.get("address").toString());
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
}
