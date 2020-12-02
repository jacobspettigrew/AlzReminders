/*

HW 4

Course: CMPT 385 Software Engineering
Instructor: Dr. Herbert H. Tsang
Description: <
     Roomdatbase to initialize the local database
    >
Due date: < 2020/12/02 >
FILE NAME:FamilyRoomDatabase.java
TEAM NAME: Alzreminders
Author: < Kyung Cheol Koh >
Input: < None>
Output: < Initialize the database >
I pledge that I have completed the programming assignment independently.
I have not copied the code from a student or any source.
I have not given my code to any student.

Sign here: __Kyung Cheol Koh______
*/

package com.back4app.patient_app.persistence;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.back4app.patient_app.models.Family;


@Database(entities = {Family.class}, version = 8, exportSchema = false)
public abstract class FamilyRoomDatabase extends RoomDatabase {

    public abstract FamilyDao FamilyDao();
    private static FamilyRoomDatabase INSTANCE;

    public static FamilyRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (FamilyRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            FamilyRoomDatabase.class, "Family_database")
                            // Wipes and rebuilds instead of migrating
                            // if no Migration object.
                            // Migration is not part of this practical.
                            .fallbackToDestructiveMigration()
                            //.addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
