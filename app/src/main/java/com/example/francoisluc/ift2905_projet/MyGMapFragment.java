package com.example.francoisluc.ift2905_projet;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MyGMapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap map;

    public MyGMapFragment() {
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
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment map = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapF);
        map.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        //add marker here
        new AddMarker().execute();

    }


    private class AddMarker extends AsyncTask<Void, Void, String>{

        @Override
        protected String doInBackground(Void... params) {
            HttpHandler sh = new HttpHandler();
            String jsonStr = sh.makeServiceCall("https://secure.bixi.com/data/stations.json");
            return jsonStr;
        }

        @Override
        protected  void onPostExecute(String json) {
            try {
                JSONObject fullJSON = new JSONObject(json);
                JSONArray jsonStationArray = fullJSON.getJSONArray("stations");

                for(int i = 0; i < jsonStationArray.length(); i++){
                    JSONObject stationI = jsonStationArray.getJSONObject(i);

                    //int sId = stationI.getInt("id");
                    String sName = stationI.getString("s");
                    // int sStatus = stationI.getInt("st");
                    double sLat = stationI.getDouble("la");
                    double sLon = stationI.getDouble("lo");
                    //int sNbBixi = stationI.getInt("ba");
                    //int sNbDock = stationI.getInt("da");

                    map.addMarker(new MarkerOptions()
                            .position(new LatLng(sLat,sLon))
                            .title(sName));

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}