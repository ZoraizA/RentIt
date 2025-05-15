package com.example.chargex;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
public class ViewLocation extends AppCompatActivity implements OnMapReadyCallback {
    private LatLng location;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        double latitude;
        double longitude;

        Intent i=getIntent();
        latitude=i.getDoubleExtra("latitude",10);
        longitude=i.getDoubleExtra("longitude",10);
        location = new LatLng(latitude,longitude);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_location);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        if(location!=null){
            googleMap.addMarker(new MarkerOptions()
                    .position(location)
                    .title("here"));
        }
    }
    public void finish(View v){
        finish();
    }
}