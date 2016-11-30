package com.panjwani.ovais.coopcompanion;

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
import java.util.Map;

public class FirebaseDatabaseManager {

    public interface userObjectListener{
        void adminUserCallback(User admin);
        void userCallback(User user);
    }

    public interface checkGroupNameInListListener{
        void result(boolean result);
    }

    protected static String TAG = "FirebaseDatabaseManager";
    protected String groupName;
    protected DatabaseReference dB;

    //update groupname based on the group the user belongs to
    public void updateGroupName(String groupName){
        if( groupName != null ) {

            if (!this.groupName.equals(groupName) || dB == null) {
                dB = FirebaseDatabase.getInstance().getReference(groupName);
            }
        } else {
            dB = null;
        }
        this.groupName = groupName;

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

    public void addTask(Task task){
        if(task != null){
            Log.d(TAG," creating task with name "+ task.name + " in group " + groupName);
            if(dB != null){
                dB.child("Tasks").child(dB.push().getKey()).setValue(task);
            }else{
                Log.d(TAG, "dB is null");
            }
        }
    }

    public void getAdminUser(final userObjectListener listener){
        if (dB == null) {
            Log.d(TAG, "dB is null!");
            //might need to make some changes
            return;
        }

        Query query = dB.child("Users").orderByChild("admin").equalTo(true);

        query.addListenerForSingleValueEvent(
            new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        //Getting admin user object from snapshot
                        User admin = dataSnapshot.getValue(User.class);
                        Log.d("Admin user object ", "email: " +  admin.email);
                        listener.adminUserCallback(admin);
                    }
                }
                @Override
                public void onCancelled(DatabaseError firebaseError) {
                    Log.d(TAG, "Admin query cancelled");
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
                            User user = dataSnapshot.getValue(User.class);
                            Log.d(" User object ", "email: " +  user.email);
                            listener.userCallback(user);
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError firebaseError) {
                        Log.d(TAG, "Regular user query cancelled");
                    }
                });
    }

    public void getUser(final userObjectListener listener){
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
                        Log.d(TAG, " user query cancelled");
                    }
                });
    }

    public void addResource(Resource resource){
        String resourceID = null;
        if(resource != null){
            Log.d(TAG," creating resource with name "+ resource.name + " in group " + groupName);
            if(dB != null){
                resourceID = dB.push().getKey();
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
                        rIDs.add(rID);
                        dB.child("Users").child(user.uID).child("resourceIDs").setValue(rIDs);
                    }
                });
            }
        }


    }

    public void checkGroupNameInList(final String tempGroupName, final checkGroupNameInListListener listener){
        if (dB == null) {
            Log.d(TAG, "dB is null!");
            return;
        }

        dB.child("GroupNames").addListenerForSingleValueEvent(
        new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    //Getting GroupName from snapshot
                     String value = dataSnapshot.child(tempGroupName).getValue(String.class);
                    if(value != null){
                        Log.d(" groupName ", value);
                        if(value.equals(tempGroupName))
                            listener.result(true);
                        else
                            listener.result(false);
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError firebaseError) {
                Log.d(TAG, "GroupNamesInList check cancelled");
            }
        });
    }

    public void addtoGroupNamesList(String newGroupName){

    }

    public void getGroupNamesList(){

    }

    public void updateResource() {

    }

    public void updateTask() {

    }
    public void removeResource(){

    }

    public void removeTask(){

    }












}
