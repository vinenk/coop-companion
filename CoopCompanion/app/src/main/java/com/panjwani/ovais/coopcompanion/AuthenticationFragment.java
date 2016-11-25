package com.panjwani.ovais.coopcompanion;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class AuthenticationFragment extends Fragment {

    public interface AuthenticationInterface {
        void startLogIn();
        void startSignUp();
    }

    protected Button logInButton;
    protected Button signUpButton;

    private AuthenticationInterface authenticationInterface;

    public static AuthenticationFragment newInstance(){
        AuthenticationFragment authenticationFragment = new AuthenticationFragment();
        return authenticationFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            authenticationInterface = (AuthenticationInterface) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement AuthenticationInterface ");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_authentication, container, false);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        View v = getView();

        logInButton = (Button) v.findViewById(R.id.loginButton);
        signUpButton = (Button) v.findViewById(R.id.signupButton);

        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authenticationInterface.startLogIn();
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authenticationInterface.startSignUp();
            }
        });

    }
}
