package com.back4app.patient_app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Save the current Installation to Back4App
        ParseInstallation.getCurrentInstallation().saveInBackground();





        ParseUser.logInInBackground("john1", "password", new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        EditText textView = findViewById(R.id.textView);

                        textView.setText(user.getString("strings"));
                    }
                }
        );



    }

}
