package com.example.francoisluc.ift2905_projet;

/**
 * Created by Rosalie on 2017-04-07.
 */

public class Station {

    private int id;
    private String name;
    private int status;
    private double latitude;
    private double longitude;
    private int nbBixis;
    private int nbDocks;

    public Station(int id, String name, int status, double lat, double lon, int bixi, int dock){
        this.id = id;
        this.name = name;
        this.status = status;
        this.latitude = lat;
        this.longitude = lon;
        this.nbBixis = bixi;
        this.nbDocks = dock;
    }
    public Station(int id){
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getStatus() {
        return status;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
       return longitude;
    }

    public int getNbBixis() {
        return nbBixis;
    }

    public int getNbDocks() {
        return nbDocks;
    }
}
