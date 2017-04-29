package com.example.francoisluc.ift2905_projet;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Rosalie on 2017-04-07.
 */

public class Station implements Parcelable{

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


    public Station(Parcel in){
        this.id = in.readInt();
        this.name = in.readString();
        this.status = in.readInt();
        this.latitude = in.readDouble();
        this.longitude = in.readDouble();
        this.nbBixis = in.readInt();
        this.nbDocks = in.readInt();
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeInt(status);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeInt(nbBixis);
        dest.writeInt(nbDocks);
    }

    public static final Parcelable.Creator<Station> CREATOR = new Parcelable.Creator<Station>() {
        @Override
        public Station createFromParcel(Parcel in) {
            return new Station(in);
        }

        @Override
        public Station[] newArray(int size) {
            return new Station[size];
        }
    };
}
