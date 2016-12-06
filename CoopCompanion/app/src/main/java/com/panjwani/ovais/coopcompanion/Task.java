package com.panjwani.ovais.coopcompanion;

import java.util.ArrayList;

/**
 * Created by Mohammed on 10/31/2016.
 */

public class Task implements Comparable<Task>{
    public String name;
    public ArrayList<String> peopleAssigned;
    public String personAssigned;
    public Long date;   //changed to Long Date to work with firebase database
    public boolean repeatWeekly;
    public String description;
    public String tID;

    //Default constructor required for firebase database
    public Task() {

    }

    public Task(String name, ArrayList<String> peopleAssigned, Long date, boolean repeatWeekly, String description) {
        this.name = name;
        this.peopleAssigned = peopleAssigned;
        this.date = date;
        this.repeatWeekly = repeatWeekly;
        this.description = description;
    }

    @Override
    public int compareTo(Task o) {
        return this.date.compareTo(o.date);
    }

    //getters required for storing custom objects in firebase database
    public String getName() {
        return name;
    }

    public ArrayList<String> getPeopleAssigned() {
        return peopleAssigned;
    }

    //changed to Long Date to work with firebase database
    public Long getDate() {
        return date;
    }

    public boolean isRepeatWeekly() {
        return repeatWeekly;
    }

    public String getDescription() {
        return description;
    }

    public String gettID() {
        return tID;
    }
}
