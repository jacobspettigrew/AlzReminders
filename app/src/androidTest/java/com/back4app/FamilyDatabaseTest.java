package com.back4app;

import androidx.lifecycle.LiveData;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import com.back4app.patient_app.models.Family;
import com.back4app.patient_app.persistence.FamilyDao;
import com.back4app.patient_app.persistence.FamilyRoomDatabase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static com.google.common.truth.Truth.assertThat;

public class FamilyDatabaseTest {

    private FamilyRoomDatabase db;
    private FamilyDao familyDao;
    private LiveData<List<Family>> mAllFamilies;

    public FamilyDao FamilyDao(){
        return db.FamilyDao();
    }

    @Before
    public void databaseInit(){
        db = Room.inMemoryDatabaseBuilder(
                ApplicationProvider.getApplicationContext(),
                FamilyRoomDatabase.class
        ).build();
        familyDao = ((FamilyRoomDatabase) db).FamilyDao();
        mAllFamilies = familyDao.getAllFamilies();
    }

    @After
    public void databaseClose(){
        db.close();
    }

    @Test public void writeFamilyAndReadInList() throws Exception {
        Family family1 = new Family("a", "a","a","a");
        familyDao.insert(family1);
        List<Family> familyList = familyDao.getAllFamilies().getValue();
        assertThat(familyList.get(0).equals(family1));
    }
}
