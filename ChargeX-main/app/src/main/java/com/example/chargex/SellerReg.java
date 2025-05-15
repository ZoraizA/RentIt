package com.example.chargex;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class SellerReg extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_reg);
    }
    public void register(View v){
        Station station=new Station();
        TextView dummy=findViewById(R.id.stationName);
        station.setName(dummy.getText().toString());
        dummy=findViewById(R.id.stationEmail);
        station.setEmail(dummy.getText().toString());
        dummy=findViewById(R.id.stationPassword);
        station.setPassword(dummy.getText().toString());
        String email=station.getEmail();
        boolean isPresent=false;
        for(int i=0;i<email.length();i++) {
            if(email.charAt(i)=='@')
                isPresent=true;
        }
        if(!isPresent) {
            Toast.makeText(SellerReg.this,"Invalid email format.@ is missing",Toast.LENGTH_SHORT).show();
            return;
        }

        String ending=".com";
        int index=0;
        for(int i=email.length()-4;i<email.length();i++) {
            if(email.charAt(i)!=ending.charAt(index)){
                Toast.makeText(SellerReg.this,"Invalid email.It should end with .com",Toast.LENGTH_SHORT).show();
                return;
            }
            index++;
        }
        String pw=station.getPassword();
        if(pw.length()<8) {
            Toast.makeText(SellerReg.this,"Password has to be atleast 8 characters",Toast.LENGTH_SHORT).show();
            return;
        }
        station.setData();

    }
    public void login(View v){
        Intent login=new Intent(getApplicationContext(),StationLogin.class);
        startActivity(login);
    }

}