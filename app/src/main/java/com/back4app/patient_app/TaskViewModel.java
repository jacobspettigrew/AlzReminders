package com.back4app.patient_app;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.parse.ParseException;

import java.util.List;

public class TaskViewModel extends AndroidViewModel {
    private TaskRepository mRepository;

    private LiveData<List<Task>> mAllTasks;

    public TaskViewModel (Application application) {
        super(application);
        mRepository = new TaskRepository(application);
        mAllTasks = mRepository.getAllTasks();
    }

    LiveData<List<Task>> getAllTasks() { return mAllTasks; }

    public void deleteAll() {
        mRepository.deleteAll();
    }

    public void deleteTask(Task Task) {
        mRepository.deleteTask(Task);
    }

    public void getTaskFromDatabse(){
        mRepository.getTaskFromDatabase();
    }

    public void insert(Task Task) { mRepository.insert(Task); }

    public void insertToDatabase(Task Task) throws ParseException {
        mRepository.insertToDatabase(Task);
    }

    public void deeletaTaskToDatabse(Task task, int position) {
        mRepository.deleteTaskToDatabse(task,position);
    }
}
