package com.example.francoisluc.ift2905_projet;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class MyGMapFragmentBixi extends Fragment implements OnMapReadyCallback {

    private GoogleMap map;
    private ArrayList<Marker> markers = new ArrayList<>();
    final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    public MyGMapFragmentBixi() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.map_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment map = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapF);
        map.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.getUiSettings().setMapToolbarEnabled(false);

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(true);
            map.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                @Override
                public boolean onMyLocationButtonClick() {
                    Location loc = map.getMyLocation();
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(loc.getLatitude(),
                           loc.getLongitude()), 17));
                    return true;
                }
            });
        } else {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
        //add marker here
        new AddMarker().execute();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //This double check is useless, but android give an error if i don't add it...
                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED)
                    map.setMyLocationEnabled(true);
                }
            }
        }
    }

    public void findAddress(String address){
        Geocoder geocoder = new Geocoder(getContext(), Locale.CANADA_FRENCH);
        try {
            List<Address> result = geocoder.getFromLocationName(address, 1);
            if(result == null || result.isEmpty());
            else{
                Address a = result.get(0);
                map.addMarker(new MarkerOptions()
                        .position(new LatLng(a.getLatitude(),a.getLongitude()))
                        .title(a.getAddressLine(0))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(a.getLatitude(),a.getLongitude()), 17));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public ArrayList<Station> getStationList(){
        ArrayList<Station> stations = new ArrayList<>();
        LatLngBounds latLongBounds = map.getProjection().getVisibleRegion().latLngBounds;

        for(int i = 0; i < markers.size(); i++){
            Marker m = markers.get(i);
            if(latLongBounds.contains(m.getPosition())){
                Station s =  (Station)m.getTag();
                stations.add(s);
            }
        }
        return stations;
    }


    private class AddMarker extends AsyncTask<Void, Station, Void>{

        @Override
        protected Void doInBackground(Void... params) {
            HttpHandler sh = new HttpHandler();
            String jsonStr = sh.makeServiceCall("https://secure.bixi.com/data/stations.json");

            try {
                JSONObject fullJSON = new JSONObject(jsonStr);
                JSONArray jsonStationArray = fullJSON.getJSONArray("stations");

                for(int i = 0; i < jsonStationArray.length(); i++){

                    JSONObject stationI = jsonStationArray.getJSONObject(i);

                    int sId = stationI.getInt("id");
                    String sName = stationI.getString("s");
                    int sStatus = stationI.getInt("st");
                    double sLat = stationI.getDouble("la");
                    double sLon = stationI.getDouble("lo");
                    int sNbBixi = stationI.getInt("ba");
                    int sNbDock = stationI.getInt("da");

                    Station st = new Station(sId, sName, sStatus, sLat, sLon, sNbBixi, sNbDock);
                    publishProgress(st);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Station ... st){
            super.onProgressUpdate(st);
            IconGenerator iconFactory = new IconGenerator(getContext());
            Drawable drawable;
            String number = "";
            Station s = st[0];

            if(s.getStatus() == 2){
                drawable = getContext().getResources().getDrawable(R.drawable.ic_marker_gray);
            }
            else if(s.getNbBixis() == 0) {
                drawable = getContext().getResources().getDrawable(R.drawable.ic_bixi_marker_red);
                number += s.getNbBixis();
            }
            else {
                drawable = getContext().getResources().getDrawable(R.drawable.ic_bixi_marker_green);
                number += s.getNbBixis();
            }
            iconFactory.setBackground(drawable);

            Marker m = map.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(number)))
                    .position(new LatLng(s.getLatitude(),s.getLongitude()))
                    .title(s.getName()));
            m.setTag(s);

            markers.add(m);

        }

        @Override
        protected  void onPostExecute(Void v) {

        }
    }
}