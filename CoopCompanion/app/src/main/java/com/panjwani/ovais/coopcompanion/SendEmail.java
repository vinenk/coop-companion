package com.panjwani.ovais.coopcompanion;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.sendgrid.SendGrid;
import com.sendgrid.SendGridException;

import java.util.ArrayList;

public class SendEmail {

    protected ArrayList<String> emailAddresses;
    protected String groupName;
    protected Context context;

    private static final String SENDGRID_USERNAME = "coopcompanion";
    private static final String SENDGRID_PASSWORD = "dAd*Ep$Ab5aZ";

    public SendEmail(String gName, ArrayList<String> emailsList, Context context){
        this.emailAddresses = emailsList;
        this.groupName = gName;
        this.context = context;

        String from = "noreply@coop-companion.firebaseapp.com";
        String subject = "Invitation To Join Group: " + groupName;
        String message = "Hello, \n You have been invited to join " + groupName +".\n To join, " +
                "Please install the Coop Companion app from Google Play store and Log In " +
                "using this email address and " + "the group name: "+ groupName +"." +
                "\n\n Thank You, \n The Coop Companion Team.";

        for(int i = 0; i< emailAddresses.size(); i++){
            String to = emailAddresses.get(i);
            SendEmailASyncTask task = new SendEmailASyncTask(context, to, from, subject, message);
            task.execute();
        }
    }


    /**
     * ASyncTask that composes and sends email
     */
    private static class SendEmailASyncTask extends AsyncTask<Void, Void, Void> {

        private Context appContext;
        private String msgResponse;

        private String to;
        private String from;
        private String subject;
        private String text;

        public SendEmailASyncTask(Context context, String to, String from, String subject,
                                  String text) {
            this.appContext = context.getApplicationContext();
            this.to = to;
            this.from = from;
            this.subject = subject;
            this.text = text;
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                SendGrid sendgrid = new SendGrid(SENDGRID_USERNAME, SENDGRID_PASSWORD);

                SendGrid.Email email = new SendGrid.Email();

                email.addTo(to);
                email.setFrom(from);
                email.setSubject(subject);
                email.setText(text);

                // Send email
                SendGrid.Response response = sendgrid.send(email);
                msgResponse = response.getMessage();

                Log.d("Email sent successfully", msgResponse);

            } catch (SendGridException e) {
                Log.e("Error sending email", e.toString());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            //Toast.makeText(appContext, msgResponse, Toast.LENGTH_SHORT).show();
        }
    }
}
