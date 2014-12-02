package com.emostn.publictransit2.db_helper;
import android.content.*;
import android.database.*;
import android.database.sqlite.*;
import android.util.*;

import com.emostn.publictransit2.model.jSoupTrolley;

import java.util.*;
import com.emostn.publictransit2.db_helper.PublicTransitContract.*;

public class Helper extends SQLiteOpenHelper {

    public static final String DB_NAME = "publictransit.db";
    public static int DB_VERSION = 1;

    public Helper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
		String trolleyTableCreateQuery = "CREATE TABLE " + TrolleyCon.TABLE_NAME + "(" +
			TrolleyCon.ID + " INTEGER PRIMARY KEY, " + TrolleyCon.NAME + " TEXT, " + TrolleyCon.URL + " TEXT, " + TrolleyCon.HTML + " TEXT)";
		db.execSQL(trolleyTableCreateQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop table trolley
		String trolleyTableDropQuery = "DROP TABLE IF EXISTS " + TrolleyCon.TABLE_NAME;
		db.execSQL(trolleyTableDropQuery);
		// Upgrade database
		onCreate(db);
    }
	// Trolley CRUD Operation

    public String insertTrolleyRecord(jSoupTrolley model) {
        SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(TrolleyCon.NAME, model.getName());
		values.put(TrolleyCon.URL, model.getUrl());
		values.put(TrolleyCon.HTML, model.getHtml());

		Long row = db.insert(TrolleyCon.TABLE_NAME, null, values);
		String r = " @ " + row;
		db.close();

		return r;

    }

    public ArrayList<jSoupTrolley> getTrolleyRecord() {
        ArrayList<jSoupTrolley> record = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

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
		db.close();
		return record;

    }

    // Get trolley names
    public ArrayList<String> getTrolleyNames() {
        ArrayList<String> record = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            String[] collumns = {"name"};
            Cursor cursor = db.query(TrolleyCon.TABLE_NAME, collumns, null, null, null, null, null, null);
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
        String countQuery = "SELECT * FROM " + TrolleyCon.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

	public void updateRow(jSoupTrolley jSoupTrolley) {
        SQLiteDatabase db = this.getWritableDatabase();

		ContentValues args = new ContentValues();
		args.put(TrolleyCon.NAME, jSoupTrolley.getName());
		args.put(TrolleyCon.URL, jSoupTrolley.getUrl());
		args.put(TrolleyCon.HTML, jSoupTrolley.getHtml());

		db.update(TrolleyCon.TABLE_NAME, args, TrolleyCon.URL + "=" + jSoupTrolley.getUrl(), null);//",new String[]{)});
		db.close();

	}

	public boolean doesTrolleyExist(String url) {
		SQLiteDatabase db = this.getReadableDatabase();
        Boolean r;
		Cursor cursor = db.rawQuery("SELECT * FROM " + TrolleyCon.TABLE_NAME + " WHERE " + TrolleyCon.URL + "=? ", new String[]{url});

		r = cursor != null ? true : false;

		cursor.close();
		db.close();
		return r;

	}
	public void dropTrolleys() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TrolleyCon.TABLE_NAME, null, null);
	}
}
