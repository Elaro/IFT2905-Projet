package com.example.francoisluc.ift2905_projet;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by a on 2017-05-01.
 */

public class Route {
    //public Distance distance;
    //public Duration duration;
    public String endAddress;
    public LatLng endLocation;
    public String startAddress;
    public LatLng startLocation;

    public List<LatLng> points;

}
