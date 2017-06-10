package com.keelerapps.fueltrack;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ankush on 04/06/17.
 */

public class FuelDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Fuel.db";

    public static final String CREATE_TABLE = "CREATE TABLE " + FuelContract.FuelEntry.TABLE_NAME +
            " ( " + FuelContract.FuelEntry._ID + " INTEGER PRIMARY KEY, " +
            FuelContract.FuelEntry.COLUMN_COST + " DECIMAL(5,2) NOT NULL, " +
            FuelContract.FuelEntry.COLUMN_KILOMETRES + " DECIMAL(5,2) NOT NULL, " +
            FuelContract.FuelEntry.COLUMN_LITRES + " DECIMAL(5,2) NOT NULL, " +
            FuelContract.FuelEntry.COLUMN_RATE + " DECIMAL(5,2) NOT NULL, " +
            FuelContract.FuelEntry.COLUMN_FULL_TANK + " BOOLEAN NOT NULL, " +
            FuelContract.FuelEntry.COLUMN_FUEL_DATE + " TEXT " + ")";

    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + FuelContract.FuelEntry.TABLE_NAME;

    public FuelDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }
}
