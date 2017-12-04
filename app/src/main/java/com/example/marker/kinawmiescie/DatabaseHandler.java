package com.example.marker.kinawmiescie;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.marker.kinawmiescie.models.Cinema;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by marker on 02.12.2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "myProject";

    // Cinemas table name
    private static final String TABLE_CINEMAS = "cinemas";

    // Cinemas Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_STREET = "street";
    private static final String KEY_POSTAL_CODE = "postal_code";
    private static final String KEY_CITY = "city";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CINEMAS_TABLE = "CREATE TABLE " + TABLE_CINEMAS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT,"
                + KEY_DESCRIPTION + " TEXT,"
                + KEY_STREET + " TEXT,"
                + KEY_POSTAL_CODE + " TEXT,"
                + KEY_CITY + " TEXT"
                + ")";
        db.execSQL(CREATE_CINEMAS_TABLE);

        // ONLY FOR PRESENTATION ------------------------------------
        Cinema cinema = new Cinema();
        cinema.setCity("Gdańsk");
        cinema.setStreet("Aleja Zwycięstwa 14");
        cinema.setName("Multikino");
        cinema.setDescription("Otwarte zimą 2000 roku. Posiada 10 sal na ktore składa się 2771 miejsc siedzących");
        addCinema(cinema);
        // ----------------------------------------------------------
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CINEMAS);

        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new cinema
    void addCinema(Cinema cinema) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, cinema.getName());
        values.put(KEY_DESCRIPTION, cinema.getDescription());
        values.put(KEY_STREET, cinema.getStreet());
        values.put(KEY_CITY, cinema.getCity());
        values.put(KEY_POSTAL_CODE, cinema.getPostal_code());

        // Inserting Row
        db.insert(TABLE_CINEMAS, null, values);
        db.close(); // Closing database connection
    }

    // Getting single cinema
    public Cinema getCinema(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CINEMAS, new String[] { KEY_ID,
                        KEY_NAME, KEY_DESCRIPTION, KEY_STREET, KEY_CITY, KEY_POSTAL_CODE}, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Cinema cinema = new Cinema(cursor.getString(1), cursor.getString(2));
        cinema.setID(cursor.getInt(0));
        cinema.setStreet(cursor.getString(3));
        cinema.setCity(cursor.getString(4));
        cinema.setPostal_code(cursor.getString(5));
        return cinema;
    }

    // Getting All Cinemas
    public List<Cinema> getAllCinemas() {
        List<Cinema> cinemaList = new ArrayList<Cinema>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CINEMAS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Cinema cinema = new Cinema();
                cinema.setID(Integer.parseInt(cursor.getString(0)));
                cinema.setName(cursor.getString(1));
                cinema.setDescription(cursor.getString(2));
                cinema.setStreet(cursor.getString(3));
                cinema.setCity(cursor.getString(4));
                cinema.setPostal_code(cursor.getString(5));
                cinemaList.add(cinema);
            } while (cursor.moveToNext());
        }

        // return cinema list
        return cinemaList;
    }

    // Updating single cinema
    public int updateCinema(Cinema cinema) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, cinema.getName());
        values.put(KEY_DESCRIPTION, cinema.getDescription());
        values.put(KEY_STREET, cinema.getStreet());
        values.put(KEY_CITY, cinema.getCity());
        values.put(KEY_POSTAL_CODE, cinema.getPostal_code());

        // updating row
        return db.update(TABLE_CINEMAS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(cinema.getID()) });
    }

    // Deleting single cinema
    public void deleteCinema(Cinema cinema) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CINEMAS, KEY_ID + " = ?",
                new String[] { String.valueOf(cinema.getID()) });
        db.close();
    }

    public void deleteCinema(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CINEMAS, KEY_ID + " = ?", new String[] { String.valueOf(id) });
        db.close();
    }


    // Getting cinemas Count
    public int getCinemasCount() {
        String countQuery = "SELECT  * FROM " + TABLE_CINEMAS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }
}
