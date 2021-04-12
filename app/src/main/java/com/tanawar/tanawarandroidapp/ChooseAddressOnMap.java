package com.tanawar.tanawarandroidapp;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import java.io.IOException;
import java.util.Locale;

import apis.FirebaseConstants;
import helper_tools.TanawarAlertDialog;
import interfaces.InitializeComponents;
public class ChooseAddressOnMap extends FragmentActivity implements InitializeComponents,
                    OnMapReadyCallback {
    private TextView edtAddress;
    private Button btnChooseAddress;
    private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    private Marker myLocationMarker;
    // address data variables
    private double lat;
    private double lng;
    private String addressAsString;
    private boolean isAddressChoosed = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_address_on_map);
        initializeActivityComponents();
        initializeComponentsListeners();
    }

    @Override
    public void initializeComponentsListeners() {
            btnChooseAddress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(addressIsValid()) {
                        // go to register activity with address data
                        Intent i = new Intent();
                        i.putExtra(FirebaseConstants.Collections.Users.ADDRESS, addressAsString);
                        i.putExtra(FirebaseConstants.Collections.Users.LATITUDE, lat);
                        i.putExtra(FirebaseConstants.Collections.Users.LONGITUDE, lng);
                        setResult(2, i);
                        finish();//finishing activity
                    }else{
                        showAddressInvalidDialog();
                    }
                }
            });
    }

    @Override
    public void initializeActivityComponents() {
        edtAddress = findViewById(R.id.edtAddressOnChooseAddressActivity);
        btnChooseAddress = findViewById(R.id.btnChooseAddress);
        initMapFragment();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //mMap.setMyLocationEnabled(true);
        // Add a marker in Sydney and move the camera
        LatLng myLocation = new LatLng(31.772120, 35.102857 );
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 10.0f));
        myLocationMarker = mMap.addMarker(new MarkerOptions().position(myLocation)
                .title("Your Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation));
        // Enable the zoom controls for the map
        mMap.getUiSettings().setZoomControlsEnabled(true);
        //addressWasChosen(myLocation);
        addGoogleMapListener();
    }
    /**
     *
     */
    private void addGoogleMapListener() {
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                addressWasChosen(latLng);
            }
        });
    }
    private void addressWasChosen(LatLng latLng){
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            android.location.Address address = geocoder.getFromLocation(
                    latLng.latitude, latLng.longitude , 1).get(0);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10.0f));
            // save these data in the variables
            String city = address.getLocality();
            String street = address.getThoroughfare();
            if(city == null || city.isEmpty()){
                showAddressDialog("Location Invalid","Please choose another " +
                        "location, Or zoom in more in the map to choose more " +
                        "accurate location.");
            }else if(street == null || street.isEmpty()) {
                saveAddressDateInVariables(city,latLng,true);
            }else{
                saveAddressDateInVariables(city +" - "+street,latLng,true);
            }
        } catch (Exception e) {
            new TanawarAlertDialog().showSimpleDialog("Failed to choose address!",
                    "Error Occurred, try to choose your address again!: ",
                    ChooseAddressOnMap.this).show();
        }
    }
    /**
     * helper method used to update the address value in the address variables.
     * @param address the address city and street.
     * @param latLng the address latitude and longitude.
     * @param b if the address was chosen b is true, else b is false.
     */
    private void saveAddressDateInVariables(String address, LatLng latLng, boolean b) {
        addressAsString = address;
        edtAddress.setText(addressAsString);
        myLocationMarker.setPosition(latLng);
        isAddressChoosed = b;
        lat = latLng.latitude;
        lng = latLng.longitude;
    }
    private void initMapFragment() {
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);

    }
    /**
     *
     * @return
     */
    private boolean addressIsValid() {
        String address = edtAddress.getText().toString();
        if(address == null || address.isEmpty() || !isAddressChoosed || lat == 0 || lng == 0){
            showAddressInvalidDialog();
            return false;
        }
        return true;
    }
    /**
     *
     */
    private void showAddressInvalidDialog(){
        new TanawarAlertDialog().showSimpleDialog("Address Invalid",
                "The address is invalid, please press your address then click the search icon" +
                        "and choose your address on the map.",
                ChooseAddressOnMap.this).show();
    }
    /**
     *
     */
    private void showAddressDialog(String title, String message){
        new TanawarAlertDialog().showSimpleDialog(title,message,
                ChooseAddressOnMap.this).show();
    }
}
