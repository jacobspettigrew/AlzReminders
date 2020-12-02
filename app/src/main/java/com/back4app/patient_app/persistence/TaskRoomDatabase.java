/*

HW 4

Course: CMPT 385 Software Engineering
Instructor: Dr. Herbert H. Tsang
Description: <
     Roomdatbase to initialize the local database
    >
Due date: < 2020/12/02 >
FILE NAME:TaskRoomDatabase.java
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

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import android.content.Context;

import com.back4app.patient_app.models.Task;

@Database(entities = {Task.class}, version = 1, exportSchema = false)
public abstract class TaskRoomDatabase extends RoomDatabase {
    public abstract TaskDao TaskDao();
    private static TaskRoomDatabase INSTANCE;


    public static TaskRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (TaskRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            TaskRoomDatabase.class, "Task_database")
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
