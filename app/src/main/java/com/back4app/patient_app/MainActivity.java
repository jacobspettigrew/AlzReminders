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

//        editor.remove("uniqueId");
//        editor.commit();
        if(!pref.contains("uniqueId")){

            ParseObject patient = new ParseObject("Patient");
            patient.put("init", true);
            String objectId;
            patient.put("uniqueId", "uniqueIdssss");
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
            textView.setText("objectId");
            editor.commit();
        }
        else {
            textView.setText(pref.getString("uniqueId", "didn't get unique id"));

        }


//        ParseUser.logInInBackground("john1", "password", new LogInCallback() {
//                    @Override
//                    public void done(ParseUser user, ParseException e) {
//                        EditText textView = findViewById(R.id.textView);
//                        textView.setText(user.getString("strings"));
//                    }
//                }
//        );



    }

}
