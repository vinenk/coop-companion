package com.panjwani.ovais.coopcompanion;

import android.graphics.Bitmap;

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
    public Bitmap image;
    public boolean collection;
    public String description;
    public SingleStatus singleStatus;
    public CollectionStatus collectionStatus;

    public Resource(String name, Bitmap image, ArrayList<String> peopleAssigned, boolean collection, String description,
                    SingleStatus singleStatus, CollectionStatus collectionStatus) {
        this.name = name;
        this.image = image;
        this.peopleAssigned = peopleAssigned;
        this.collection = collection;
        this.description = description;
        this.singleStatus = singleStatus;
        this.collectionStatus = collectionStatus;
    }
}
