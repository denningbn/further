package com.example.further;

import static java.lang.Math.abs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.OnSuccessListener;
import java.lang.Math;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class RunActivity extends AppCompatActivity {

    private static final int PERMISSIONS_FINE_LOCATION = 99;
    TextView tv_location, tv_node;

    LocationRequest locationRequest;

    FusedLocationProviderClient fusedLocationProviderClient;

    private LocationCallback locationCallback;

    public boolean trackingLocation;
    //bool to describe whether it's coarse/0 or fine/1
    public boolean coarseFineAccuracy;
    //bool to describe slow/0 or fast/0
    public boolean slowFastInterval;

    public boolean encrypt;

    //runtime location object storage
    private LocationNode first;

    private boolean paused;
    private String startPausedRun, pauseRun;

    Button b_pause, b_end;

    Observable<Settings> settingsObservable;
    Disposable disposable;

    AppDatabase appDatabase;

    SettingsDAO settingsDao;

    Settings settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run);

        //ui elements
        tv_location = findViewById(R.id.tv_location);
        tv_node = findViewById(R.id.tv_node_count);

        b_end = findViewById(R.id.b_end);
        b_pause = findViewById(R.id.b_pause);

        startPausedRun = "Start Paused Run";
        pauseRun = "Pause Run";

        b_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!paused)
                {
                        stopLocationUpdates();
                        paused = true;
                        b_pause.setText(startPausedRun);
                }
                else {
                    startLocationUpdates();
                    paused = false;
                    b_pause.setText(pauseRun);
                }
            }
        });

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        getSettings();

        //by default everything is set to true
        trackingLocation = true;
        coarseFineAccuracy = true;
        slowFastInterval = true;

        locationRequest = createLocReq();

        first = new LocationNode(null);


        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);

                //Here is where we do stuff with the location.

                if (locationResult == null) {
                    tv_location.setText("Location Results are Null");
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    updateGPS();
                    first.addNode(location);
                    testNode();
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
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    protected void onResume(){
        super.onResume();

        if (trackingLocation){
            startLocationUpdates();
        }
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
        double distance = getTotalDistance();

        //TODO
        //implement room database insert
    }

    private void insertRunIntoDatabase(){

    }

    private LocationRequest createLocReq(){
        int priority;
        long interval;

        if (settings.coarseFineAccuracy){
            priority = Priority.PRIORITY_HIGH_ACCURACY;
        }
        else{
            priority = Priority.PRIORITY_LOW_POWER;
        }

        if (settings.slowFastInterval){
            interval = 5 * 1000;
        }
        else{
            interval = 20 * 1000;
        }

        return new LocationRequest.Builder(priority, interval).setMinUpdateIntervalMillis(5000).build();
    }

    private void testNode(){
        tv_node.setText(Integer.toString(first.getLength()));
    }
    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    private void stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    private double getDistanceBetweenTwoPoints(Location loc1, Location loc2){
        if (loc2 == null){
            return 0.0;
        }
        double lat1 =loc1.getLatitude();
        double lon1 = loc1.getLongitude();

        double lat2 = loc2.getLatitude();
        double lon2 = loc2.getLongitude();

        return abs((lat1 - lat2) / (lon1 - lon2));
    }

    private double getTotalDistance(){
        LocationNode<Location> iter = first;
        double sumDistance = 0.0;

        while (iter.getNext() != null){
            sumDistance += getDistanceBetweenTwoPoints(iter.getData(), (Location) iter.getNext().getData());
            iter = iter.getNext();
        }

        return sumDistance;
    }

    public void getSettings(){
        settingsObservable = Observable.create(emitter -> {
            appDatabase = AppDatabaseSingleton.getDatabaseInstance(this);
            settingsDao = appDatabase.settingsDao();

            Settings s = settingsDao.getSettingsById(2);

            emitter.onNext(s);
            emitter.onComplete();
        });

        disposable = settingsObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    settings = s;
                });
    }
}