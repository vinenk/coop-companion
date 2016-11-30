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
import android.widget.Toast;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class CreateInvitations extends Fragment {


    public interface CreateInvitationsInterface {
        void invitationsFinish(ArrayList<String> members);
    }

    private CreateInvitations.CreateInvitationsInterface createInvitationsInterface;
    public static String TAG = "Invitations";
    protected Button inviteButton;
    protected String email1;
    protected String email2;
    protected String email3;
    protected String email4;
    protected String email5;

    public static CreateInvitations newInstance(){
        CreateInvitations createInvitations = new CreateInvitations();
        return createInvitations;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            createInvitationsInterface = (CreateInvitationsInterface) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement CreateInvitationsInterface ");
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_create_invitations, container, false);

        inviteButton = (Button) v.findViewById(R.id.inviteButton);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final View v = getView();

        final EditText emailView1 = (EditText) v.findViewById(R.id.email1);
        final EditText emailView2 = (EditText) v.findViewById(R.id.email2);
        final EditText emailView3 = (EditText) v.findViewById(R.id.email3);
        final EditText emailView4 = (EditText) v.findViewById(R.id.email4);
        final EditText emailView5 = (EditText) v.findViewById(R.id.email5);

        inviteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                email1 = emailView1.getText().toString().trim();
                email2 = emailView2.getText().toString().trim();
                email3 = emailView3.getText().toString().trim();
                email4 = emailView4.getText().toString().trim();
                email5 = emailView5.getText().toString().trim();

                ArrayList<String> emails = new ArrayList<>();
                emails.add(email1);
                emails.add(email2);
                emails.add(email3);
                emails.add(email4);
                emails.add(email5);

                //Validate Data
                String validateString = ValidateData.validate(getResources(), emails);
                // If there is a validation error, display the error
                if (validateString.length() > 0) {
                    Toast.makeText(getActivity(), validateString, Toast.LENGTH_LONG).show();
                    return;
                }

                createInvitationsInterface.invitationsFinish(emails);
            }
        });
    }
}
