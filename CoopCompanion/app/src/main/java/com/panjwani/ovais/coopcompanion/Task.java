package com.panjwani.ovais.coopcompanion;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Mohammed on 10/31/2016.
 */

public class Task implements Comparable<Task>{
    public String name;
    public ArrayList<String> peopleAssigned;
    public Date date;
    public boolean repeatable;
    public String repeat;
    public String description;

    public Task(String name, ArrayList<String> peopleAssigned, Date date, boolean repeatable, String repeat, String description) {
        this.name = name;
        this.peopleAssigned = peopleAssigned;
        this.date = date;
        this.repeatable = repeatable;
        this.repeat = repeat;
        this.description = description;
    }

    @Override
    public int compareTo(Task o) {
        return this.date.compareTo(o.date);
    }
}
