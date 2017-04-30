package com.example.francoisluc.ift2905_projet;

import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    public MyGMapFragmentBixi() {
        super();
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
        map = googleMap;
        map.getUiSettings().setMapToolbarEnabled(false);

        //add marker here
        new AddMarker().execute();
    }

    public void setOnInfoClickListener(GoogleMap.OnInfoWindowClickListener onInfoClickListener)
    {
        map.setOnInfoWindowClickListener(onInfoClickListener);
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
                        .title("Me")
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
            Station s = st[0];

            if(s.getNbBixis() == 0)
                drawable = getContext().getResources().getDrawable(R.drawable.ic_bixi_marker_red);
            else
                drawable = getContext().getResources().getDrawable(R.drawable.ic_bixi_marker_green);
            iconFactory.setBackground(drawable);

            Marker m = map.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon("" + s.getNbBixis())))
                    .position(new LatLng(s.getLatitude(),s.getLongitude()))
                    .title(s.getName())
            .snippet("bixi:"+s.getNbBixis()+" || "+"docks:"+s.getNbDocks()));
            m.setTag(s);

            markers.add(m);

        }

        @Override
        protected  void onPostExecute(Void v) {

        }
    }
}