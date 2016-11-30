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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.panjwani.ovais.coopcompanion.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpFragment extends Fragment {

    public interface SignUpInterface {
        void signUpFinish(User user);
    }

    private SignUpFragment.SignUpInterface signUpInterface;
    private FirebaseAuth mAuth;
    public static String TAG = "SignUp";
    protected Button signUpButton;
    protected String firstName;
    protected String lastName;
    protected String email;
    protected String password;
    protected String confirmPassword;
    protected String groupName;
    protected boolean residentAdmin;
    private FirebaseDatabaseManager fDManager;

    public static SignUpFragment newInstance(FirebaseDatabaseManager fDManager){
        SignUpFragment signUpFragment = new SignUpFragment();
        signUpFragment.setFDManager(fDManager);
        return signUpFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            signUpInterface = (SignUpInterface) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement SignUpInterface ");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_sign_up, container, false);

        signUpButton = (Button) v.findViewById(R.id.signupButton);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final View v = getView();

        final EditText firstNameView = (EditText) v.findViewById(R.id.firstName);
        final EditText lastNameView = (EditText) v.findViewById(R.id.lastName);
        final EditText emailView = (EditText) v.findViewById(R.id.email);
        final EditText passwordView = (EditText) v.findViewById(R.id.password);
        final EditText confirmPasswordView = (EditText) v.findViewById(R.id.confirmPassword);
        final EditText groupNameView = (EditText) v.findViewById(R.id.groupName);
        final CheckBox residentAdminView = (CheckBox) v.findViewById(R.id.residentAdminCheckbox);

        mAuth = FirebaseAuth.getInstance();

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firstName = firstNameView.getText().toString().trim();
                lastName = lastNameView.getText().toString().trim();
                email = emailView.getText().toString().trim();
                password = passwordView.getText().toString().trim();
                confirmPassword = confirmPasswordView.getText().toString().trim();
                groupName = groupNameView.getText().toString().trim();
                residentAdmin = residentAdminView.isChecked();

                //Validate Data
                String validateString = ValidateData.validate(getResources(),
                        firstName, lastName, email, password, confirmPassword, groupName);
                // If there is a validation error, display the error
                if (validateString.length() > 0) {
                    //Snackbar.make(v, validateString, Snackbar.LENGTH_LONG).show();
                    Toast.makeText(getActivity(), validateString, Toast.LENGTH_LONG).show();
                    return;
                }

                //Check if the groupName is already in use
                fDManager.checkGroupNameInList(groupName, new FirebaseDatabaseManager.checkGroupNameInListListener() {
                    @Override
                    public void result(boolean result) {
                        if(result){
                            String error = "Group Name " + groupName + " already exists, Please choose a different group name.";
                            Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();
                            return;
                        }
                    }
                });

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
                                    User user = new User();
                                    user.firstName = firstName;
                                    user.lastName = lastName;
                                    user.email = email;
                                    user.admin = true;
                                    user.residentAdmin = residentAdmin;
                                    user.groupName = groupName;
                                    user.groupMembers = new ArrayList<String>();
                                    user.groupMembersVariable = new ArrayList<String>();
                                    user.resourceIDs = new ArrayList<String>();
                                    user.uID = mAuth.getCurrentUser().getUid();
                                    signUpInterface.signUpFinish(user);
                                }
                            }
                        });

            }
        });

    }

    public void setFDManager(FirebaseDatabaseManager fDManager){
        this.fDManager = fDManager;
    }
}
