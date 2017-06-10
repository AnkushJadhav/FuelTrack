package com.keelerapps.fueltrack;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.Locale;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "FuelTrack";

    public static final String SHARED_PREFS_TAG = "FuelTrackPrefs";

    public static final int REQUEST_CODE = 1;
    public static final int RESULT_FAIL = -99;

    private static Set<String> sharedPrefHolder;
    SharedPreferences sharedPrefs;
    SharedPreferences.Editor sharedEditor;

    public static final String SEPERATOR = "|";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPrefs =  getSharedPreferences(MainActivity.SHARED_PREFS_TAG, MODE_PRIVATE);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addIntent = new Intent(MainActivity.this, AddActivity.class);
                //startActivityForResult(addIntent, REQUEST_CODE);
                startActivity(addIntent);
                finish();
            }
        });
        sharedPrefHolder = sharedPrefs.getStringSet("SHARED_PREFS", null);

        String strInitKms = sharedPrefs.getString("INITIAL_KILOMETRES", "0");
        int fInitKms = Integer.parseInt(strInitKms);
        Log.d(TAG, "init kms = "+fInitKms);

        FuelDbHelper mDbHelper = new FuelDbHelper(this);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                FuelContract.FuelEntry.COLUMN_COST,
                FuelContract.FuelEntry.COLUMN_KILOMETRES,
                FuelContract.FuelEntry.COLUMN_LITRES,
                FuelContract.FuelEntry.COLUMN_FUEL_DATE
        };

        Cursor cursor = db.query(
                FuelContract.FuelEntry.TABLE_NAME,        // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        float tKms = 0;
        float tLts = 0;
        float tAvg = 0;
        String date = "-";

        int prevKms = fInitKms;
        while (cursor.moveToNext()) {
            Log.d(TAG, "cursor count = "+cursor.getCount());
            float cost = cursor.getFloat(
                    cursor.getColumnIndexOrThrow(FuelContract.FuelEntry.COLUMN_COST));
            float kms2 = cursor.getFloat(
                    cursor.getColumnIndexOrThrow(FuelContract.FuelEntry.COLUMN_KILOMETRES));
            int kms = (int) kms2;
            int diffKms = kms - prevKms;
            Log.d(TAG, "Diff kms = "+diffKms);
            prevKms = kms;
            float litres = cursor.getFloat(
                    cursor.getColumnIndexOrThrow(FuelContract.FuelEntry.COLUMN_LITRES));

            float avg = diffKms / litres;
            Log.d(TAG, "avg = "+avg);

            tAvg += avg;
            Log.d(TAG, "tAvg = "+tAvg);

            date = cursor.getString(cursor.getColumnIndexOrThrow(FuelContract.FuelEntry.COLUMN_FUEL_DATE));
        }

        float mileage = tAvg/cursor.getCount();
        Log.d(TAG,"mileage = "+mileage);

        int count = cursor.getCount();
        cursor.close();

        if (count > 0) {
            TextView avgTextView = (TextView) findViewById(R.id.textViewMileageIndicator);
            avgTextView.setText(String.format(Locale.getDefault(), "%.1f", mileage));
            TextView kmsTextView = (TextView) findViewById(R.id.textViewKmsIndicator);
            kmsTextView.setText(String.format(Locale.getDefault(), "%d", prevKms));
            TextView prevDateTextView = (TextView) findViewById(R.id.textViewLastRefillIndicator);
            prevDateTextView.setText(date);

        } else {
            TextView kmsTextView = (TextView) findViewById(R.id.textViewKmsIndicator);
            kmsTextView.setText(String.format(Locale.getDefault(), "%d", fInitKms));
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                String cost = data.getStringExtra(FuelContract.FuelEntry.COLUMN_COST);
                String kms = data.getStringExtra(FuelContract.FuelEntry.COLUMN_KILOMETRES);
                String litres = data.getStringExtra(FuelContract.FuelEntry.COLUMN_LITRES);
                String rate = data.getStringExtra(FuelContract.FuelEntry.COLUMN_RATE);
                boolean fullTank = data.getBooleanExtra(FuelContract.FuelEntry.COLUMN_FULL_TANK, false);
                String date = data.getStringExtra(FuelContract.FuelEntry.COLUMN_FUEL_DATE);

                String entry = date + SEPERATOR +
                        cost + SEPERATOR +
                        kms + SEPERATOR +
                        litres + SEPERATOR +
                        rate + SEPERATOR +
                        fullTank + SEPERATOR;

                /*
                sharedPrefs =  getPreferences(MODE_PRIVATE);
                sharedEditor = sharedPrefs.edit();
                sharedPrefHolder = sharedPrefs.getStringSet("SHARED_PREFS", null);

                if (sharedPrefHolder == null) {
                    sharedPrefHolder = new ArraySet<>();
                }
                sharedPrefHolder.add(entry);

                sharedEditor.putStringSet("SHARED_PREFS", sharedPrefHolder);
                sharedEditor.apply();
                */
            }
        }
    }
}
