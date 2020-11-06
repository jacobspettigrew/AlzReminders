/*
HEADER
FILE NAME:App.java
TEAN NAME: Alzreminders
BUGS:
PEOPLE WHO WORKED ON: KYUNG CHEOL KOH
PURPOSE:
        THIS ACTIVITY CONNECTS TO THE DATABASE
*/

package com.back4app.patient_app;

import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.app.Application;
import android.util.Log;
import android.widget.EditText;


public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.back4app_app_id))
                // if defined
                .clientKey(getString(R.string.back4app_client_key))
                .server(getString(R.string.back4app_server_url))
                .build()
        );
    }
}