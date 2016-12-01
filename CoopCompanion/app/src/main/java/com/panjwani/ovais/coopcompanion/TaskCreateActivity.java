package com.panjwani.ovais.coopcompanion;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class TaskCreateActivity extends AppCompatActivity {

    private static final int REQUESTCODE = 222;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_create);

        final ArrayList<String> users = getIntent().getStringArrayListExtra("users");

        EditText taskName = (EditText) findViewById(R.id.create_task_name);
        TextView checkedPeople = (TextView) findViewById(R.id.checked_people);
        Button addPeople = (Button) findViewById(R.id.add_people);
        TextView dueDate = (TextView) findViewById(R.id.due_date);
        Button addDueDate = (Button) findViewById(R.id.add_due_date);
        CheckBox repeatable = (CheckBox) findViewById(R.id.repeatable);
        Spinner repeatVar = (Spinner) findViewById(R.id.repeat_var);
        EditText description = (EditText) findViewById(R.id.description);

        addPeople.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UserCheckListActivity.class);
                intent.putExtra("users", users);
                startActivityForResult(intent, REQUESTCODE);
            }
        });
     }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUESTCODE) {
            String[] checkedUsers = data.getStringArrayExtra("checkedUsers");

        }
    }
}
