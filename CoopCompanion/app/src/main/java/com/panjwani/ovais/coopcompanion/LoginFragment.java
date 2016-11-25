package com.panjwani.ovais.coopcompanion;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;


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

    public static LoginFragment newInstance(){
        LoginFragment loginFragment= new LoginFragment();
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

        mAuth = FirebaseAuth.getInstance();

        //check groupname in database
        //if user is a member of the group proceed with login (check under admin user object)
        //prompt user to create a password and create account, create a user object for the
        //if there isn't one already.This would be a regular user if there isn't a user object
        // already in database. Pass user object to callback
    }
}
