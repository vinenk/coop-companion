package com.panjwani.ovais.coopcompanion;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FirebaseDatabaseManager implements Parcelable{

    public interface userObjectListener{
        void adminUserCallback(User admin);
        void userCallback(User user);
    }

    public interface checkGroupNameInListListener{
        void result(boolean result);
    }

    public interface groupNameListListener{
        void groupNameListCallback(HashMap<String, String> map);
    }

    public interface userObjectUpdateListener{
        void updateUserObject(User u);
    }

    public interface taskListListener{
        void taskListCallback(ArrayList<Task> tasks);
    }

    public interface resourceListListener{
        void resourceListCallback(ArrayList<Resource> resources);
    }

    public interface taskListener{
        void taskCallback(Task task);
    }

    public interface resourceListener{
        void resourceCallback(Resource resource);
    }

    protected static String TAG = "FirebaseDatabaseManager";
    protected String groupName;
    protected DatabaseReference dB;
    protected userObjectUpdateListener userObjUpdateListener;

    public FirebaseDatabaseManager(userObjectUpdateListener listener){
        userObjUpdateListener = listener;
    }

    //update groupname based on the group the user belongs to
    public void updateGroupName(String groupName){
        if( groupName != null ) {

            if ( (this.groupName != null && !this.groupName.equals(groupName)) || dB == null) {
                dB = FirebaseDatabase.getInstance().getReference(groupName);
            }
        } else {
            dB = null;
        }
        this.groupName = groupName;
        Log.d(TAG, "group name updated");
    }

    public void addUser(User user){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            Log.d(TAG," creating user with e-mail "+ user.email + " in group " + groupName);
            if(dB != null){
                dB.child("Users").child(currentUser.getUid()).setValue(user);
            }else{
                Log.d(TAG, "dB is null");
            }
        }
    }

    //might need to change this to addTask(Task task, User user)
    public void addTask(Task task){
        String taskID = null;
        if(task != null){
            Log.d(TAG," creating task with name "+ task.name + " in group " + groupName);
            if(dB != null){
                taskID = dB.push().getKey();
                task.tID = taskID;
                dB.child("Tasks").child(taskID).setValue(task);
            }else{
                Log.d(TAG, "dB is null");
            }
        }

        final String tID = taskID;

        if(tID != null){

            for (int i = 0; i< task.getPeopleAssigned().size(); i++){
                String email = task.getPeopleAssigned().get(i);

                getUser(email, new userObjectListener() {
                    @Override
                    public void adminUserCallback(User admin) {

                    }

                    @Override
                    public void userCallback(User user) {
                        ArrayList<String> tIDs = user.getTaskIDs();
                        if(tIDs == null){
                            tIDs = new ArrayList<String>();
                        }
                        tIDs.add(tID);
                        dB.child("Users").child(user.uID).child("taskIDs").setValue(tIDs);
                    }
                });
            }
            getCurrentUser(new userObjectListener() {
                @Override
                public void adminUserCallback(User admin) {

                }

                @Override
                public void userCallback(User user) {
                    userObjUpdateListener.updateUserObject(user);
                }
            });
        }

    }

    public void getAdminUser(final String groupName, final userObjectListener listener){

        final DatabaseReference dBref = FirebaseDatabase.getInstance().getReference(groupName);

        Query query = dBref.child("Users").orderByChild("admin").equalTo(true);

        query.addListenerForSingleValueEvent(
            new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        //Getting admin user object from snapshot
                        for (DataSnapshot adminSnapshot : dataSnapshot.getChildren()) {
                            User admin = adminSnapshot.getValue(User.class);
                            Log.d("Admin user object ", "email: " + admin.email);
                            if(admin.groupName.equals(groupName)){
                                listener.adminUserCallback(admin);
                                break;
                            }
                        }
                    }
                    else {
                        Log.d("Admin user object ", "doesn't exist");
                        listener.adminUserCallback(null);
                    }
                }
                @Override
                public void onCancelled(DatabaseError firebaseError) {
                    Log.d(TAG, "Admin query cancelled");
                }
            });
    }

    public void getUser(String groupName, String email, final userObjectListener listener){

        final DatabaseReference dBref = FirebaseDatabase.getInstance().getReference(groupName);

        Query query = dBref.child("Users").orderByChild("email").equalTo(email);

        query.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            //Getting user object from snapshot
                            for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                                User user = userSnapshot.getValue(User.class);
                                Log.d(" User object ", "email: " + user.email);
                                listener.userCallback(user);
                            }
                        }
                        else{
                            Log.d("User object:", "Doesn't exist yet");
                            listener.userCallback(null);
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError firebaseError) {
                        Log.d(TAG, "Regular user query cancelled");
                    }
                });
    }

    public void getUser(String email, final userObjectListener listener){
        if (dB == null) {
            Log.d(TAG, "dB is null!");
            //might need to make some changes
            return;
        }

        Query query = dB.child("Users").orderByChild("email").equalTo(email);

        query.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            //Getting user object from snapshot
                            for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                                User user = userSnapshot.getValue(User.class);
                                Log.d(" User object ", "email: " + user.email);
                                listener.userCallback(user);
                            }
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError firebaseError) {
                        Log.d(TAG, "Regular user query cancelled");
                    }
                });
    }

    public void getCurrentUser(final userObjectListener listener){
        if (dB == null) {
            Log.d(TAG, "dB is null!");
            //might need to make some changes
            return;
        }

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        dB.child("Users").child(currentUser.getUid()).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            //Getting user object from snapshot
                            User user = dataSnapshot.getValue(User.class);
                            Log.d(" User object ", "email: " +  user.email);
                            listener.userCallback(user);
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError firebaseError) {
                        Log.d(TAG, " Current user query cancelled");
                    }
                });
    }

    public void addResource(Resource resource){
        String resourceID = null;
        if(resource != null){
            Log.d(TAG," creating resource with name "+ resource.name + " in group " + groupName);
            if(dB != null){
                resourceID = dB.push().getKey();
                resource.rID = resourceID;
                dB.child("Resources").child(resourceID).setValue(resource);
            }else{
                Log.d(TAG, "dB is null");
            }
        }

        final String rID = resourceID;

        if(rID != null){

            for (int i = 0; i< resource.getPeopleAssigned().size(); i++){
                String email = resource.getPeopleAssigned().get(i);

                getUser(email, new userObjectListener() {
                    @Override
                    public void adminUserCallback(User admin) {

                    }

                    @Override
                    public void userCallback(User user) {
                        ArrayList<String> rIDs = user.getResourceIDs();
                        if(rIDs == null){
                            rIDs = new ArrayList<String>();
                        }
                        rIDs.add(rID);
                        dB.child("Users").child(user.uID).child("resourceIDs").setValue(rIDs);
                    }
                });
            }
            getCurrentUser(new userObjectListener() {
                @Override
                public void adminUserCallback(User admin) {

                }

                @Override
                public void userCallback(User user) {
                    userObjUpdateListener.updateUserObject(user);
                }
            });
        }

    }

    public void checkGroupNameInList(final String tempGroupName, final checkGroupNameInListListener listener){

        DatabaseReference dBref = FirebaseDatabase.getInstance().getReference("GroupNames");

        dBref.addListenerForSingleValueEvent(
        new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    //Getting GroupName from snapshot
                     String value = dataSnapshot.child(tempGroupName).getValue(String.class);
                    if(value != null){
                        Log.d(" groupName ", value);
                        if(value.equals(tempGroupName))         //This is not needed
                            listener.result(true);
                    }
                    else
                        listener.result(false);
                }
                else{
                    listener.result(false);
                }
            }
            @Override
            public void onCancelled(DatabaseError firebaseError) {
                Log.d(TAG, "GroupNamesInList check cancelled");
            }
        });
    }

    public void addtoGroupNamesList(final String newGroupName){

        final DatabaseReference dBref = FirebaseDatabase.getInstance().getReference("GroupNames");

        getGroupNamesList(new groupNameListListener() {
            @Override
            public void groupNameListCallback(HashMap<String, String> map) {
                map.put(newGroupName, newGroupName);
                dBref.setValue(map);
            }
        });
    }

    public void getGroupNamesList(final groupNameListListener listener){

        DatabaseReference dBref = FirebaseDatabase.getInstance().getReference("GroupNames");

        dBref.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        HashMap<String, String> map = new HashMap<>();
                        if(dataSnapshot.exists()){
                            //Getting GroupName List from snapshot
                            for (DataSnapshot groupNameSnapshot : dataSnapshot.getChildren()) {
                                //Getting the groupNames from snapshot
                                String value = groupNameSnapshot.getValue(String.class);
                                if(value != null){
                                    Log.d(" groupName ", value);
                                    map.put(value, value);
                                }

                            }
                        }
                        listener.groupNameListCallback(map);

                    }
                    @Override
                    public void onCancelled(DatabaseError firebaseError) {
                        Log.d(TAG, "GroupNamesList query cancelled");
                    }
                });

    }

    public void updateResource(Resource resource) {
        if (dB == null) {
            Log.d(TAG, "dB is null!");
            return;
        }

        if(resource != null){
            dB.child("Resources").child(resource.rID).setValue(resource);
        }

    }

    public void updateTask(Task task) {
        if (dB == null) {
            Log.d(TAG, "dB is null!");
            return;
        }

        if(task != null){
            dB.child("Tasks").child(task.tID).setValue(task);
        }

    }
    public void removeResource(final Resource resource) {
        if (dB == null) {
            Log.d(TAG, "dB is null!");
            return;
        }

        dB.child("Resources").child(resource.rID).removeValue();

        for (int i = 0; i< resource.getPeopleAssigned().size(); i++) {
            String email = resource.getPeopleAssigned().get(i);

            getUser(email, new userObjectListener() {
                @Override
                public void adminUserCallback(User admin) {

                }

                @Override
                public void userCallback(User user) {
                    ArrayList<String> rIDs = user.getResourceIDs();
                    ArrayList<String> newRIDs = new ArrayList<>();
                    for(int i = 0; i< rIDs.size(); i++){
                        if(!rIDs.get(i).equals(resource.rID))
                            newRIDs.add(rIDs.get(i));
                    }

                    dB.child("Users").child(user.uID).child("resourceIDs").setValue(newRIDs);
                }
            });
        }
        getCurrentUser(new userObjectListener() {
            @Override
            public void adminUserCallback(User admin) {

            }

            @Override
            public void userCallback(User user) {
                userObjUpdateListener.updateUserObject(user);
            }
        });
    }

    public void removeTask(final Task task) {
        if (dB == null) {
            Log.d(TAG, "dB is null!");
            return;
        }

        dB.child("Tasks").child(task.tID).removeValue();

        for (int i = 0; i< task.getPeopleAssigned().size(); i++){
            String email = task.getPeopleAssigned().get(i);

            getUser(email, new userObjectListener() {
                @Override
                public void adminUserCallback(User admin) {

                }

                @Override
                public void userCallback(User user) {
                    ArrayList<String> tIDs = user.getTaskIDs();
                    ArrayList<String> newTIDs = new ArrayList<>();
                    for(int i = 0; i< tIDs.size(); i++){
                        if(!tIDs.get(i).equals(task.tID))
                            newTIDs.add(tIDs.get(i));
                    }

                    dB.child("Users").child(user.uID).child("taskIDs").setValue(newTIDs);
                }
            });
        }
        getCurrentUser(new userObjectListener() {
            @Override
            public void adminUserCallback(User admin) {

            }

            @Override
            public void userCallback(User user) {
                userObjUpdateListener.updateUserObject(user);
            }
        });
    }

    public void updateUserObj(User user) {
        if (dB == null) {
            Log.d(TAG, "dB is null!");
            return;
        }

        if(user != null){
            dB.child("Users").child(user.uID).setValue(user);
        }

    }

    public void getTasksList(final User user, final taskListListener listener) {
        if (dB == null) {
            Log.d(TAG, "dB is null!");
            return;
        }

        /*
        taskList = new ArrayList<>();

        getCurrentUser(new userObjectListener() {
            @Override
            public void adminUserCallback(User admin) {

            }

            @Override
            public void userCallback(User user) {
                for(int i = 0; i< user.taskIDs.size(); i++){
                    getTask(user.taskIDs.get(i), new taskListener() {
                        @Override
                        public void taskCallback(Task task) {
                            if(task != null){
                                taskList.add(task);
                            }
                        }
                    });
                }
            }
        });

        listener.taskListCallback(taskList);
        */

        dB.child("Tasks").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    //Getting task objects from snapshot
                    ArrayList<Task> taskList = new ArrayList<>();
                    if(user.taskIDs != null) {
                        for (int i = 0; i < user.taskIDs.size(); i++) {
                            Task task = dataSnapshot.child(user.taskIDs.get(i)).getValue(Task.class);
                            taskList.add(task);
                        }
                    }
                    listener.taskListCallback(taskList);
                }
                else {
                    Log.d(TAG, "No tasks created");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "getTasksList call cancelled");
            }
        });

    }

    public void getResourcesList(final User user, final resourceListListener listener) {
        if (dB == null) {
            Log.d(TAG, "dB is null!");
            return;
        }

        //resourceList.clear();
        /*getCurrentUser(new userObjectListener() {
            @Override
            public void adminUserCallback(User admin) {

            }

            @Override
            public void userCallback(User user) {
                for(int i = 0; i< user.resourceIDs.size(); i++){
                    getResource(user.resourceIDs.get(i), new resourceListener() {
                        @Override
                        public void resourceCallback(Resource resource) {
                            if(resource != null){
                                resourceList.add(resource);
                            }
                        }
                    });
                }
            }
        });
        */

        dB.child("Resources").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    //Getting resource objects from snapshot
                    ArrayList<Resource> resourceList = new ArrayList<>();
                    if (user.resourceIDs != null) {
                        for (int i = 0; i < user.resourceIDs.size(); i++) {
                            Resource resource = dataSnapshot.child(user.resourceIDs.get(i)).getValue(Resource.class);
                            resourceList.add(resource);
                        }
                    }
                    listener.resourceListCallback(resourceList);
                }
                else {
                    Log.d(TAG, "No resources created");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "getResourcesList call cancelled");
            }
        });

    }

    public void getResource(String iD, final resourceListener listener) {

        if (dB == null) {
            Log.d(TAG, "dB is null!");
            return;
        }

        dB.child("Resources").child(iD).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    //Getting resource object from snapshot
                    Resource resource = dataSnapshot.getValue(Resource.class);
                    Log.d(" Resource object ", "name: " +  resource.name);
                    listener.resourceCallback(resource);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "getResource call cancelled");
            }
        });
    }

    public void getTask(String iD, final taskListener listener) {

        if (dB == null) {
            Log.d(TAG, "dB is null!");
            return;
        }

        dB.child("Tasks").child(iD).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    //Getting task object from snapshot
                    Task task = dataSnapshot.getValue(Task.class);
                    Log.d(" Task object ", "name: " +  task.name);
                    listener.taskCallback(task);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "getTask call cancelled");
            }
        });
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {


    }
}
