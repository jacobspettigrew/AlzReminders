package com.back4app.patient_app;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Save the current Installation to Back4App
        ParseInstallation.getCurrentInstallation().saveInBackground();
        SharedPreferences pref = getApplicationContext().getSharedPreferences("Storage", 0);
        SharedPreferences.Editor editor = pref.edit();

        TextView textView = findViewById(R.id.textView);



        if(!pref.contains("uniqueId")){

            ParseObject patient = new ParseObject("Patient");
            patient.put("init", true);
            String objectId = uniqueIdReturned();
            patient.put("uniqueId", objectId);
            patient.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if(e == null){
                    }
                    else{
                    }
                }
            });

            objectId = patient.getString("uniqueId");
            editor.putString("uniqueId", objectId);
            textView.setText(objectId;
            editor.commit();
        }
        else {
            textView.setText(pref.getString("uniqueId", "didn't get unique id"));
        }
    }


    public String uniqueIdReturned(){

        String validChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder a = new StringBuilder();
        Random rand = new Random();
        while (a.length() < 7) {
            int index = (int) (rand.nextFloat() * validChars.length());
            a.append(validChars.charAt(index));
        }
        String uniqueid = a.toString();

        return uniqueid;
    }

}
