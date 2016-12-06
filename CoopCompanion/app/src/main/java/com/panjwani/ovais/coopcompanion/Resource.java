package com.panjwani.ovais.coopcompanion;

import android.graphics.Bitmap;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;

/**
 * Created by Mohammed on 10/31/2016.
 */

public class Resource {

    public enum SingleStatus {
        EMPTY,
        AVAILABLE
    }

    public enum CollectionStatus {
        DEPLETED,
        AVAILABLE
    }

    public String name;
    public ArrayList<String> peopleAssigned;
    //public Bitmap image;
    public boolean collection;
    public String description;
    public SingleStatus singleStatus;
    public CollectionStatus collectionStatus;
    public String rID;

    //default constructor required to store custom object
    public Resource() {

    }

    //removed Bitmap image from constructor
    public Resource(String name, ArrayList<String> peopleAssigned, boolean collection, String description,
                    SingleStatus singleStatus, CollectionStatus collectionStatus) {
        this.name = name;
        //this.image = image;
        this.peopleAssigned = peopleAssigned;
        this.collection = collection;
        this.description = description;
        this.singleStatus = singleStatus;
        this.collectionStatus = collectionStatus;
    }

    //Required to make enums work with firebase database

    // The Firebase data mapper will ignore this
    @Exclude
    public SingleStatus getSingleStatusAsEnum() {
        return singleStatus;
    }

    public String getSingleStatus() {
        // Convert enum to string
        if (singleStatus == null) {
            return null;
        } else {
            return singleStatus.name();
        }
    }

    public void setSingleStatus(String singleStatusString) {
        // Get enum from string
        if (singleStatusString == null) {
            this.singleStatus = null;
        } else {
            this.singleStatus = SingleStatus.valueOf(singleStatusString);
        }
    }

    // The Firebase data mapper will ignore this
    @Exclude
    public CollectionStatus getCollectionStatusAsEnum() {
        return collectionStatus;
    }

    public String getCollectionStatus() {
        // Convert enum to string
        if (collectionStatus == null) {
            return null;
        } else {
            return collectionStatus.name();
        }
    }

    public void setCollectionStatus(String collectionStatusString) {
        // Get enum from string
        if (collectionStatusString == null) {
            this.collectionStatus = null;
        } else {
            this.collectionStatus = CollectionStatus.valueOf(collectionStatusString);
        }
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<String> getPeopleAssigned() {
        return peopleAssigned;
    }

    public boolean isCollection() {
        return collection;
    }

    public String getrID() {
        return rID;
    }

}
