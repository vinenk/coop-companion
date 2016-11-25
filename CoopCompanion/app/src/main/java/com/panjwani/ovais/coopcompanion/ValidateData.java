package com.panjwani.ovais.coopcompanion;


import android.content.res.Resources;

import java.util.ArrayList;

public class ValidateData {
    // Locally check login or account creation credentials
    // Empty string means all is ok
    static String validate(Resources resources, String firstName, String lastName, String email,
                           String password, String confirmPassword, String groupName) {
        boolean validationError = false;
        StringBuilder validationErrorMessage =
                new StringBuilder(resources.getString(R.string.error_intro));
        if (email.trim().length() == 0) {
            validationError = true;
            validationErrorMessage.append(resources.getString(R.string.error_blank_email));
        } else if (!email.endsWith("@gmail.com")) {
            validationError = true;
            validationErrorMessage.append("Please use a valid @gmail email address");
        }

        if (password.trim().length() == 0) {
            if (validationError) {
                validationErrorMessage.append(resources.getString(R.string.error_join));
            }
            validationError = true;
            validationErrorMessage.append(resources.getString(R.string.error_blank_password));
        } else if( password.trim().length() < 6 ) {
            if (validationError) {
                validationErrorMessage.append(resources.getString(R.string.error_join));
            }
            validationError = true;
            validationErrorMessage.append(resources.getString(R.string.error_password_too_short));
        }
        if (confirmPassword != null
                && !password.equals(confirmPassword)) {
            if (validationError) {
                validationErrorMessage.append(resources.getString(R.string.error_join));
            }
            validationError = true;
            validationErrorMessage.append(resources.getString(
                    R.string.error_mismatched_passwords));
        }
        if (firstName != null
                && firstName.trim().length() == 0) {
            if (validationError) {
                validationErrorMessage.append(resources.getString(R.string.error_join));
            }
            validationError = true;
            validationErrorMessage.append(resources.getString(R.string.error_blank_first_name));
        }

        if (lastName != null
                && lastName.trim().length() == 0) {
            if (validationError) {
                validationErrorMessage.append(resources.getString(R.string.error_join));
            }
            validationError = true;
            validationErrorMessage.append(resources.getString(R.string.error_blank_last_name));
        }

        if (groupName.trim().length() == 0) {
            if (validationError) {
                validationErrorMessage.append(resources.getString(R.string.error_join));
            }
            validationError = true;
            validationErrorMessage.append(resources.getString(R.string.error_blank_group_name));
        }

        String returnString = "";
        if (validationError) {
            returnString = validationErrorMessage.toString();
        }
        return returnString;
    }

    static String validate(Resources resources, ArrayList<String> emails) {
        boolean validationError = false;
        StringBuilder validationErrorMessage =
                new StringBuilder(resources.getString(R.string.error_intro));

        for (int i = 0; i< emails.size(); i++) {

            if (emails.get(i).trim().length() != 0 && !emails.get(i).endsWith("@gmail.com")) {
                validationError = true;
            }
        }


        String returnString = "";
        if (validationError) {
            validationErrorMessage.append("Please use a valid @gmail email address");
            returnString = validationErrorMessage.toString();
        }
        return returnString;

    }
}
