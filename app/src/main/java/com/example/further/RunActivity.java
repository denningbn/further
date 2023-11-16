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
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class RunActivity extends AppCompatActivity {

    private static final int PERMISSIONS_FINE_LOCATION = 99;
    TextView tv_node, tv_runid;

    LocationRequest locationRequest;

    FusedLocationProviderClient fusedLocationProviderClient;

    private LocationCallback locationCallback;

    public boolean trackingLocation;
    //bool to describe whether it's coarse/0 or fine/1

    //runtime location object storage
    private LocationNode<Location> first;
    private LocationNode<Location> last;

    private boolean paused;
    private String startPausedRun, pauseRun;

    Button b_pause, b_end;

    Observable<Settings> settingsObservable;
    Observable<Run> runObservable;
    Disposable disposable, runDisposable;

    AppDatabase appDatabase;

    SettingsDAO settingsDao;

    RunDAO runDao;

    Settings currentSettings;

    float dis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run);

        //ui elements
        tv_runid = findViewById(R.id.tv_runid);
        b_end = findViewById(R.id.b_end);
        b_pause = findViewById(R.id.b_pause);


        initVars();

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

        b_end.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                stopLocationUpdates();
                saveRun();
            }
        });




        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);

                if (locationResult == null) {
                    return;
                }
                locLoop(locationResult.getLocations());
            }
        };

        initTracking();
        updateGPS();
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
                    }
                    else {
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
        Run run = new Run(dis);

        runObservable = Observable.create(emitter -> {
            appDatabase = AppDatabaseSingleton.getDatabaseInstance(this);
            runDao = appDatabase.runDao();

            long id = runDao.insert(run);

            runDao.getRunById(id);

            emitter.onNext(run);
            emitter.onComplete();
        });

        runDisposable = runObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    tv_runid.setText(s.toString());
                });
    }

    private LocationRequest createLocReq(){
        int priority;
        long interval;

        if (currentSettings.coarseFineAccuracy){
            priority = Priority.PRIORITY_HIGH_ACCURACY;
        }
        else{
            priority = Priority.PRIORITY_LOW_POWER;
        }

        if (currentSettings.slowFastInterval){
            interval = 5 * 1000;
        }
        else{
            interval = 20 * 1000;
        }

        return new LocationRequest.Builder(priority, interval).setMinUpdateIntervalMillis(5000).build();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    private void stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
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
                    currentSettings = s;
                });
    }

    private void addLocation(Location location){
        last = first.addNode(location);
    }

    private void initVars(){
        //initiate all variables at the beginning of the function
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        first = new LocationNode(null);

        currentSettings = new Settings();
        getSettings();

        locationRequest = createLocReq();

        dis = 0;

        startPausedRun = "Start Paused Run";
        pauseRun = "Pause Run";

        trackingLocation = true;
    }

    private void initTracking(){
        if (trackingLocation) {
            startLocationUpdates();
        }
    }

    private void locLoop(List<Location> locationsGotten){
        for (Location location : locationsGotten) {
            updateGPS();
            addLocation(location);
            if ((last.getData() != null) & (last.getPrev().getData() != null)) {
                dis += (float) metersToMiles(last.getData().distanceTo(last.getPrev().getData()));

                tv_runid.setText(Float.toString(roundDistance(dis)));
            }
        }
    }

    private double metersToMiles(float meters){
        return meters / 1609.34;
    }

    private float roundDistance(float value){
        return Math.round(value) * 100 / 100f;
    }
}