package com.emostn.publictransit2.db_helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.emostn.publictransit2.db_helper.PublicTransitContract.StopCon;
import com.emostn.publictransit2.db_helper.PublicTransitContract.TrolleyCon;
import com.emostn.publictransit2.db_helper.PublicTransitContract.VehicleCon;
import com.emostn.publictransit2.model.Stop;
import com.emostn.publictransit2.model.Vehicle;
import com.emostn.publictransit2.model.jSoupTrolley;

import java.util.ArrayList;

public class DbHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "publicTransit.db";
    public static final int DB_VERSION = 1;

    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            // Create table Vehicle
            String vehicleTableCreateQuery = "CREATE TABLE " + VehicleCon.TABLE_NAME +
                    " (" +
                    VehicleCon.ID + " INTEGER PRIMARY KEY, " +
                    VehicleCon.TYPE + " TEXT," +
                    VehicleCon.NAME + " TEXT" +
                    ")";
            db.execSQL(vehicleTableCreateQuery);

            // Create table Stop
            String stopTableCreateQuery =  "CREATE TABLE " + StopCon.TABLE_NAME +
                    " ( " +
                    StopCon.ID + " INTEGER PRIMARY KEY, " +
                    StopCon.NAME + " TEXT, " +
                    StopCon.LATITUDE + " FLOAT, " +
                    StopCon.LONGITUDE + " FLOAT" +
                    ")";

            db.execSQL(stopTableCreateQuery);

            // Create table trolley
            String trolleyTableCreateQuery = "CREATE TABLE " + TrolleyCon.TABLE_NAME +
                    "(" +
                    TrolleyCon.ID + " INTEGER PRIMARY KEY, " +
                    TrolleyCon.NAME + " TEXT, " +
                    TrolleyCon.URL + " TEXT, " +
                    TrolleyCon.HTML + " TEXT)";
            db.execSQL(trolleyTableCreateQuery);

        } catch (SQLException se) {
            Log.v("DatabaseHandler Oncreate SQLException",
				  Log.getStackTraceString(se));
        } catch (Exception e) {
            Log.v("DatabaseHandler Oncreate Exception",
				  Log.getStackTraceString(e));
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            // Drop table Vehicle
            String vehicleTableDropQuery = "DROP TABLE IF EXISTS " + VehicleCon.TABLE_NAME;
            db.execSQL(vehicleTableDropQuery);

            // Drop table Stop
            String stopTableDropQuery = "DROP TABLE IF EXISTS "
				+ StopCon.TABLE_NAME;
            db.execSQL(stopTableDropQuery);

            // Drop table trolley
            String trolleyTableDropQuery = "DROP TABLE IF EXISTS "
				+ TrolleyCon.TABLE_NAME;
            db.execSQL(trolleyTableDropQuery);

            // Upgrade database
            onCreate(db);

        } catch (SQLException se) {
            Log.v("DatabaseHandler onUpgrade SQLException",
				  Log.getStackTraceString(se));
        } catch (Exception e) {
            Log.v("DatabaseHandler onUpgrade Exception",
				  Log.getStackTraceString(e));
        }

    }

    // Vehicle CRUD Operation

    public String insertVehicleRecord(Vehicle model) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(VehicleCon.TYPE, model.getType());
            values.put(VehicleCon.NAME, model.getName());
            db.insert(VehicleCon.TABLE_NAME, null, values);
            db.close();

            return "Record insert successfully...";
        } catch (SQLiteException se) {
            Log.v("DatabaseHandler insertVehicleRecord Exception",
				  Log.getStackTraceString(se));
            return se.getMessage();
        } catch (Exception e) {
            Log.v("DatabaseHandler insertVehicleRecord Exception",
				  Log.getStackTraceString(e));
            return e.getMessage();
        } finally {
            db.close();
        }
    }

    public ArrayList<Vehicle> getVehicleRecord() {
        ArrayList<Vehicle> record = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            String selectQuery = "SELECT * FROM " + VehicleCon.TABLE_NAME;
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {

                    Vehicle model = new Vehicle();
                    model.setId(cursor.getLong(0));
                    model.setType(cursor.getString(1));
                    model.setName(cursor.getString(2));

                    record.add(model);

                } while (cursor.moveToNext());
            }
            return record;
        } catch (SQLiteException se) {
            Log.v("DatabaseHandler getVehicleRecord Exception",
				  Log.getStackTraceString(se));
        } catch (Exception e) {
            Log.v("DatabaseHandler getVehicleRecord Exception",
				  Log.getStackTraceString(e));
        } finally {
            db.close();
        }
        return record;
    }

    // Stop CRUD Operation

    public String insertStopRecord(Stop model) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(StopCon.NAME, model.getName());
            values.put(StopCon.LATITUDE, model.getLatitude());
            values.put(StopCon.LONGITUDE, model.getLongtitude());

            db.insert(StopCon.TABLE_NAME, null, values);
            db.close();

            return "Record insert succussfully...";
        } catch (SQLiteException se) {
            Log.v("DatabaseHandler insertStopRecord Exception",
				  Log.getStackTraceString(se));
            return se.getMessage();
        } catch (Exception e) {
            Log.v("DatabaseHandler insertStopRecord Exception",
				  Log.getStackTraceString(e));
            return e.getMessage();
        } finally {
            db.close();
        }
    }

    public ArrayList<Stop> getStopRecord() {
        ArrayList<Stop> record = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            String selectQuery = "SELECT * FROM " + StopCon.TABLE_NAME;
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {

                    Stop model = new Stop();
                    model.setId(cursor.getLong(0));
                    model.setName(cursor.getString(1));
                    model.setLatitude(cursor.getFloat(2));
                    model.setLongtitude(cursor.getFloat(3));

                    record.add(model);

                } while (cursor.moveToNext());
            }
            return record;
        } catch (SQLiteException se) {
            Log.v("DatabaseHandler getVehicleRecord Exception",
				  Log.getStackTraceString(se));
        } catch (Exception e) {
            Log.v("DatabaseHandler getVehicleRecord Exception",
				  Log.getStackTraceString(e));
        } finally {
            db.close();
        }
        return record;
    }

    // Trolley CRUD Operation

    public String insertTrolleyRecord(jSoupTrolley model) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(TrolleyCon.NAME, model.getName());
            values.put(TrolleyCon.URL, model.getUrl());
            values.put(TrolleyCon.HTML, model.getHtml());

            Long row = db.insert(TrolleyCon.TABLE_NAME, null, values);
			String r = " @ " + row;
            db.close();

            return r;
        } catch (SQLiteException se) {
            Log.v("DatabaseHandler insertTrolleyRecord Exception",
				  Log.getStackTraceString(se));
            return se.getMessage();
        } catch (Exception e) {
            Log.v("DatabaseHandler insertTrolleyRecord Exception",
				  Log.getStackTraceString(e));
            return e.getMessage();
        } finally {
            db.close();
        }
    }

    public ArrayList<jSoupTrolley> getTrolleyRecord() {
        ArrayList<jSoupTrolley> record = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            String selectQuery = "SELECT * FROM " + TrolleyCon.TABLE_NAME;
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {

                    jSoupTrolley model = new jSoupTrolley();
                    model.setId(cursor.getLong(0));
                    model.setName(cursor.getString(1));
                    model.setUrl(cursor.getString(2));
                    model.setHtml(cursor.getString(3));

                    record.add(model);

                } while (cursor.moveToNext());
            }
            return record;
        } catch (SQLiteException se) {
            Log.v("DatabaseHandler getVehicleRecord Exception",
				  Log.getStackTraceString(se));
        } catch (Exception e) {
            Log.v("DatabaseHandler getVehicleRecord Exception",
				  Log.getStackTraceString(e));
        } finally {
            db.close();
        }
        return record;
    }

    // Get trolley names
    public ArrayList<String> getTrolleyNames() {
        ArrayList<String> record = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            String[] col = {TrolleyCon.NAME};
            Cursor cursor = db.query(TrolleyCon.TABLE_NAME, col, null, null, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {

                    String name = cursor.getString(0);
                    record.add(name);

                } while (cursor.moveToNext());
            }
            return record;
        } catch (SQLiteException se) {
            Log.v("DatabaseHandler getVehicleRecord Exception",
				  Log.getStackTraceString(se));
        } catch (Exception e) {
            Log.v("DatabaseHandler getVehicleRecord Exception",
				  Log.getStackTraceString(e));
        } finally {
            db.close();
        }
        return record;
    }

    // Getting contacts Count
    public int getTrolleyCount() {
        String countQuery = "SELECT  * FROM " + TrolleyCon.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }
}
