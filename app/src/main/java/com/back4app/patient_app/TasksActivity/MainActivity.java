/*

HW 4

Course: CMPT 385 Software Engineering
Instructor: Dr. Herbert H. Tsang
Description: <
     GRABS THE DATA FROM THE BACK4APPS DATABASE
        SHOW THE LIST OF TASKS FOR PATIENTS
    >
Due date: < 2020/12/02 >
FILE NAME: MainAcitivty.java
TEAM NAME: Alzreminders
Author: < Kyung Cheol Koh >
Input: < None>
Output: < Initialize the database  >
I pledge that I have completed the programming assignment independently.
I have not copied the code from a student or any source.
I have not given my code to any student.

Sign here: __Kyung Cheol Koh______
*/



package com.back4app.patient_app.TasksActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;


import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.back4app.patient_app.R;
import com.back4app.patient_app.adapters.TaskListAdapter;
import com.back4app.patient_app.models.Task;
import com.back4app.patient_app.viewmodels.TaskViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;



public class MainActivity extends AppCompatActivity {

    static ArrayList<String> Tasks;
    private SwipeRefreshLayout mSwipeRefresh;

    //DATABASE
    private ParseObject mPatientObject;
    static SharedPreferences mPref;
    SharedPreferences.Editor mEditor;

    // UI
    TextView mPatientidTextView;
    TaskListAdapter adapter;

    public static final int NEW_Task_ACTIVITY_REQUEST_CODE = 1;
    private TaskViewModel mTaskViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        adapter = new TaskListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Get a new or existing ViewModel from the ViewModelProvider.
        mTaskViewModel = ViewModelProviders.of(this).get(TaskViewModel.class);

        // Add an observer on the LiveData returned by getAlphabetizedTasks.
        // The onChanged() method fires when the observed data changes and the activity is
        // in the foreground.
        mTaskViewModel.getAllTasks().observe(this, new Observer<List<Task>>() {
            @Override
            public void onChanged(@Nullable final List<Task> Tasks) {
                // Update the cached copy of the Tasks in the adapter.
                adapter.setTasks(Tasks);
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NewTaskActivity.class);
                startActivityForResult(intent, NEW_Task_ACTIVITY_REQUEST_CODE);
            }
        });



        //DATABASE
        mPref = getApplicationContext().getSharedPreferences("Storage", 0);
        mEditor = mPref.edit();

        //CHECK IF UNIQUE ID EXISTS
        containsUniqueId();

        //swipe to delete
        ItemTouchHelper helper = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(0,
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(RecyclerView recyclerView,
                                          RecyclerView.ViewHolder viewHolder,
                                          RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder,
                                         int direction) {
                        int position = viewHolder.getAdapterPosition();
                        Task myTask = adapter.getTaskAtPosition(position);
                        Toast.makeText(MainActivity.this, "Deleting " +
                                myTask.getTask(), Toast.LENGTH_LONG).show();

                        // Delete the Task
                        mTaskViewModel.deleteTask(myTask);
                        mTaskViewModel.deeletaTaskToDatabse(myTask,position);
                    }
                });
        helper.attachToRecyclerView(recyclerView);

                //REFRESH LISTENER
        mSwipeRefresh = findViewById(R.id.swiperefreshItem);
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                mTaskViewModel.getTaskFromDatabse();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mSwipeRefresh.isRefreshing()) {
                            mSwipeRefresh.setRefreshing(false);
                        }
                    }
                }, 1000);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_Task_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Task Task = new Task(data.getStringExtra(NewTaskActivity.EXTRA_REPLY_Tasks));
            mTaskViewModel.insert(Task);
            try {
                mTaskViewModel.insertToDatabase(Task);
            } catch (ParseException e) {
                e.printStackTrace();
            }

        } else {
            Toast.makeText(
                    getApplicationContext(),
                    R.string.empty_not_saved,
                    Toast.LENGTH_LONG).show();
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

        if (!mPref.contains("uniqueId")) {
            mPatientObject = new ParseObject("Patient");
            mPatientObject.put("init", true);
            uniqueId = uniqueIdReturned();
            mPatientObject.put("uniqueId", uniqueId);
            mPatientObject.saveInBackground();

            mEditor.putString("objectId", mPatientObject.getObjectId());
            mEditor.putString("uniqueId", uniqueId);

            mEditor.commit();
        }
    }

    // WATCHES FOR THE LONG PRESS TO DELETE
//    private void setUpListViewListener() {
//        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                Context context = getApplicationContext();
//                Toast.makeText(context, "Task Removed", Toast.LENGTH_LONG).show();
//
//                mItems.remove(position);
//                mItemsAdapter.notifyDataSetChanged();
//                //PUT DELETED LIST TO THE DATABASE
//                mPatientObject.put("arrayToDos", mItems);
//
//                mPatientObject.saveInBackground();
//                return true;
//            }
//        });
//    }

    //ITERATION TO FIND THE TASKS GIVEN THE UNIQUE ID
    private void findObjectId() {
        Tasks = new ArrayList<>();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Patient");
        query.whereEqualTo("uniqueId", mPref.getString("uniqueId", "didn't get unique id"));

        query.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    if (objects.size() > 0) {
                        for (ParseObject object : objects) {
                            List<Object> item = object.getList("arrayToDos");
                            if (item == null) {
                                Log.d("Room", "done:  item empty");
                            } else {
                                List<Task> listTasks = new LinkedList<>();

                                for (int i = 0; i < item.size(); i++) {
                                    Task task = new Task(item.get(i).toString());
                                    listTasks.add(task);
                                    Tasks.add(item.get(i).toString());
                                    Log.d("Room", "done: " + item.get(i).toString());
                                }
                                adapter.setTasks(listTasks);

                            }
                        }
                    }
                }
            }
        });
    }
}