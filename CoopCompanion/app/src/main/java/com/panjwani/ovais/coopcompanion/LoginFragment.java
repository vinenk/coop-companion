package com.panjwani.ovais.coopcompanion;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    public interface LoginInterface {
        void logInFinish(User user);
    }

    private LoginFragment.LoginInterface loginInterface;
    private FirebaseAuth mAuth;
    public static String TAG = "LogIn";
    protected Button logInButton;
    protected String email;
    protected String password;
    protected String groupName;
    private FirebaseDatabaseManager fDManager;

    public static LoginFragment newInstance(FirebaseDatabaseManager fDManager){
        LoginFragment loginFragment= new LoginFragment();
        loginFragment.setFDManager(fDManager);
        return loginFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            loginInterface = (LoginInterface) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement LoginInterface ");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        logInButton = (Button) v.findViewById(R.id.loginButton);

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final View v = getView();

        final EditText emailView = (EditText) v.findViewById(R.id.email);
        final EditText passwordView = (EditText) v.findViewById(R.id.password);
        final EditText groupNameView = (EditText) v.findViewById(R.id.groupName);
        final EditText confirmPasswordView = (EditText) v.findViewById(R.id.confirmPassword);

        mAuth = FirebaseAuth.getInstance();

        //check groupname in database
        //if user is a member of the group proceed with login (check under admin user object)
        //prompt user to create a password and create account, create a user object for the
        //if there isn't one already.This would be a regular user if there isn't a user object
        // already in database. Pass user object to callback

        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = emailView.getText().toString().trim();
                final String password = passwordView.getText().toString().trim();
                final String groupName = groupNameView.getText().toString().trim();

                //Validate Data
                String validateString = ValidateData.validate(getResources(),
                        null, null, email, password, null, groupName);
                // If there is a validation error, display the error
                if (validateString.length() > 0) {
                    //Snackbar.make(v, validateString, Snackbar.LENGTH_LONG).show();
                    Toast.makeText(getActivity(), validateString, Toast.LENGTH_LONG).show();
                    return;
                }

                fDManager.getAdminUser(groupName, new FirebaseDatabaseManager.userObjectListener() {
                    @Override
                    public void adminUserCallback(User admin) {
                        if(admin == null){
                            String error = "This group doesn't exist. Please Sign Up and create the group if you are the admin";
                            Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();
                            return;
                        }
                        ArrayList<String> members = admin.getGroupMembers();
                        boolean isMember = false;
                        if(admin.email.equals(email)){
                            isMember = true;
                        }
                        else {
                            for (int i = 0; i< members.size(); i++){
                                if(members.get(i).equals(email)){
                                    isMember = true;
                                    break;
                                }
                            }
                        }

                        if(isMember){
                            fDManager.getUser(groupName, email, new FirebaseDatabaseManager.userObjectListener() {
                                @Override
                                public void adminUserCallback(User admin) {

                                }

                                @Override
                                public void userCallback(final User user) {
                                    if(user != null){
                                        //login with email and password

                                        // Set up a progress dialog
                                        final ProgressDialog dlg = new ProgressDialog(getActivity());
                                        dlg.setTitle("Please wait.");
                                        dlg.setMessage("Logging in.  Please wait.");
                                        dlg.show();

                                        // Log In
                                        mAuth.signInWithEmailAndPassword(email, password)
                                                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                                        dlg.dismiss();
                                                        Log.d(TAG, "LogInWithEmail:onComplete:" + task.isSuccessful());

                                                        // If sign in fails, display a message to the user. If sign in succeeds
                                                        // the auth state listener will be notified and logic to handle the
                                                        // signed in user can be handled in the listener.
                                                        if (!task.isSuccessful()) {
                                                            Log.w(TAG, "LogInWithEmail " + task.getException().getMessage());
                                                            Snackbar.make(v,
                                                                    "Authentication failed: " + task.getException().getMessage(),
                                                                    Snackbar.LENGTH_LONG)
                                                                    .setAction("Action", null).show();
                                                        } else {
                                                            loginInterface.logInFinish(user);
                                                        }
                                                    }
                                                });
                                    }
                                    else{
                                        //create new account with email and password
                                        // Set up a progress dialog
                                        final ProgressDialog dialog = new ProgressDialog(getActivity());
                                        dialog.setTitle("Please wait.");
                                        dialog.setMessage("Signing up.  Please wait.");
                                        dialog.show();
                                        // Set up a new Firebase user
                                        mAuth.createUserWithEmailAndPassword(email, password)
                                                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                                                        dialog.dismiss();
                                                        // If sign in fails, display a message to the user. If sign in succeeds
                                                        // the auth state listener will be notified and logic to handle the
                                                        // signed in user can be handled in the listener.
                                                        if (!task.isSuccessful()) {
                                                            // Show the error message
                                                            Exception e = task.getException();
                                                            if (e != null) Log.d(TAG, e.getMessage());
                                                            Snackbar.make(v, "Authentication failed", Snackbar.LENGTH_LONG)
                                                                    .setAction("Action", null).show();
                                                        } else {
                                                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                                    .setDisplayName(email).build();
                                                            mAuth.getCurrentUser().updateProfile(profileUpdates);
                                                            User usr = new User();
                                                            usr.firstName = null;
                                                            usr.lastName = null;
                                                            usr.email = email;
                                                            usr.admin = false;
                                                            usr.residentAdmin = false;
                                                            usr.groupName = groupName;
                                                            usr.groupMembers = new ArrayList<String>();
                                                            usr.groupMembersVariable = new ArrayList<String>();
                                                            usr.resourceIDs = new ArrayList<String>();
                                                            usr.taskIDs = new ArrayList<String>();
                                                            usr.uID = mAuth.getCurrentUser().getUid();
                                                            fDManager.addUser(usr);
                                                            loginInterface.logInFinish(usr);
                                                        }
                                                    }
                                                });
                                    }
                                }
                            });
                        }
                        else {
                            String error = "You are not a member of this group. Please contact admin to join the group";
                            Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();
                            return;
                        }
                    }

                    @Override
                    public void userCallback(User user) {

                    }
                });

            }
        });
    }

    public void setFDManager(FirebaseDatabaseManager fDManager){
        this.fDManager = fDManager;
    }

}
