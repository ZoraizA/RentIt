package com.example.chargex;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.Filter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import android.Manifest;

public class ViewStations extends AppCompatActivity implements OnMapReadyCallback {
    private List<Station> stationList;
    private LatLng userLocation;
    private LatLng stationLocation;
    private String date;
    private String endTime;
    private String startTime;
    private Boolean stationsSorted=false;

    GoogleMap map;
    private FusedLocationProviderClient fusedLocationProviderClient;
    public static int LOCATION_REQUEST_CODE = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        stationList=new ArrayList<>();
        date=getIntent().getStringExtra("date");
        startTime=getIntent().getStringExtra("startTime");
        endTime=getIntent().getStringExtra("endTime");
        Log.d(TAG,"start time is:"+startTime);
        Log.d(TAG,"end time is:"+endTime);
        Log.d(TAG,"date is:"+date);


        getMachines(new callback() {


            @Override
            public void onSuccess(String result) {
                if(stationList.size()==0){
                    Log.d("BookSlotActivity", "Stations not found");
                }
                else {
                    Log.d("BookSlotActivity", "Stations Found:"+stationList.size());
                }

                if(stationList.size()==0){
                    Log.d("BookSlotActivity", "Stations not found");
                }
                else {
                    Log.d("BookSlotActivity", "Stations Found");
                }

                for(int i=0;i< stationList.size()-1;i++){
                    for(int j=0;j<stationList.size()-1;j++){
                        double firstValue=Math.sqrt(Math.pow(stationList.get(j).getLatitude()- userLocation.latitude,2)+Math.pow(stationList.get(j).getLongitude()-userLocation.longitude,2));
                        double secondValue=Math.sqrt(Math.pow(stationList.get(j+1).getLatitude()- userLocation.latitude,2)+Math.pow(stationList.get(j+1).getLongitude()-userLocation.longitude,2));
                        if(firstValue>secondValue){
                            Station temp=stationList.get(j);
                            stationList.set(j,stationList.get(j+1));
                            stationList.set(j+1,temp);

                        }
                    }
                }
                stationsSorted=true;
                onMapReady(map);
                //all station with free slots will have their data stored in stationList.
            }

            @Override
            public void onFailure(Exception e) {
                    Log.d(TAG,"data fetch failed");
            }
        });
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_stations);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if(mapFragment!=null){
            mapFragment.getMapAsync(this);}
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        checkLocationPermission();


    }

    private void checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getUserLocation();
        } else {
            requestForpermissions();
        }
    }


        private void requestForpermissions(){
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION},LOCATION_REQUEST_CODE);
        }
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            if(requestCode== LOCATION_REQUEST_CODE){
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this,"Permission Accepted",Toast.LENGTH_SHORT).show();
                    getUserLocation();
                }
                else{
                    Toast.makeText(this,"Permission Rejected",Toast.LENGTH_SHORT).show();
                }
            }
        }



    private void getUserLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>(){
            @Override
            public void onSuccess(Location location){
                if(location!=null){
                    double lat=location.getLatitude();
                    double longitude=location.getLongitude();
                    userLocation=new LatLng(lat,longitude);
                    map.moveCamera(CameraUpdateFactory.newLatLng(userLocation));
                    map.animateCamera(CameraUpdateFactory.zoomTo(12));
                    MarkerOptions options=new MarkerOptions().position(userLocation).title("My location");
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                    map.addMarker(options);

                }
            }
        });
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
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        if(stationsSorted) {
            if (stationList.size() == 0) {
                Log.d("BookSlotActivity", "Stations not found");
            } else {
                Log.d("BookSlotActivity", "Stations Found");
                Log.d("Stations", "Number of stations: " + stationList.size());
                Log.d("Stations", "User Location: " + userLocation.latitude + ", " + userLocation.longitude);



            }

            if (map != null) {
                map.getUiSettings().setMyLocationButtonEnabled(true);
                for (int i = 0; i < 5; i++) {
                    if (i >= stationList.size())
                        break;
                    LatLng location = new LatLng(stationList.get(i).getLatitude(), stationList.get(i).getLongitude());
                    MarkerOptions options;
                    if (i == 0) {
                        options = new MarkerOptions().position(location).title("Closest Station");
                    } else if (i == 1) {
                        options = new MarkerOptions().position(location).title("Second Closest Station");
                    } else if (i == 2) {
                        options = new MarkerOptions().position(location).title("Third Closest Station");
                    } else if (i == 3) {
                        options = new MarkerOptions().position(location).title("Fourth Closest Station");
                    } else {
                        options = new MarkerOptions().position(location).title("Fifth Closest station");
                    }


                    if (i == 0 || i == 1) {
                        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                    } else if (i == 2 || i == 3) {
                        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
                    } else
                        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                    if (i == 0) {
                        stationLocation = new LatLng(stationList.get(i).getLatitude(), stationList.get(i).getLongitude());

                        // Add a polygon
                        PolygonOptions polygonOptions = new PolygonOptions()
                                .add(userLocation, stationLocation)
                                 // You can customize the color
                                .strokeWidth(5);  // You can customize the width

                        map.addPolygon(polygonOptions);
                    }
                    map.addMarker(options);
                    Log.d("Stations", "station Location: " + stationLocation.latitude + ", " + stationLocation.longitude);
                    Log.d("marker", "Marker Added");
                }
            }

            map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    // Update userLocation when the map is clicked
                    userLocation = latLng;
                    map.clear(); // Clear the map to remove previous markers and polygons
                    MarkerOptions options=new MarkerOptions().position(userLocation).title("My location");
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                    map.addMarker(options);
                    for(int i=0;i< stationList.size()-1;i++){
                        for(int j=0;j<stationList.size()-1;j++){
                            double firstValue=Math.sqrt(Math.pow(stationList.get(j).getLatitude()- userLocation.latitude,2)+Math.pow(stationList.get(j).getLongitude()-userLocation.longitude,2));
                            double secondValue=Math.sqrt(Math.pow(stationList.get(j+1).getLatitude()- userLocation.latitude,2)+Math.pow(stationList.get(j+1).getLongitude()-userLocation.longitude,2));
                            if(firstValue>secondValue){
                                Station temp=stationList.get(j);
                                stationList.set(j,stationList.get(j+1));
                                stationList.set(j+1,temp);

                            }
                        }
                    }
                    onMapReady(map); // Add markers and polygon with the updated user location
                    return;
                }
            });

            map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    map.clear();
                    MarkerOptions options=new MarkerOptions().position(userLocation).title("My location");
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                    map.addMarker(options);
                    if (map != null) {
                        map.getUiSettings().setMyLocationButtonEnabled(true);
                        for (int i = 0; i < 5; i++) {
                            if (i >= stationList.size())
                                break;
                            LatLng location = new LatLng(stationList.get(i).getLatitude(), stationList.get(i).getLongitude());

                            if (i == 0) {
                                options = new MarkerOptions().position(location).title("Closest Station");
                            } else if (i == 1) {
                                options = new MarkerOptions().position(location).title("Second Closest Station");
                            } else if (i == 2) {
                                options = new MarkerOptions().position(location).title("Third Closest Station");
                            } else if (i == 3) {
                                options = new MarkerOptions().position(location).title("Fourth Closest Station");
                            } else {
                                options = new MarkerOptions().position(location).title("Fifth Closest station");
                            }


                            if (i == 0 || i == 1) {
                                options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                            } else if (i == 2 || i == 3) {
                                options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
                            } else
                                options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                            if (i == 0) {
                                stationLocation = marker.getPosition();

                                // Add a polygon
                                PolygonOptions polygonOptions = new PolygonOptions()
                                        .add(userLocation, stationLocation)
                                        // You can customize the color
                                        .strokeWidth(5);  // You can customize the width

                                map.addPolygon(polygonOptions);
                            }
                            map.addMarker(options);
                            Log.d("Stations", "station Location: " + stationLocation.latitude + ", " + stationLocation.longitude);
                            // Return false to indicate that we have not consumed the event and that we wish for the default behavior to occur

                        }
                    }
                    return false;}
            });
        }


    }

    private float getColorForMarker(int markerIndex) {
        switch (markerIndex) {
            case 0:
                return BitmapDescriptorFactory.HUE_RED;
            case 1:
                return BitmapDescriptorFactory.HUE_BLUE;
            case 2:
                return BitmapDescriptorFactory.HUE_GREEN;
            case 3:
                return BitmapDescriptorFactory.HUE_YELLOW;
            case 4:
                return BitmapDescriptorFactory.HUE_MAGENTA;
            default:
                return BitmapDescriptorFactory.HUE_RED;
        }
    }

    public void showMachines(View v){
        Intent i=new Intent(this,showMachinesUser.class);
        Log.d(TAG,"long"+stationLocation.longitude);
        Log.d(TAG,"lat:"+stationLocation.latitude);
        i.putExtra("date",date);
        i.putExtra("startTime",startTime);
        i.putExtra("endTime",endTime);

        i.putExtra("longitude",stationLocation.longitude);
        i.putExtra("latitude",stationLocation.latitude);
        startActivity(i);
    }

    // Create a custom BitmapDescriptor with the specified color

}