package com.example.further;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.OnSuccessListener;

public class RunActivity extends AppCompatActivity {

    private static final int PERMISSIONS_FINE_LOCATION = 99;
    TextView tv_location;

    LocationRequest locationRequest;

    FusedLocationProviderClient fusedLocationProviderClient;

    private LocationCallback locationCallback;

    public boolean trackingLocation;
    //bool to describe whether it's coarse/0 or fine/1
    public boolean coarseFineAccuracy;
    //bool to describe slow/0 or fast/0
    public boolean slowFastInterval;

    //runtime location object storage
    private LocationNode first;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run);

        //ui elements
        tv_location = findViewById(R.id.tv_location);

        //create location request
        locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000)
                .setMinUpdateIntervalMillis(5000)
                .build();

        //by default everything is set to true
        trackingLocation = true;
        coarseFineAccuracy = true;
        slowFastInterval = true;

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);

                Location location = locationResult.getLastLocation();
                //Here is where we do stuff with the location.

                if (location != null) {
                    tv_location.setText(location.toString());
                    first.addNode(location);
                }
            }
        };

        if (trackingLocation) {
            startLocationUpdates();
        }
        updateGPS();
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        //fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch(requestCode) {
            case PERMISSIONS_FINE_LOCATION:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    updateGPS();
                }
                else {
                    Toast.makeText(this,"This app requires permission to work properly." ,Toast.LENGTH_SHORT).show();
                }
        }
    }

    private void updateGPS() {
        //create location client
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null){
                        tv_location.setText(location.toString());
                    }
                    else {
                        tv_location.setText("Location is Null as this is not a real phone.");
                    }
                }
            });
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_FINE_LOCATION);
            }
        }
    }

    private void saveRun(){
        LocationNode<Location> iter = first;

        while (iter != null)
        {
            //TODO
            //add all of the location data to the sql database
        }
    }

    private LocationRequest createLocReq(){
        int priority;
        long interval;



        if (coarseFineAccuracy){
            priority = Priority.PRIORITY_HIGH_ACCURACY;
        }
        else{
            priority = Priority.PRIORITY_LOW_POWER;
        }

        if (slowFastInterval){
            interval = 5 * 1000;
        }
        else{
            interval = 20 * 1000;
        }

        return new LocationRequest.Builder(priority, interval).setMinUpdateIntervalMillis(5000).build();
    }
}