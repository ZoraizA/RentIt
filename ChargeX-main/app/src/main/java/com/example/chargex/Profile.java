package com.example.chargex;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firestore.admin.v1.Index;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Profile extends AppCompatActivity {
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences = getSharedPreferences("user_data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        String username=preferences.getString("username","");
        setContentView(R.layout.activity_profile);
        updateUI();
    }
    public void updateUI() {
        SharedPreferences preferences = getSharedPreferences("user_data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        String username=preferences.getString("username","");
        user=new User();
        user.getAccount(username, new callback() {
                    @Override
                    public void onSuccess(String result) {
                        TextView dummy=findViewById(R.id.Name);
                        if(user.getName()!=null) {
                            dummy.setText(user.getName());
                        }
                        dummy=findViewById(R.id.email);
                        if(user.getEmail()!=null) {
                            dummy.setText(user.getEmail());
                        }

                        dummy = findViewById(R.id.cnic);
                        if(user.getCnic()!=null) {
                            dummy.setText(user.getCnic());
                        }
                        dummy=findViewById(R.id.DoB);
                        if(user.getDateOfBirth()!=null) {
                            dummy.setText(user.getDateOfBirth());
                        }
                    }

                    @Override
                    public void onFailure(Exception e) {

                    }
                });

        Log.d(TAG,"data fetched");

        Log.d(TAG,"data set");
    }
    public void update(View v){
        TextView t=findViewById(R.id.Name);
        user.setName(t.getText().toString());

        t=findViewById(R.id.email);
        user.setEmail(t.getText().toString());
        t=findViewById(R.id.cnic);
        user.setCNIC(t.getText().toString());
        if(user.getCnic().length()!=13){
            Toast.makeText(this, "CNIC must be 13 digits", Toast.LENGTH_SHORT).show();
            return;
        }

        t=findViewById(R.id.DoB);
        user.setDoB(t.getText().toString());
        String email=user.getEmail();
        boolean isPresent=false;
        for(int i=0;i<email.length();i++) {
            if(email.charAt(i)=='@')
                isPresent=true;
        }
        if(!isPresent) {
            Toast.makeText(Profile.this,"Invalid email format.@ is missing",Toast.LENGTH_SHORT).show();
            return;
        }

        String ending=".com";
        int index=0;
        for(int i=email.length()-4;i<email.length();i++) {
            if(email.charAt(i)!=ending.charAt(index)){
                Toast.makeText(Profile.this,"Invalid email.It should end with .com",Toast.LENGTH_SHORT).show();
                return;
            }
            index++;
        }
        /*String pw=station.getPassword();
        if(pw.length()<8) {
            Toast.makeText(StationProfile.this,"Password has to be atleast 8 characters",Toast.LENGTH_SHORT).show();
            return;
        }*/
        user.setData();
        Intent idx=new Intent(getApplicationContext(), CustomerIndex.class);
        startActivity(idx);

    }



}