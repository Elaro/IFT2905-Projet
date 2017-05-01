package com.example.francoisluc.ift2905_projet;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.ui.IconGenerator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by a on 2017-04-28.
 */

public class ItineraryTask extends Fragment implements OnMapReadyCallback, DirectioFinderListener  {

        private GoogleMap gMap;
        private List<Marker> originMarkers = new ArrayList<>();
        private List<Marker> destinationMarkers = new ArrayList<>();
        private List<Polyline> polylinePaths = new ArrayList<>();
        private ProgressDialog progressDialog;
        final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
        private ArrayList<Station> stationList = new ArrayList<>();

    public ItineraryTask() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.map_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment map = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapF);
        map.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        gMap.getUiSettings().setMapToolbarEnabled(false);

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            gMap.setMyLocationEnabled(true);
            gMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                @Override
                public boolean onMyLocationButtonClick() {
                    Location loc = gMap.getMyLocation();
                    gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(loc.getLatitude(),
                            loc.getLongitude()), 17));
                    return true;
                }
            });
        }
        new GetStations().execute();
    }

    public void sendRequest(EditText itinStartLocation, EditText itinDestLocation){
        String origin = itinStartLocation.getText().toString();
        String destination = itinDestLocation.getText().toString();

        try{
            new DirectionFinder(this,origin,destination).execute();
        } catch(UnsupportedEncodingException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onDirectionFinderStart() {
        //progressDialog = ProgressDialog.show(this, "Please wait.",
         //       "Finding direction..!", true);

        if (originMarkers != null) {
            for (Marker marker : originMarkers) {
                marker.remove();
            }
        }

        if (destinationMarkers != null) {
            for (Marker marker : destinationMarkers) {
                marker.remove();
            }
        }

        if (polylinePaths != null) {
            for (Polyline polyline:polylinePaths ) {
                polyline.remove();
            }
        }
    }

    @Override
    public void onDirectionFinderSuccess(List<Route> routes) {
//        progressDialog.dismiss();
        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();

        for (Route route : routes) {
            //gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.startLocation, 16));
            //((TextView) findViewById(R.id.tvDuration)).setText(route.duration.text);
            //((TextView) findViewById(R.id.tvDistance)).setText(route.distance.text);

            originMarkers.add(gMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                    .title(route.startAddress)
                    .position(route.startLocation)));
            gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.startLocation, 16));
            //add bixi
            addStationMarker(true);

            destinationMarkers.add(gMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                    .title(route.endAddress)
                    .position(route.endLocation)));
            gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.endLocation, 16));
            //add dock
            addStationMarker(false);

            gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.startLocation, 16));

            PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).
                    color(Color.BLUE).
                    width(10);

            for (int i = 0; i < route.points.size(); i++)
                polylineOptions.add(route.points.get(i));

            polylinePaths.add(gMap.addPolyline(polylineOptions));

        }
    }

    public void addStationMarker(boolean start){

        LatLngBounds latLongBounds = gMap.getProjection().getVisibleRegion().latLngBounds;

        for(int i = 0; i < stationList.size(); i++){
            Station s = stationList.get(i);
            LatLng ll = new LatLng(s.getLatitude(),s.getLongitude());
            if(latLongBounds.contains(ll)){
                if(start)
                    addBixiMarker(s, ll);
                else
                    addDockMarker(s, ll);
            }
        }
    }

    private void addBixiMarker(Station s, LatLng ll){
        IconGenerator iconFactory = new IconGenerator(getContext());
        Drawable drawable;
        String number = "";

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

        gMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(number)))
                .position(ll)
                .title(s.getName())
                .snippet("bixi:"+s.getNbBixis()+" || "+"docks:"+s.getNbDocks()));

    }

    private void addDockMarker(Station s, LatLng ll){
        IconGenerator iconFactory = new IconGenerator(getContext());
        Drawable drawable;
        String number = "";

        if(s.getStatus() == 2){
            drawable = getContext().getResources().getDrawable(R.drawable.ic_marker_gray);
        }
        else if(s.getNbBixis() == 0) {
            drawable = getContext().getResources().getDrawable(R.drawable.ic_dock_marker_red);
            number += s.getNbDocks();
        }
        else {
            drawable = getContext().getResources().getDrawable(R.drawable.ic_dock_marker_green);
            number += s.getNbDocks();
        }
        iconFactory.setBackground(drawable);

        gMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(number)))
                .position(ll)
                .title(s.getName())
                .snippet("bixi:"+s.getNbBixis()+" || "+"docks:"+s.getNbDocks()));


    }

    private class GetStations extends AsyncTask<Void, Void, ArrayList<Station>> {

        @Override
        protected ArrayList<Station> doInBackground(Void... params) {
            HttpHandler sh = new HttpHandler();
            String jsonStr = sh.makeServiceCall("https://secure.bixi.com/data/stations.json");
            ArrayList<Station> list = new ArrayList<>();

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
                    list.add(st);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return list;
        }
        @Override
        protected  void onPostExecute(ArrayList<Station> list) {
            stationList = list;
        }
    }

}
