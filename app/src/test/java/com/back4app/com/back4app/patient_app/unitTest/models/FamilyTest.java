package com.back4app.com.back4app.patient_app.unitTest.models;

import com.back4app.patient_app.models.Family;

import org.junit.Assert;
import org.junit.Test;

public class FamilyTest {

    @Test
    public void isFamilyEqual_identicalFamily_returnTrue() {

        Family family1 = new Family("a", "a","a","a");
        Family family2 = new Family("a", "a","a","a");

        Assert.assertEquals(family1,family2);
        System.out.println("The notes are equal" + family1.getId() + "   " + family2.getId());


    }
}
