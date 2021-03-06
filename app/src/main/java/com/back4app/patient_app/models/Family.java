/*

HW 4

Course: CMPT 385 Software Engineering
Instructor: Dr. Herbert H. Tsang
Description: <
     The model and entity class: The class includes all the necessary information stored for Family
    >
Due date: < 2020/12/02 >
FILE NAME:Family.java
TEAM NAME: Alzreminders
Author: < Kyung Cheol Koh >
Input: < None>
Output: < Stores the actual tasks  >
I pledge that I have completed the programming assignment independently.
I have not copied the code from a student or any source.
I have not given my code to any student.

Sign here: __Kyung Cheol Koh______
*/


package com.back4app.patient_app.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Objects;

import static androidx.room.Entity.*;

@Entity(tableName = "family_table")
public class Family implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "name")
    @NonNull
    private String mName;

    @ColumnInfo(name = "relationship")
    private String mRelationship;

    @ColumnInfo(name = "description")
    private String mDescription;

    @ColumnInfo(name = "image_url")
    private String mImage_url;

    protected Family(Parcel in) {
        id = in.readInt();
        mName = in.readString();
        mRelationship = in.readString();
        mDescription = in.readString();
        mImage_url = in.readString();
    }

    public static final Creator<Family> CREATOR = new Creator<Family>() {
        @Override
        public Family createFromParcel(Parcel in) {
            return new Family(in);
        }

        @Override
        public Family[] newArray(int size) {
            return new Family[size];
        }
    };


    //Constructors
    public Family( String name, String relationship, String description, String image_url) {
        this.mName = name;
        this.mRelationship = relationship;
        this.mDescription = description;
        this.mImage_url = image_url;
    }
    @Ignore
    public Family(int id, String name, String relationship, String description, String image_url) {
        this.id = id;
        this.mName = name;
        this.mRelationship = relationship;
        this.mDescription = description;
        this.mImage_url = image_url;
    }

    @NonNull
    public String getName() {
        return this.mName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    //Used for passing between intents
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(mName);
        parcel.writeString(mDescription);
        parcel.writeString(mRelationship);
        parcel.writeString(mImage_url);
    }

    //Getter and Setters
    public String getRelationship() {
        return this.mRelationship;
    }

    public void setRelationship(String mRelationship) {
        this.mRelationship = mRelationship;
    }

    public String getDescription() {
        return this.mDescription;
    }

    public void setDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public String getImage_url() {
        return this.mImage_url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public void setImage_url(String image_url) {
        this.mImage_url = image_url;
    }


    //Tostring method
    @Override
    public String toString() {
        return "Family{" +
                "id=" + getId() +
                ", mName='" + mName + '\'' +
                ", mRelationship='" + mRelationship + '\'' +
                ", mDescription='" + mDescription + '\'' +
                ", mImage_url='" + mImage_url + '\'' +
                '}';
    }

    //Used for testing
    @Override
    public boolean equals(@Nullable Object obj) {
        if(obj == null){
            return false;
        }
        if(getClass() == null){
            return false;
        }
        Family family = (Family) obj;
        return family.getId() == getId()
                && family.getName() == getName()
                && family.getDescription() == getDescription()
                && family.getRelationship() == getRelationship()
                && family.getImage_url() == getImage_url();
    }

}
