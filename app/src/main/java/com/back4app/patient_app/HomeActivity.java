/*

HW 4

Course: CMPT 385 Software Engineering
Instructor: Dr. Herbert H. Tsang
Description: <
     TO ADD a new TASK to the Recyclerview
    >
Due date: < 2020/12/02 >
FILE NAME: Home.java
TEAM NAME: Alzreminders
Author: < Kyung Cheol Koh >
Input: < None>
Output: < Initialize the database  >
I pledge that I have completed the programming assignment independently.
I have not copied the code from a student or any source.
I have not given my code to any student.

Sign here: __Kyung Cheol Koh______
*/

package com.back4app.patient_app;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.util.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.back4app.patient_app.FamilyActivity.FamilyActivity;
import com.back4app.patient_app.TasksActivity.MainActivity;
import com.back4app.patient_app.models.Family;
import com.parse.Parse;
import com.parse.ParseObject;

import java.util.Random;


public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    //DATABASE
    private ParseObject mPatientObject;
    SharedPreferences mPref;
    SharedPreferences.Editor mEditor;
    EditText mPatientidTextView;

    //UI
    private CardView mCardViewTask, mCardViewReminder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //ASKING FOR READ PERMISSIONS
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED) {
        }

        else {
            ActivityCompat.requestPermissions(this, new String[]
                    { Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
        }


        mPatientidTextView = findViewById(R.id.patientIdHome);

        //DATABASE
        mPref = getApplicationContext().getSharedPreferences("Storage", 0);
        mEditor = mPref.edit();
        containsUniqueId();

        //UI
        mCardViewTask = findViewById(R.id.cardViewTask);
        mCardViewReminder = findViewById(R.id.cardViewFamily);
        mCardViewTask.setOnClickListener(this);
        mCardViewReminder.setOnClickListener(this);
    }


    //LISTS OF FUNCTIONS TO GO TO DIFFERENT ACTIVITIES
    public void goToTasks(View view){
        Toast.makeText(this,"Sign Up", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(view.getContext(), MainActivity.class);
        startActivity(intent);
    }

    public void goToFamily(View view){
        Toast.makeText(this,"Sign Up", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(view.getContext(), Family.class);
        startActivity(intent);
    }

    //Onclick to go to different activites
    @Override
    public void onClick(View view) {
        Intent intent;
        switch(view.getId()){
            case R.id.cardViewTask: {
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.cardViewFamily: {
                intent = new Intent(this, FamilyActivity.class);
                startActivity(intent);
                break;
            }
            default:{
                break;
            }
        }
    }


    //GENERATE UNIQUE ID
    public String uniqueIdReturned() {
        String validChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        String uniqueid;
        StringBuilder a = new StringBuilder();
        Random rand = new Random();
        while (a.length() < 7) {
            int index = (int) (rand.nextFloat() * validChars.length());
            a.append(validChars.charAt(index));
        }
        uniqueid = a.toString();
        return uniqueid;
    }

    // CHECK IF APP RUNS FOR THE FIRST TIME
    public void containsUniqueId() {
        String uniqueId = "";

        if(!isWifiConnected()){
            mPatientidTextView.setText("please connect to wifi");
        }
        //Store uniqueId in the local database
        else if (!mPref.contains("uniqueId")) {
            //Create a parseobject with Patient and store the uniqueID
            mPatientObject = new ParseObject("Patient");
            mPatientObject.put("init", true);
            uniqueId = uniqueIdReturned();
            mPatientObject.put("uniqueId", uniqueId);
            mPatientObject.saveInBackground();

            //Store it in the local storage here
            mEditor.putString("objectId", mPatientObject.getObjectId());
            mEditor.putString("uniqueId", uniqueId);
            mPatientidTextView.setText(uniqueId);
            mEditor.commit();
        } else {
            mPatientidTextView.setText(mPref.getString("uniqueId", "didn't get unique id"));

        }
    }


    //Check if wifi is connected
    public boolean isWifiConnected()
    {
        ConnectivityManager connectivityManagerm = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
        return (connectivityManagerm != null) && (connectivityManagerm.getActiveNetworkInfo() != null) &&
                (connectivityManagerm.getActiveNetworkInfo().getType() == 1);
    }
}
