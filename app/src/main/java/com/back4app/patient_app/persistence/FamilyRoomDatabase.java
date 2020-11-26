package com.back4app.patient_app.persistence;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.back4app.patient_app.models.Family;


@Database(entities = {Family.class}, version = 5, exportSchema = false)
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
