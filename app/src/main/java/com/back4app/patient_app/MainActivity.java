package com.back4app.patient_app;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {


    private ArrayList<String> items;
    private ArrayAdapter<String> itemsAdapter;

    //database
    private ParseObject patientObject;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    //ui
    TextView textView;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //database
        pref = getApplicationContext().getSharedPreferences("Storage", 0);
        editor = pref.edit();

        //ui
        textView = findViewById(R.id.textView);
        listView = findViewById(R.id.listView);
        items = new ArrayList<>();

        //if unique id exists
        containsUniqueId();

        items = new ArrayList<>();
        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        findObjectId();

        listView.setAdapter(itemsAdapter);
        itemsAdapter.notifyDataSetChanged();
        setUpListViewListener();
    }

    //kiren's code to create an unique id
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

    public void containsUniqueId(){
        String uniqueId ="";

        if(!pref.contains("uniqueId")){
            patientObject = new ParseObject("Patient");
            patientObject.put("init", true);
            uniqueId = uniqueIdReturned();
            patientObject.put("uniqueId", uniqueId);
            patientObject.saveInBackground();

            editor.putString("objectId", patientObject.getObjectId());
            editor.putString("uniqueId", uniqueId);
            textView.setText(uniqueId);
            editor.commit();
        }
        else {
            textView.setText(pref.getString("uniqueId", "didn't get unique id"));
        }
    }



    // Watches for a long press that will remove an task from the task list
    private void setUpListViewListener() {
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Context context = getApplicationContext();
                Toast.makeText(context, "Task Removed", Toast.LENGTH_LONG).show();

                items.remove(position);
                itemsAdapter.notifyDataSetChanged();

                patientObject.put("arrayToDos", items);
                patientObject.saveInBackground();
                return true;
            }
        });
    }

    private void findObjectId(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Patient");
        query.whereEqualTo("uniqueId", pref.getString("uniqueId", "didn't get unique id"));

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    if (objects.size() > 0) {
                        for (ParseObject object : objects) {
                            patientObject = object;
                            List<Object> item = object.getList("arrayToDos");
                            if (item == null) {
                            }
                            else {
                                for (int i = 0; i < item.size(); i++) {
                                    items.add(item.get(i).toString());
                                }
                                itemsAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                }
            }
        });
    }
}
