package com.orionsoft.vsafe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.orionsoft.vsafe.model.Case;
import com.orionsoft.vsafe.model.Department;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ReportCaseDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    String dateTime;
    String tempDeparts = "";
    String tempLatitude = "";
    String tempLongitude = "";

    private EditText edTxtRCDetailDate;
    private EditText edTxtRCDetailSituation;
    private EditText edTxtRCDetailDepart;
    private EditText edTxtRCDetailDetails;
    private EditText edTxtRCDetailLocation;

    private Button btnRCDetailFImg;
    private Button btnRCDetailBImg;
    private Button btnRCDetailSubmit;

    Case aCase;
    Department department;

    // Initializing FusedLocationProviderClient object
    FusedLocationProviderClient mFusedLocationClient;
    int PERMISSION_ID = 44;

//        -----------------------------------------------------------------------------------------------

    //    Activity wide interface - onClick() method
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnRCDetailFImg:

            default:
                break;
        }
    }

//        -----------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_case_details);

        edTxtRCDetailDate = findViewById(R.id.edTxtRCDetailDate);
        edTxtRCDetailSituation = findViewById(R.id.edTxtRCDetailSituation);
        edTxtRCDetailDepart = findViewById(R.id.edTxtRCDetailDepart);
        edTxtRCDetailDetails = findViewById(R.id.edTxtRCDetailDetails);
        edTxtRCDetailLocation = findViewById(R.id.edTxtRCDetailLocation);

        btnRCDetailFImg = findViewById(R.id.btnRCDetailFImg);
        btnRCDetailBImg = findViewById(R.id.btnRCDetailBImg);
        btnRCDetailSubmit = findViewById(R.id.btnRCDetailSubmit);

        // Instantiate the setOnClickListener(s) at runtime
        btnRCDetailFImg.setOnClickListener(this);
        btnRCDetailBImg.setOnClickListener(this);
        btnRCDetailSubmit.setOnClickListener(this);

        // Retrieve objects from the previous activity
        aCase = (Case) getIntent().getSerializableExtra("caseObj");
        department = (Department) getIntent().getSerializableExtra("departObj");

        // The current date and time
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateTime = dateFormat.format(Calendar.getInstance().getTime());

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        // Method to get the location
        getLastLocation();

        setDataOnStartup(); // Update fields with data on startup
    }

//        -----------------------------------------------------------------------------------------------

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

//        -----------------------------------------------------------------------------------------------

    // Update fields with data on startup
    private void setDataOnStartup() {
        edTxtRCDetailDate.setText(dateTime);
        edTxtRCDetailSituation.setText(aCase.getSituation());
        edTxtRCDetailDepart.setText(getSelectedDeparts());

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                edTxtRCDetailLocation.setText(tempLatitude + "," + tempLongitude);
            }
        }, 3000);
    }

//        -----------------------------------------------------------------------------------------------

    // Get selected departments to a string
    private String getSelectedDeparts() {
        if (department.getPolice() == 1) {
            tempDeparts += "Police, ";
        }
        if (department.getHospital() == 1) {
            tempDeparts += "Hospital, ";
        }
        if (department.getFireBrigade() == 1) {
            tempDeparts += "Fire Brigade, ";
        }
        if (department.getDmc() == 1) {
            tempDeparts += "DMC, ";
        }
        if (department.getMwca() == 1) {
            tempDeparts += "MWCA, ";
        }

        return tempDeparts.substring(0, tempDeparts.length() - 2);
    }

//        -----------------------------------------------------------------------------------------------
//        -----------------------------------------------------------------------------------------------
//        -----------------------------------------------------------------------------------------------

    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        // Check if permissions are given
        if (checkPermissions()) {

            // Check if location is enabled
            if (isLocationEnabled()) {

                // Getting last location from FusedLocationClient object
                mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if (location == null) {
                            requestNewLocationData();
                        } else {
                            tempLatitude = String.valueOf(location.getLatitude());
                            tempLongitude = String.valueOf(location.getLongitude());
                        }
                    }
                });
            } else {
                Toast.makeText(this, "Please turn on" + " your location...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            // If permissions aren't available, request for permissions
            requestPermissions();
        }
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        // Initializing LocationRequest object with appropriate methods
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        // setting LocationRequest
        // on FusedLocationClient
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }

    private LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            tempLatitude = String.valueOf(mLastLocation.getLatitude());
            tempLongitude = String.valueOf(mLastLocation.getLongitude());
        }
    };

    // Method to check for permissions
    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        // If we want background location on Android 10.0 and higher, use: ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    // Method to request for permissions
    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
    }

    // Method to check if location is enabled
    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    // If everything is alright then
    @Override
    public void
    onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (checkPermissions()) {
            getLastLocation();
        }
    }

//        -----------------------------------------------------------------------------------------------
//        -----------------------------------------------------------------------------------------------
//        -----------------------------------------------------------------------------------------------

    
}