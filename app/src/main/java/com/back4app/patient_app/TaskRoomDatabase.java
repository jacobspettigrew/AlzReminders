package com.back4app.patient_app;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import static com.back4app.patient_app.MainActivity.Tasks;
import static com.back4app.patient_app.MainActivity.mPref;

@Database(entities = {Task.class}, version = 1, exportSchema = false)
public abstract class TaskRoomDatabase extends RoomDatabase {
    public abstract TaskDao TaskDao();
    private static TaskRoomDatabase INSTANCE;


    static TaskRoomDatabase getDatabase(final Context context) {
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

//    private static  RoomDatabase.Callback sBack4appCallback =
//            new RoomDatabase.Callback(){
//
//                @Override
//                public void onOpen(@NonNull SupportSQLiteDatabase db){
//                    super.onOpen(db);
//
//                }
//            };
//
//    private static RoomDatabase.Callback sRoomDatabaseCallback =
//            new RoomDatabase.Callback(){
//
//                @Override
//                public void onOpen (@NonNull SupportSQLiteDatabase db){
//                    super.onOpen(db);
//
//                        new PopulateDbAsync(INSTANCE).execute();
//
//                }
//            };
//
//    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {
//
//        private final TaskDao mDao;
//        ArrayList<String> tasks;
//
//        PopulateDbAsync(TaskRoomDatabase db)  {
//            mDao = db.TaskDao();
//            tasks = new ArrayList();
//            tasks.add("hel");
//        }
//
//        @Override
//        protected  Void doInBackground(final Void... params) {
//            // Start the app with a clean database every time.
//            // Not needed if you only populate the database
//            // when it is first created
//            mDao.deleteAll();
//
//                for (int i = 0; i <= tasks.size() - 1; i++) {
//                    //Task task = new Task(Tasks.get(i));
//                    Task task = new Task(tasks.get(i));
//                    mDao.insert(task);
//                }
//
//
//            return null;
//        }
//    }
}
