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

public class AdminLogIn extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_log_in);
    }
    public void LogIn(View v){
        Admin admin=new Admin();
        TextView email=findViewById(R.id.adminEmail);
        String email_=email.toString();
        if(email.length()==0){
            Toast.makeText(AdminLogIn.this,"Enter email", Toast.LENGTH_SHORT).show();
            return;
        }
        admin.getAccount(email.getText().toString(), new callback() {
            @Override
            public void onSuccess(String result) {
                TextView input=findViewById(R.id.adminPassword);
                String pw=input.getText().toString();
                if(pw.length()==0){
                    Toast.makeText(AdminLogIn.this,"Enter password", Toast.LENGTH_SHORT).show();
                    return;
                }
                Log.d(TAG,"logIn done");
                Log.d(TAG,"pw is:"+admin.getPassword());
                if(admin.getPassword().equals(input.getText().toString())){
                    SharedPreferences preferences = getSharedPreferences("user_data", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("loggedIn", "True");
                    editor.putString("role","admin");
                    editor.putString("username",admin.getEmail());
                    editor.apply();
                    Intent index=new Intent(getApplicationContext(),AdminIndex.class);
                    startActivity(index);
                }
                else{
                    Toast.makeText(AdminLogIn.this,"Account not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Exception e) {
                TextView input=findViewById(R.id.adminPassword);
                String pw=input.toString();
                if(pw.length()==0){
                    Toast.makeText(AdminLogIn.this,"Enter password", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(AdminLogIn.this,"Account not found", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void signUp(View v){
        Intent profile=new Intent(getApplicationContext(),SignUp.class);
        startActivity(profile);
    }
}