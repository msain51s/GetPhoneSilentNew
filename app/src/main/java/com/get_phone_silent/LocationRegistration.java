package com.get_phone_silent;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.get_phone_silent.db.DB_Handler;
import com.get_phone_silent.model.LocationDataModel;
import com.get_phone_silent.utility.GeocodingLocation;
import com.get_phone_silent.utility.Utils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class LocationRegistration extends AppCompatActivity implements LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "LocationActivity";
    private static final long INTERVAL = 1000 * 10;
    private static final long FASTEST_INTERVAL = 1000 * 5;
    TextView tvLocation;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mCurrentLocation;
    String mLastUpdateTime;

    Toolbar toolbar;
    TextView toolbarTitleText;
    EditText address_input_field, latitude_input_field, longitude_input_field;
    AppCompatSpinner distance_spinner;
    View layoutView;
    List<Address> addresses;
    String[] distance_arr;
    Location loc;
    GeocodingLocation locationAddress;
    String locationResult;
    double currentLatitude,currentLongitude;

    DB_Handler db_handler;

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startToGetLocation();
        setContentView(R.layout.activity_location_registration);
        initToolBar();
        locationAddress = new GeocodingLocation();
        db_handler = new DB_Handler(this);
        distance_arr = getResources().getStringArray(R.array.distance_arr);

        toolbarTitleText= (TextView) findViewById(R.id.toolbar_title_text);
        layoutView = findViewById(R.id.layout);
        address_input_field = (EditText) findViewById(R.id.address_input_field);
        latitude_input_field = (EditText) findViewById(R.id.latitude_input_field);
        longitude_input_field = (EditText) findViewById(R.id.longitude_input_field);
        distance_spinner = (AppCompatSpinner) findViewById(R.id.distance_spinner);


        toolbarTitleText.setText("Location Registration");
        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, distance_arr);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        distance_spinner.setAdapter(adapter);
        distance_spinner.setSelection(0);
        distance_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                String selectedDistance = distance_arr[position];
                Toast.makeText(LocationRegistration.this, selectedDistance + " meter", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    /*Method to in initialize toolbar*/
    private void initToolBar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
    public void performGetLocation(View view) {

        String addressValue = address_input_field.getText().toString();
        if (TextUtils.isEmpty(addressValue)) {
            Utils.showValidationPopup(layoutView, "please enter Address to register");
            return;
        }

        GeocodingLocation locationAddress = new GeocodingLocation();
        locationAddress.getLocationFromAddress(addressValue,
                getApplicationContext(), new GeocoderHandler());


    }

    public void performGetCurrentLocation(View view) {
        locationAddress.getAddressFromLocation(currentLatitude, currentLongitude,
                getApplicationContext(), new GeocoderHandler());

    }

    public void performRegisterLocation(View view) {

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                if(locationResult!=null) {
                    String[] locationArr = locationResult.split(":");
                    if (locationArr.length > 0) {
                        LocationDataModel model = new LocationDataModel();
                        model.setAddress(locationArr[0]);
                        model.setLatitude(locationArr[1]);
                        model.setLongitude(locationArr[2]);
                        model.setStatus("Enable");
                        model.setAreaRadius("100");
                        if (!locationArr[1].equalsIgnoreCase(" ") && !locationArr[2].equalsIgnoreCase(" ")) {
                            db_handler.insertLocationData(model);

                            Utils.showCommonAlertDialog(LocationRegistration.this, "Success", "Place Successfully registered for getting your phone silent",
                                    SweetAlertDialog.SUCCESS_TYPE);
                        } else if (TextUtils.isEmpty(address_input_field.getText().toString())) {
                            Utils.showCommonAlertDialog(LocationRegistration.this, "Alert",
                                    "please get location by address or current location", SweetAlertDialog.NORMAL_TYPE);
                        } else {
                            Utils.showCommonAlertDialog(LocationRegistration.this, "Alert",
                                    "Location not found,please retry after getting Location", SweetAlertDialog.NORMAL_TYPE);
                        }
                    }
                }else{
                    Utils.showCommonAlertDialog(LocationRegistration.this, "Alert",
                            "please get location by address or current location", SweetAlertDialog.NORMAL_TYPE);
                }
            }
        });

    }

    public void performGetStoredData(View view) {
        List<LocationDataModel> list = db_handler.getRegisteredLocationDataList();
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                String result = list.get(i).getAddress() + ","
                        + list.get(i).getLatitude() + ","
                        + list.get(i).getLongitude() + ","
                        + list.get(i).getStatus() + ","
                        + list.get(i).getAreaRadius() + " m";
                Toast.makeText(this, result, Toast.LENGTH_LONG).show();
            }
        }
    }

    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String locationAddress;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    locationResult = locationAddress;
                    break;
                default:
                    locationAddress = null;
            }
            String resultArr[] = locationAddress.split(":");
            if (resultArr.length > 2) {
                address_input_field.setText(resultArr[0]);
                latitude_input_field.setText(resultArr[1]);
                longitude_input_field.setText(resultArr[2]);
            }
            //       Toast.makeText(LocationRegistration.this,locationAddress,Toast.LENGTH_LONG).show();
            Log.d("address", locationAddress);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent();
        setResult(121,intent);
    }

    public void startToGetLocation(){
        if (!isGooglePlayServicesAvailable()) {
            finish();
        }
        createLocationRequest();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

                updateUI();

    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart fired ..............");
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop fired ..............");
        mGoogleApiClient.disconnect();
        Log.d(TAG, "isConnected ...............: " + mGoogleApiClient.isConnected());
    }

    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
            return false;
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "onConnected - isConnected ...............: " + mGoogleApiClient.isConnected());
        startLocationUpdates();
    }

    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
        Log.d(TAG, "Location update started ..............: ");
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "Connection failed: " + connectionResult.toString());
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "Firing onLocationChanged..............................................");
        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        updateUI();
    }

    private void updateUI() {
        Log.d(TAG, "UI update initiated .............");
        if (null != mCurrentLocation) {
            currentLatitude = mCurrentLocation.getLatitude();
            currentLongitude = mCurrentLocation.getLongitude();

            Log.d(TAG, "At Time: " + mLastUpdateTime + "\n" +
                    "Latitude: " + currentLatitude + "\n" +
                    "Longitude: " + currentLongitude + "\n" +
                    "Accuracy: " + mCurrentLocation.getAccuracy() + "\n" +
                    "Provider: " + mCurrentLocation.getProvider());
        } else {
            Log.d(TAG, "location is null ...............");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
        Log.d(TAG, "Location update stopped .......................");
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();
            Log.d(TAG, "Location update resumed .....................");
        }
    }
}
