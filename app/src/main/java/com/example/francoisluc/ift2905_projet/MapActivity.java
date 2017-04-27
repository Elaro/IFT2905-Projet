package com.example.francoisluc.ift2905_projet;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


/**
 * Created by a on 2017-04-24.
 */

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap myMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_bixi);
        setContentView(R.layout.content_docks);
        setContentView(R.layout.content_itinerary);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //myMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        //onMapReady(myMap);
    }

    @Override
    public void onMapReady(GoogleMap myMap) {

        LatLng Montreal = new LatLng(-34, 151);
        myMap.addMarker(new MarkerOptions().position(Montreal).title("Marker_Montreal"));
        myMap.moveCamera(CameraUpdateFactory.newLatLng(Montreal));


    }


}
