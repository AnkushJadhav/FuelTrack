package com.keelerapps.fueltrack;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class SplashScreen extends AppCompatActivity {

    SharedPreferences sharedPrefs;
    SharedPreferences.Editor sharedEditor;
    EditText editTextKms;

    boolean commited;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPrefs =  getSharedPreferences(MainActivity.SHARED_PREFS_TAG, MODE_PRIVATE);
        boolean init = sharedPrefs.getBoolean("INIT_OVER", false);
        if (init) {
            Intent splash = new Intent(this, MainActivity.class);
            startActivity(splash);
            finish();
        }

        setContentView(R.layout.activity_splash_screen);

        editTextKms = (EditText) findViewById(R.id.editTextKmsInit);
    }

    public void onClickSave(View view) {

        String initKms = editTextKms.getText().toString();
        Log.d(MainActivity.TAG, "init kms splash = "+initKms);
        sharedEditor = sharedPrefs.edit();
        sharedEditor.putBoolean("INIT_OVER", true);
        sharedEditor.putString("INITIAL_KILOMETRES", initKms);
        sharedEditor.apply();
        Intent main = new Intent(this, MainActivity.class);
        startActivity(main);
        finish();

    }
}
