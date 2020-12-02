/*

HW 4

Course: CMPT 385 Software Engineering
Instructor: Dr. Herbert H. Tsang
Description: <
     Actual queries to the database: add, delete, update and get Livedata
    >
Due date: < 2020/12/02 >
FILE NAME:FamilyDao.java
TEAM NAME: Alzreminders
Author: < Kyung Cheol Koh >
Input: < None>
Output: < Initialize the database  >
I pledge that I have completed the programming assignment independently.
I have not copied the code from a student or any source.
I have not given my code to any student.

Sign here: __Kyung Cheol Koh______
*/

package com.back4app.patient_app.persistence;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.back4app.patient_app.models.Family;

import java.util.List;

import io.reactivex.rxjava3.core.Single;

@Dao
public interface FamilyDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Family family) ;
    @Query("DELETE FROM family_table")
    void deleteAll();
    @Delete
    void deleteFamily(Family family) ;
    @Update
    void update(Family ... family) ;

    @Query("SELECT * from family_table ORDER BY name ASC")
    LiveData<List<Family>> getAllFamilies();
}
