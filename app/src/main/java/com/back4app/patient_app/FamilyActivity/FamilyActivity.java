package com.back4app.patient_app.FamilyActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.back4app.patient_app.adapters.FamilyListAdapter;
import com.back4app.patient_app.models.Family;
import com.back4app.patient_app.viewmodels.FamilyViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class FamilyActivity extends AppCompatActivity {

    private static final String TAG = "FamilyActivity" ;
    static ArrayList<String> Families;




    // UI
    TextView mPatientidTextView;
    FamilyListAdapter mAdapter;

    public static final int NEW_Family_ACTIVITY_REQUEST_CODE = 1;
    private FamilyViewModel mFamilyViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);


//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        mAdapter = new FamilyListAdapter(this);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAdapter.setOnItemClickListener(new FamilyListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Log.d(TAG, "onItemClick: " + position  + "  is cliked");
                Intent intent = new Intent(FamilyActivity.this, NewFamilyActivity.class);

                Family family = mAdapter.getFamilyAtPosition(position);
                intent.putExtra("mode", "edit" );
                intent.putExtra("family", family);


                startActivityForResult(intent, 1);
                mFamilyViewModel.deleteFamily(family);

            }
        });



        // Get a new or existing ViewModel from the ViewModelProvider.
        mFamilyViewModel = ViewModelProviders.of(this).get(FamilyViewModel.class);

        // Add an observer on the LiveData returned by getAlphabetizedFamilys.
        // The onChanged() method fires when the observed data changes and the activity is
        // in the foreground.
        mFamilyViewModel.getAllFamilies().observe(this, new Observer<List<Family>>() {
            @Override
            public void onChanged(@Nullable final List<Family> Families) {
                // Update the cached copy of the Families in the adapter.
                mAdapter.setmFamilies(Families);
            }
        });


        //Floating button button
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FamilyActivity.this, NewFamilyActivity.class);
                Family family = new Family("name", "Friend", "Description", "URi" );
                intent.putExtra("mode", "add" );
                startActivityForResult(intent, 1);
            }
        });


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
                        Family myFamily = mAdapter.getFamilyAtPosition(position);
                        Toast.makeText(FamilyActivity.this, "Deleting " +
                                myFamily.getName(), Toast.LENGTH_LONG).show();

                        // Delete the Family
                        mFamilyViewModel.deleteFamily(myFamily);
                    }
                });
        helper.attachToRecyclerView(recyclerView);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_Family_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Family family = data.getParcelableExtra(NewFamilyActivity.EXTRA_REPLY_Family);
            mFamilyViewModel.insert(family);
            Log.d(TAG, "onActivityResult: " + family.toString());
        }
//        else if{
//            Family family = data.getParcelableExtra(NewFamilyActivity.EXTRA_REPLY_Family);
//            mFamilyViewModel.deleteFamily();
//            Log.d(TAG, "onActivityResult: " + family.toString());
//        }
        else {
            Toast.makeText(
                    getApplicationContext(),
                    R.string.empty_not_saved,
                    Toast.LENGTH_LONG).show();
        }
    }

}
