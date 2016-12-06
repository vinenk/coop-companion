package com.panjwani.ovais.coopcompanion;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class ResourceCreateActivity extends AppCompatActivity {

    private static final int REQUESTCODE = 333;
    private static ArrayList<String> checkedUsers;
    private static TextView checkedPeople;
    private static long date;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resource_create);

        final ArrayList<String> users = getIntent().getStringArrayListExtra("users");

        EditText resourceName = (EditText) findViewById(R.id.create_resource_name);
        checkedPeople = (TextView) findViewById(R.id.checked_people);
        Button addPeople = (Button) findViewById(R.id.add_people);
        CheckBox collection = (CheckBox) findViewById(R.id.collection);
        EditText description = (EditText) findViewById(R.id.description);

        addPeople.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UserCheckListActivity.class);
                intent.putExtra("users", users);
                startActivityForResult(intent, REQUESTCODE);
            }
        });

        Resource resource = new Resource(resourceName.getText().toString(), checkedUsers, collection.isChecked(), description.getText().toString());

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUESTCODE) {
            checkedUsers = new ArrayList<>(Arrays.asList(data.getStringArrayExtra("checkedUsers")));
            String checked = checkedUsers.toString();
            checked = checked.substring(1, checked.length()-1);
            checkedPeople.setText(checked);
        }
    }
}
