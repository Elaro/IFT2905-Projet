package com.example.francoisluc.ift2905_projet;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Rosalie on 2017-04-27.
 */

public class MapFragment extends Fragment {

    private MapView map;

    public MapFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.map_fragment, container, false);

        if (GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity().getApplicationContext()) == ConnectionResult.SUCCESS) {

            //Initialze mapview
            MapsInitializer.initialize(getActivity());

            map = (MapView) view.findViewById(R.id.mapView);
            map.onCreate(savedInstanceState);

            //GoogleMap gMap = map.getMap();
            //gMap.addMarker(new MarkerOptions().title("title").snippet("description").position(new LatLng(49.5594950, -1.8414880))).showInfoWindow();

        } else {
            Toast.makeText(getActivity(), "Please install google play services", Toast.LENGTH_LONG).show();
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        map.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        map.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        map.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        map.onLowMemory();
    }
}