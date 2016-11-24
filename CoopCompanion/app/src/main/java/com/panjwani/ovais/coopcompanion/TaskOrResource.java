package com.panjwani.ovais.coopcompanion;

import java.util.ArrayList;

public class TaskOrResource implements Comparable<TaskOrResource>{
    String name;
    public ArrayList<String> peopleAssigned;
    boolean isTask;
    Task task;
    Resource resource;

    public TaskOrResource(Task task) {
        this.name = task.name;
        this.peopleAssigned = task.peopleAssigned;
        isTask = true;
        this.task = task;
    }

    public TaskOrResource(Resource resource) {
        this.name = resource.name;
        this.peopleAssigned = resource.peopleAssigned;
        isTask = false;
        this.resource = resource;
    }

    @Override
    public int compareTo(TaskOrResource o) {
        if (this.isTask && o.isTask) {
            return this.task.compareTo(o.task);
        } else if (!this.isTask && o.isTask) {
            return -1;
        } else if (this.isTask && !o.isTask) {
            return 1;
        } else {
            return 0;
        }
    }
}
