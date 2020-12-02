/*

HW 4

Course: CMPT 385 Software Engineering
Instructor: Dr. Herbert H. Tsang
Description: <
     The repository to control database
     The repository serves as a path to the local and cloud database.
    >
Due date: < 2020/12/02 >
FILE NAME:FamilyRepository.java
TEAM NAME: Alzreminders
Author: < Kyung Cheol Koh >
Input: < None>
Output: < Initialize the database  >
I pledge that I have completed the programming assignment independently.
I have not copied the code from a student or any source.
I have not given my code to any student.

Sign here: __Kyung Cheol Koh______
*/


package com.back4app.patient_app.repositories;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.room.RoomDatabase;

import com.back4app.patient_app.models.Family;
import com.back4app.patient_app.persistence.FamilyDao;
import com.back4app.patient_app.persistence.FamilyRoomDatabase;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

import static com.parse.Parse.getApplicationContext;

public class FamilyRepository {
    //LiveData and Dao
    private FamilyDao mFamilyDao;
    private LiveData<List<Family>> mAllFamilies;
    private List<Family> mlistTasks;
    SharedPreferences mPref;
    SharedPreferences.Editor mEditor;

    //Constructor to pass TaskDao and LiveData
    public FamilyRepository(Application application) {
        RoomDatabase db = FamilyRoomDatabase.getDatabase(application);
        mFamilyDao = ((FamilyRoomDatabase) db).FamilyDao();

        mAllFamilies = mFamilyDao.getAllFamilies();
    }

    public LiveData<List<Family>> getAllFamilies() {
        return mAllFamilies;
    }

    //insert,update and delete family
    public void insert (Family family) {
        new FamilyRepository.insertAsyncFamily(mFamilyDao).execute(family);
    }

    public void update (Family family){
        new FamilyRepository.updateAsyncFamily(mFamilyDao).execute(family);
    }

    public void deleteAll() {
        new FamilyRepository.deleteAllFamiliesAsyncTask(mFamilyDao).execute();
    }

    public void deleteFamily(Family Family) {
        new FamilyRepository.deleteFamilyAsyncTask(mFamilyDao).execute(Family);
    }


    //Async function to insert a family which runs in the background thread
    private static class insertAsyncFamily extends AsyncTask<Family, Void, Void> {

        private FamilyDao mAsyncTaskDao;
        insertAsyncFamily(FamilyDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Family... params) {
            try {
                mAsyncTaskDao.insert(params[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    //Async function to update a family which runs in the background thread
    private static class updateAsyncFamily extends AsyncTask<Family, Void, Void> {

        private FamilyDao mAsyncTaskDao;
        updateAsyncFamily(FamilyDao dao) {
            mAsyncTaskDao = dao;
        }


        @Override
        protected Void doInBackground(final Family... params) {
            try {
                mAsyncTaskDao.update(params[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }




    //Async function to delete all family which runs in the background thread
    private static class deleteAllFamiliesAsyncTask extends AsyncTask<Void, Void, Void> {
        private FamilyDao mAsyncTaskDao;

        deleteAllFamiliesAsyncTask(FamilyDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }

    //Async function to delete a family which runs in the background thread
    private static class deleteFamilyAsyncTask extends AsyncTask<Family, Void, Void> {
        private FamilyDao mAsyncTaskDao;

        deleteFamilyAsyncTask(FamilyDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Family... params) {
            try {
                mAsyncTaskDao.deleteFamily(params[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
