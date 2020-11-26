package com.back4app.patient_app.persistence;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.back4app.patient_app.models.Family;
import com.back4app.patient_app.models.Task;

import java.util.List;

@Dao
public interface FamilyDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Family family);
    @Query("DELETE FROM family_table")
    void deleteAll();
    @Delete
    void deleteFamily(Family family);


    @Query("SELECT * from family_table ORDER BY name ASC")
    LiveData<List<Family>> getAllFamilies();
}
