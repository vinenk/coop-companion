package com.panjwani.ovais.coopcompanion;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import java.util.ArrayList;

/**
 * Created by Mohammed on 12/1/2016.
 */

public class UserCheckListAdapter extends BaseAdapter {

    public interface UserCheckListener {
        void UserCheckCallback(String user, boolean isChecked);
    }

    private LayoutInflater inflater;
    private ArrayList<String> users;
    private UserCheckListener ucl;

    public UserCheckListAdapter(UserCheckListener ucl, Context context, ArrayList<String> users) {
        this.ucl = ucl;
        inflater = LayoutInflater.from(context);
        this.users = users;
    }

    @Override
    public int getCount() {
        if (users != null) {
            return users.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        if (users != null) {
            return users.get(position);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        if (users != null) {
            return users.get(position).hashCode();
        } else {
            return 0;
        }
    }

    public void bindView(final String user, View view, ViewGroup parent) {
        CheckBox userCheck = (CheckBox) view.findViewById(R.id.checkbox_user);
        userCheck.setText(user);
        userCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ucl.UserCheckCallback(user, isChecked);
            }
        });
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String user = (String) getItem(position);
        if (user == null) {
            throw new IllegalStateException("this should be called when users is not null");
        }
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.single_item_check_list, parent, false);
        }
        bindView(user, convertView, parent);
        return convertView;
    }
}
