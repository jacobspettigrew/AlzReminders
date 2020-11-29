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
    private FamilyDao mFamilyDao;
    private LiveData<List<Family>> mAllFamilies;
    private List<Family> mlistTasks;
    SharedPreferences mPref;
    SharedPreferences.Editor mEditor;

    public FamilyRepository(Application application) {
        RoomDatabase db = FamilyRoomDatabase.getDatabase(application);
        mFamilyDao = ((FamilyRoomDatabase) db).FamilyDao();


        mAllFamilies = mFamilyDao.getAllFamilies();
    }

    public LiveData<List<Family>> getAllFamilies() {
        return mAllFamilies;
    }

    public void insert (Family family) {
        new FamilyRepository.insertAsyncFamily(mFamilyDao).execute(family);
    }

    public void update (Family family){
        new FamilyRepository.updateAsyncFamily(mFamilyDao).execute(family);
    }

    public void deleteAll() {
        new FamilyRepository.deleteAllFamiliesAsyncTask(mFamilyDao).execute();
    }

    // Need to run off main thread
    public void deleteFamily(Family Family) {
        new FamilyRepository.deleteFamilyAsyncTask(mFamilyDao).execute(Family);
    }


    private static class insertAsyncFamily extends AsyncTask<Family, Void, Void> {

        private FamilyDao mAsyncTaskDao;

        insertAsyncFamily(FamilyDao dao) {
            mAsyncTaskDao = dao;
        }


        @Override
        protected Void doInBackground(final Family... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class updateAsyncFamily extends AsyncTask<Family, Void, Void> {

        private FamilyDao mAsyncTaskDao;

        updateAsyncFamily(FamilyDao dao) {
            mAsyncTaskDao = dao;
        }


        @Override
        protected Void doInBackground(final Family... params) {
            mAsyncTaskDao.update(params[0]);
            return null;
        }
    }




    /**
     * Delete all Tasks from the database (does not delete the table)
     */
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

    /**
     *  Delete a single Task from the database.
     */
    private static class deleteFamilyAsyncTask extends AsyncTask<Family, Void, Void> {
        private FamilyDao mAsyncTaskDao;

        deleteFamilyAsyncTask(FamilyDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Family... params) {
            mAsyncTaskDao.deleteFamily(params[0]);
            return null;
        }
    }
}
