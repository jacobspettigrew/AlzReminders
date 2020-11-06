/*
HEADER
FILE NAME:MainActivity.java
TEAN NAME: Alzreminders
BUGS:
PEOPLE WHO WORKED ON: KYUNG CHEOL KOH ki
PURPOSE:
        GRABS THE DATA FROM THE BACK4APPS DATABASE
        SHOW THE LIST OF TASKS FOR PATIENTS
CODING STANDARD
    NAME CONVENTION: CAMELCASE STARTING WITH LOWERCASE
    GLOBAL VARIABLE: CAMELCASE STARTING WITH m

*/

package com.back4app.patient_app;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> mItems;
    private ArrayAdapter<String> mItemsAdapter;
    private SwipeRefreshLayout mSwipeRefresh;

    //DATABASE
    private ParseObject mPatientObject;
    SharedPreferences mPref;
    SharedPreferences.Editor mEditor;

    // UI
    TextView mPatientidTextView;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //DATABASE
        mPref = getApplicationContext().getSharedPreferences("Storage", 0);
        mEditor = mPref.edit();

        //UI
        mPatientidTextView = findViewById(R.id.patientidTextView);
        mListView = findViewById(R.id.listView);
        mItems = new ArrayList<>();
        mSwipeRefresh = findViewById(R.id.swiperefreshItems);

        //CHECK IF UNIQUE ID EXISTS
        containsUniqueId();

        mItems = new ArrayList<>();
        mItemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mItems);
        findObjectId();

        mListView.setAdapter(mItemsAdapter);
        mItemsAdapter.notifyDataSetChanged();
        setUpListViewListener();

        //REFRESH LISTENER
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mItems.clear();
                findObjectId();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(mSwipeRefresh.isRefreshing()) {
                            mSwipeRefresh.setRefreshing(false);
                        }
                    }
                }, 1000);
            }
        });
    }

    //GENERATE UNIQUE ID
    public String uniqueIdReturned(){
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
    public void containsUniqueId(){
        String uniqueId ="";

        if(!mPref.contains("uniqueId")){
            mPatientObject = new ParseObject("Patient");
            mPatientObject.put("init", true);
            uniqueId = uniqueIdReturned();
            mPatientObject.put("uniqueId", uniqueId);
            mPatientObject.saveInBackground();

            mEditor.putString("objectId", mPatientObject.getObjectId());
            mEditor.putString("uniqueId", uniqueId);
            mPatientidTextView.setText(uniqueId);
            mEditor.commit();
        }
        else {
            mPatientidTextView.setText(mPref.getString("uniqueId", "didn't get unique id"));
        }
    }

    // WATCHES FOR THE LONG PRESS TO DELETE
    private void setUpListViewListener() {
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Context context = getApplicationContext();
                Toast.makeText(context, "Task Removed", Toast.LENGTH_LONG).show();

                mItems.remove(position);
                mItemsAdapter.notifyDataSetChanged();
                //PUT DELETED LIST TO THE DATABASE
                mPatientObject.put("arrayToDos", mItems);
                mPatientObject.saveInBackground();
                return true;
            }
        });
    }

    //ITERATION TO FIND THE TASKS GIVEN THE UNIQUE ID
    private void findObjectId(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Patient");
        query.whereEqualTo("uniqueId", mPref.getString("uniqueId", "didn't get unique id"));

        //BACKGROUND THREAD TO FIND THE UNIQUE ID
        // ITERATE TO APPEND THE DATA TO THE ARRAYLISTS
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    if (objects.size() > 0) {
                        for (ParseObject object : objects) {
                            mPatientObject = object;
                            List<Object> item = object.getList("arrayToDos");
                            if (item == null) {
                            }
                            else {
                                for (int i = 0; i < item.size(); i++) {
                                    mItems.add(item.get(i).toString());
                                }
                                mItemsAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                }
            }
        });
    }
}
