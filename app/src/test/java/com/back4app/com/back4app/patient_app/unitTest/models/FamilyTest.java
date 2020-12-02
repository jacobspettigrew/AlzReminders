package com.back4app.com.back4app.patient_app.unitTest.models;

import com.back4app.patient_app.models.Family;

import org.junit.Assert;
import org.junit.Test;

public class FamilyTest {


    //compare families with the same id
    @Test
    public void isFamilyEqual_identicalFamily_returnTrue() {

        Family family1 = new Family("a", "a","a","a");
        Family family2 = new Family("a", "a","a","a");

        Assert.assertEquals(family1,family2);
        System.out.println("The familes are equal" + family1.getId() + "   " + family2.getId());
    }

    //compare families withe the different ids
    @Test
    public void isFamiliesEqual_differentIds_returnFalse() {

        Family family1 = new Family("a", "a","a","a");
        Family family2 = new Family("a", "a","a","a");

        family1.setId(1);
        family2.setId(2);

        Assert.assertNotEquals(family1,family2);
        System.out.println("The familes are equal" + family1.getId() + "   " + family2.getId());
    }

    //compare families withe the different names
    @Test
    public void isFamiliesEqual_differentNames_returnFalse() {

        Family family1 = new Family("a", "a","a","a");
        Family family2 = new Family("b", "a","a","a");


        Assert.assertNotEquals(family1,family2);
        System.out.println("The familes are equal" + family1.getId() + "   " + family2.getId());
    }

    //compare families withe the different names
    @Test
    public void isFamiliesEqual_differentDescriptions_returnFalse() {

        Family family1 = new Family("a", "a","a","a");
        Family family2 = new Family("b", "a","b","a");


        Assert.assertNotEquals(family1,family2);
        System.out.println("The familes are equal" + family1.getId() + "   " + family2.getId());
    }

    //compare families withe the different names
    @Test
    public void isFamiliesEqual_differentUrls_returnFalse() {

        Family family1 = new Family("a", "a","a","a");
        Family family2 = new Family("b", "a","a","b");


        Assert.assertNotEquals(family1,family2);
        System.out.println("The familes are not equal" + family1.getId() + "   " + family2.getId());
    }







}
