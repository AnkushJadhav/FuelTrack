package com.keelerapps.fueltrack;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

public class AddActivity extends AppCompatActivity {

    private EditText etCost;
    private EditText etKms;
    private EditText etLitres;
    private EditText etRate;
    private CheckBox cbFullTank;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        etCost = (EditText) findViewById(R.id.editTextCost);
        etKms = (EditText) findViewById(R.id.editTextKms);
        etLitres = (EditText) findViewById(R.id.editTextLitres);
        etRate = (EditText) findViewById(R.id.editTextRate);
        cbFullTank = (CheckBox) findViewById(R.id.checkBoxFullTank);

        etLitres.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    if (etCost.getText().length() > 0 && etLitres.getText().length() > 0) {
                        etRate.setText(
                                String.format(Locale.getDefault(), "%.2f",
                                (Float.parseFloat(etCost.getText().toString()) / Float.parseFloat(etLitres.getText().toString()))
                                ));
                    }
                }
            }
        });
    }

    public void saveEntry(View view) {
        float cost;
        float kms;
        float litres;
        float rate;
        boolean fullTank;
        String date;

        cost = Float.parseFloat(etCost.getText().toString());
        kms = Float.parseFloat(etKms.getText().toString());
        litres = Float.parseFloat(etLitres.getText().toString());
        rate = Float.parseFloat(etRate.getText().toString());
        fullTank = cbFullTank.isChecked();
        date = DateFormat.getDateInstance().format(new Date());

        FuelDbHelper dbHelper = new FuelDbHelper(getApplicationContext());

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues(FuelContract.FuelEntry.COLUMNS_COUNT);
        values.put(FuelContract.FuelEntry.COLUMN_COST, cost);
        values.put(FuelContract.FuelEntry.COLUMN_KILOMETRES, kms);
        values.put(FuelContract.FuelEntry.COLUMN_LITRES, litres);
        values.put(FuelContract.FuelEntry.COLUMN_RATE, rate);
        values.put(FuelContract.FuelEntry.COLUMN_FULL_TANK, fullTank);
        values.put(FuelContract.FuelEntry.COLUMN_FUEL_DATE, date);

        long rowId = db.insert(FuelContract.FuelEntry.TABLE_NAME, null, values);

        Intent returnIntent = new Intent(this, MainActivity.class);
        if (rowId > -1) {
            returnIntent.putExtra(FuelContract.FuelEntry._ID, rowId);
            returnIntent.putExtra(FuelContract.FuelEntry.COLUMN_COST, cost);
            returnIntent.putExtra(FuelContract.FuelEntry.COLUMN_KILOMETRES, kms);
            returnIntent.putExtra(FuelContract.FuelEntry.COLUMN_LITRES, litres);
            returnIntent.putExtra(FuelContract.FuelEntry.COLUMN_RATE, rate);
            returnIntent.putExtra(FuelContract.FuelEntry.COLUMN_FULL_TANK, fullTank);
            returnIntent.putExtra(FuelContract.FuelEntry.COLUMN_FUEL_DATE, date);
            setResult(RESULT_OK, returnIntent);
        }
        else {
            setResult(MainActivity.RESULT_FAIL, returnIntent);
        }
        startActivity(returnIntent);
        finish();
    }
}
