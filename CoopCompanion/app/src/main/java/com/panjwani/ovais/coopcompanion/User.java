package com.panjwani.ovais.coopcompanion;


import java.util.ArrayList;

public class User {
    protected String firstName;
    protected String lastName;
    protected String email;
    protected boolean admin;
    protected boolean residentAdmin;
    protected String groupName;
    protected ArrayList<String> groupMembers;
    protected ArrayList<String> groupMembersVariable;
    protected ArrayList<String> resourceIDs;
    protected ArrayList<String> taskIDs;
    protected String uID;

    public User(){

    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public boolean isAdmin() {
        return admin;
    }

    public boolean isResidentAdmin() {
        return residentAdmin;
    }

    public String getGroupName() {
        return groupName;
    }

    public ArrayList<String> getGroupMembers() {
        return groupMembers;
    }

    public ArrayList<String> getGroupMembersVariable() {
        return groupMembersVariable;
    }

    public ArrayList<String> getResourceIDs() {
        return resourceIDs;
    }

    public String getuID() {
        return uID;
    }

    public ArrayList<String> getTaskIDs() {
        return taskIDs;
    }

}
