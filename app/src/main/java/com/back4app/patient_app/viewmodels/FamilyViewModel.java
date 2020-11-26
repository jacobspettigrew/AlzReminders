package com.back4app.patient_app.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.back4app.patient_app.models.Family;
import com.back4app.patient_app.models.Family;
import com.back4app.patient_app.repositories.FamilyRepository;
import com.back4app.patient_app.repositories.FamilyRepository;
import com.parse.ParseException;

import java.util.List;

public class FamilyViewModel extends AndroidViewModel {
    private FamilyRepository mRepository;

    private LiveData<List<Family>> mAllFamilies;

    public FamilyViewModel (Application application) {
        super(application);
        mRepository = new FamilyRepository(application);
        mAllFamilies = mRepository.getAllFamilies();
    }

    public LiveData<List<Family>> getAllFamilies() { return mAllFamilies; }

    public void deleteAll() {
        mRepository.deleteAll();
    }

    public void deleteFamily(Family Family) {
        mRepository.deleteFamily(Family);
    }

    public void insert(Family Family) { mRepository.insert(Family); }


}
