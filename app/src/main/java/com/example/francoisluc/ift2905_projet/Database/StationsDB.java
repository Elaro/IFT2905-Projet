package com.example.francoisluc.ift2905_projet.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Rosalie on 2017-04-06.
 */

public class StationsDB {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "stations.db";

    private static final String TABLE_STATIONS = "table_stations";
    private static final String COL_ID = "ID";

    private SQLiteDatabase db;

    private MySQLiteHelper mySQLiteHelper;

    public StationsDB(Context context){
        //Create database and table stations
        mySQLiteHelper = new MySQLiteHelper(context, DB_NAME, null, DB_VERSION);
    }

    public void open(){
        db = mySQLiteHelper.getWritableDatabase();
    }

    public void close(){
        db.close();
    }

    public SQLiteDatabase getDB(){
        return db;
    }

    public long insertStation (StationsTableElement stationsTableElement){
        ContentValues values = new ContentValues();
        values.put(COL_ID, stationsTableElement.getId());
        return db.insert(TABLE_STATIONS, null, values);
    }

    public int removeStation(int id){
        return db.delete(TABLE_STATIONS, COL_ID + " = " +id, null);
    }

    public Cursor getStations(){
        return db.query(TABLE_STATIONS, new String[] {COL_ID}, null, null, null, null, null);

    }

    public boolean checkIfInDatabase(int id){
        boolean result = false;
        Cursor c = db.query(TABLE_STATIONS, null, COL_ID + " = ?",
                new String[] {"" + id}, null, null, null);
        if(c.getCount() >= 1)
            result = true;
        return result;
    }
}
