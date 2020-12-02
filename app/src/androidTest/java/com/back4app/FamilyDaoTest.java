package com.back4app;

import com.back4app.patient_app.models.Family;
import com.back4app.patient_app.persistence.FamilyRoomDatabase;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class FamilyDaoTest  extends FamilyDatabaseTest {


    @Test
    public void insertRead() throws Exception{
        Family family = new Family("a", "a", "a", "a");

        FamilyDao().insert(family);

        LiveDataTestUtil<List<Family>> liveDataTestUtil = new LiveDataTestUtil<>();
        List<Family> insertedFamily = liveDataTestUtil.getValue(FamilyDao().getAllFamilies());

        assertNotNull(insertedFamily);
        assertEquals(family.getName(), insertedFamily.get(0).getName());




    }
}
