package com.example.chargex;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class setLocation extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap map;
    private LatLng stationLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_location);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public void onMapReady(@NonNull GoogleMap googleMap){
        map=googleMap;
        LatLng lahore=new LatLng(31.5497,74.3436);
        float zoom=15.0f;
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(lahore, zoom));
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                // You can adjust the zoom level as needed

                // Move camera to Lahore with the specified zoom level
               ;
                map.clear();


                stationLocation=latLng;
                map.addMarker(new MarkerOptions()
                        .position(stationLocation)
                        .title("station"));
            }
        });
    }
    public void apply(View v){
        Station station=new Station();
        if(stationLocation!=null) {
            //station.setLongitude(stationLocation.latitude);
            //station.setLatitude(stationLocation.longitude);//set longitude and latitude here
            SharedPreferences preferences = getSharedPreferences("user_data", Context.MODE_PRIVATE);
            String username=preferences.getString("username","");
            Log.d(TAG,"username is:"+username);
            station.getAccount(username, new callback() {
                        @Override
                        public void onSuccess(String result) {
                            station.setLongitude(stationLocation.longitude);
                            station.setLatitude(stationLocation.latitude);
                            station.setData();
                            goProfile();
                        }

                        @Override
                        public void onFailure(Exception e) {
                            Log.d(TAG,"station fetching failed");
                            goProfile();
                        }
                    });
                    //Log.d(TAG,"Longitude is "+ station.getLongitude());

        }

        //add name,email,address,contact Number text fields and update btn in activity_seller_profile.xml

    }
    public void goProfile(){
        Intent i=new Intent(getApplicationContext(),StationProfile.class);
        startActivity(i);
    }


}