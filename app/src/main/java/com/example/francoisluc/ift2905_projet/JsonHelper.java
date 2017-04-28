package com.example.francoisluc.ift2905_projet;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by Rosalie on 2017-04-26.
 */

public class JsonHelper {

    private String source;


    public JsonHelper(String url){
        this.source = url;
    }

    public ArrayList<Station> getStationById(int id){
        ArrayList<Station> res = null;
        try {
            res = new GetStationByID().execute(id).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return res;
    }

    /*
    public JSONArray getStationArray() {
        JSONArray result = null;
        HttpHandler sh = new HttpHandler();
        String jsonStr = sh.makeServiceCall(source);
        if (jsonStr != null) {
            try {
                JSONObject fullJSON = new JSONObject(jsonStr);
                result = fullJSON.getJSONArray("stations");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return  result;
    }
    */

    public ArrayList<Station> getStationAtLatLon(long latmax, long latmin, long lonmax, long lonmin){
        ArrayList<Station> res = null;
        try {
            res = new GetStationAtLatLon().execute(latmax, latmin, lonmax, lonmin).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return res;
    }

    private class GetStationByID extends AsyncTask<Integer, Void, ArrayList<Station>>{

        @Override
        protected ArrayList<Station> doInBackground(Integer... params) {
            int idToFind = params[0];
            ArrayList<Station> result = new ArrayList<Station>();
            HttpHandler sh = new HttpHandler();
            String jsonStr = sh.makeServiceCall(source);
            if(jsonStr != null){
                try {
                    JSONObject fullJSON = new JSONObject(jsonStr);
                    JSONArray jsonStationArray = fullJSON.getJSONArray("stations");

                    for(int i = 0; i < jsonStationArray.length(); i++){
                        JSONObject stationI = jsonStationArray.getJSONObject(i);
                        if(stationI.getInt("id") == idToFind ){
                            Log.i("info", "station find");
                            int sId = stationI.getInt("id");
                            String sName = stationI.getString("s");
                            int sStatus = stationI.getInt("st");
                            double sLat = stationI.getDouble("la");
                            double sLon = stationI.getDouble("lo");
                            int sNbBixi = stationI.getInt("ba");
                            int sNbDock = stationI.getInt("da");

                            Station s = new Station(sId, sName, sStatus, sLat, sLon, sNbBixi, sNbDock);
                            Log.i("info", "station " + s.getName());
                            result.add(s);
                            break;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else
                Log.i("info", "json null");
            return result;
        }
    }

    private class GetStationAtLatLon extends AsyncTask<Long, Void, ArrayList<Station>>{

        @Override
        protected ArrayList<Station> doInBackground(Long... params) {
            long latmax = params[0];
            long latmin = params[1];
            long lonmax = params[2];
            long lonmin = params[3];
            ArrayList<Station> result = new ArrayList<Station>();
            HttpHandler sh = new HttpHandler();
            String jsonStr = sh.makeServiceCall(source);
            if(jsonStr != null){
                try {
                    JSONObject fullJSON = new JSONObject(jsonStr);
                    JSONArray jsonStationArray = fullJSON.getJSONArray("stations");

                    for(int i = 0; i < jsonStationArray.length(); i++){
                        JSONObject stationI = jsonStationArray.getJSONObject(i);
                        long stLat = stationI.getLong("la");
                        long stLon = stationI.getLong("lo");

                        if((stLat < latmax) && (stLat > latmin) && (stLon < lonmax) && (stLon > lonmin)){

                            int sId = stationI.getInt("id");
                            String sName = stationI.getString("s");
                            int sStatus = stationI.getInt("st");
                            int sNbBixi = stationI.getInt("ba");
                            int sNbDock = stationI.getInt("da");

                            Station s = new Station(sId, sName, sStatus, stLat, stLon, sNbBixi, sNbDock);
                            result.add(s);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else
                Log.i("info", "json null");
            return result;
        }
    }

}
