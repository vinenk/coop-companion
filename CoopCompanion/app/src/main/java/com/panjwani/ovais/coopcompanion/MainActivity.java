package com.panjwani.ovais.coopcompanion;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        AuthenticationFragment.AuthenticationInterface,
        SignUpFragment.SignUpInterface, LoginFragment.LoginInterface,
        Settings.SettingsInterface, CreateInvitations.CreateInvitationsInterface,
        FirebaseDatabaseManager.userObjectUpdateListener {


    private View navHeader;
    private TextView userName, userEmail;
    protected FirebaseAuth mAuth;
    protected FirebaseAuth.AuthStateListener mAuthListener;


    public static final String MYPREFERENCES = "myPrefs";
    public static final String USER = "User";
    public int navItemPos = 0;
    private User user;
    public static String TAG = "Coop Companion";
    public static Uri calUri;
    //private boolean signedIn;

    SharedPreferences prefs;
    SharedPreferences.Editor prefsEditor;
    FirebaseDatabaseManager fDManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        prefs = getSharedPreferences(MYPREFERENCES, MODE_PRIVATE);

        fDManager = new FirebaseDatabaseManager(this);

        initializeFirebase();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the home action
        } else if (id == R.id.nav_tasks) {

        } else if (id == R.id.nav_resources) {

        } else if (id == R.id.nav_settings) {
            Settings settings = new Settings();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragmentFrame, settings);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    protected void initializeFirebase() {
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    //signedIn = true;
                    User thisUser = getUserObject();
                    if(thisUser != null){
                        fDManager.updateGroupName(thisUser.groupName);
                        Log.d("userobj"," " + thisUser);
                    }
                    //load home fragment here
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    //signedIn = false;
                    fDManager.updateGroupName(null);
                    getSupportActionBar().hide();
                    AuthenticationFragment authenticationFragment = AuthenticationFragment.newInstance();
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.add(R.id.fragmentFrame, authenticationFragment);
                    //ft.addToBackStack(null);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    ft.commit();
                }

            }
        };

    }

    @Override
    public void startLogIn() {
        LoginFragment loginFragment = LoginFragment.newInstance(fDManager);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragmentFrame, loginFragment);
        ft.addToBackStack(null);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }

    @Override
    public void startSignUp() {
        SignUpFragment signUpFragment = SignUpFragment.newInstance(fDManager);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragmentFrame, signUpFragment);
        ft.addToBackStack(null);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }

    @Override
    public void logInFinish(User usr) {
        //show action bar and load home fragment(upcoming), store given user object in shared preferences
        //update group name in FirebaseDatabaseManger also
        getSupportActionBar().show();
        this.user = usr;
        saveUserObject(usr);
        fDManager.updateGroupName(usr.groupName);
        //databaseTest();

    }

    @Override
    public void signUpFinish(User user){
        //set user object and load createInvitations fragment
        this.user = user;
        //save in shared preferences
        saveUserObject(this.user);
        fDManager.addUser(this.user);
        fDManager.updateGroupName(user.groupName);
        getSupportFragmentManager().popBackStack();
        CreateInvitations createInvitationsFragment = CreateInvitations.newInstance();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragmentFrame, createInvitationsFragment);
        ft.addToBackStack(null);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }

    @Override
    public void logOut() {
        //clear sharedpreferences
        // log out user and load Authentication fragment, change groupname in fDManager to null
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            mAuth.signOut();
            fDManager.updateGroupName(null);
            /*
            getSupportActionBar().hide();
            AuthenticationFragment authenticationFragment = AuthenticationFragment.newInstance();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragmentFrame, authenticationFragment);
            //ft.addToBackStack(null);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.commit();
            */
        }
    }

    @Override
    public void invitationsFinish(ArrayList<String> members) {
        //set groupMembers list and the copy of the list, send invites, store User object in
        //shared preferences and firebase database, then load createCalenderfragment
        //once calender is created, show action bar and load home fragment(upcoming tasks and resources)
        /* TODO: You can only create a group of five right now,
            need to provide the option to add additional members
        */

        if(user != null) {

            for (int i = 0; i < members.size(); i++) {
                user.groupMembers.add(members.get(i));
                user.groupMembersVariable.add(members.get(i));
            }

            //Add the email address of Admin to the list of members if he is also a resident
            if(user.residentAdmin){
                user.groupMembers.add(user.email);
                user.groupMembersVariable.add(user.email);
            }

            saveUserObject(user);
            fDManager.updateUserObj(user);
        }

        SendEmail emailInvites = new SendEmail(user.groupName, members, this);
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
        if (this.user != null){
            saveUserObject(this.user);
        }

    }

    public User getUserObject(){
        Gson gson = new Gson();
        String json = prefs.getString(USER, "");
        this.user = gson.fromJson(json, User.class);
        return this.user;
    }

    public void saveUserObject(User usr){
        Gson gson = new Gson();
        String json = gson.toJson(usr);
        prefsEditor = prefs.edit();
        prefsEditor.putString(USER, json);
        prefsEditor.commit();
        Log.d("User object saved for ", usr.email);
    }

    @Override
    public void updateUserObject(User u) {
        this.user = u;
        saveUserObject(this.user);
    }

    public void databaseTest(){

        /*
        ArrayList<String> people = new ArrayList<>();
        people.add("vynds12@gmail.com");
        people.add("vynds@gmail.com");
        Resource resource = new Resource("TestResource2", people, true, "test2", Resource.SingleStatus.AVAILABLE, Resource.CollectionStatus.AVAILABLE);
        fDManager.addResource(resource);
        */

        /*
        fDManager.getResourcesList(new FirebaseDatabaseManager.resourceListListener() {
            @Override
            public void resourceListCallback(ArrayList<Resource> resources) {
                Log.d("ResourceListTest", " "+resources.size());
                for(int i = 0; i< resources.size(); i++){
                    Log.d("getresources", resources.get(i).getName());
                }
            }
        });
        */
    }
}
