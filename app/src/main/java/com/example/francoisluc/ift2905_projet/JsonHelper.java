package com.example.francoisluc.ift2905_projet;

import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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

}
