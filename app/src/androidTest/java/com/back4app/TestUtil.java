package com.back4app;

import com.back4app.patient_app.models.Family;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TestUtil {

    public static final Family TEST_Family_1 = new Family("a","a","a","a");


    public static final Family TEST_Family_2 = new Family("b","b","b","b");

    public static final List<Family> Test_Family_LIST = Collections.unmodifiableList(
            new ArrayList<Family>(){{
                add(new Family("a","a","a","a"));
                add(new Family("b","b","b","b"));
            }}
    );
}
