package com.example.sudhaker_ftc_test.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.sudhaker_ftc_test.AddAppointmentActivity;
import com.example.sudhaker_ftc_test.R;

import java.util.ArrayList;

/**
 * Created by sudhaker on 1/7/2018.
 */


public class ContactAdapter extends BaseAdapter {

    private final ArrayList<String> data;
    private final ArrayList<String> selectedItemList;

    public ContactAdapter(ArrayList<String> list) {
        data = list;
        selectedItemList = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public String getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_contact_view, parent, false);
            holder.name = (TextView) convertView.findViewById(R.id.tv_Name);
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.cb_contact);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.name.setText(getItem(position));
        holder.checkBox.setTag(data.get(position));
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                String item = (String) compoundButton.getTag();
                if (b) {
                    if (selectedItemList.contains(item)) {
                        ///not needed to add
                    } else {
                        selectedItemList.add(item);
                        AddAppointmentActivity.contactListToCheckForUpdateCase.add(item);

                    }
                } else {
                    if (selectedItemList.contains(item)) {
                        selectedItemList.remove(item);
                        if(AddAppointmentActivity.contactListToCheckForUpdateCase.contains(item))
                        {
                            AddAppointmentActivity.contactListToCheckForUpdateCase.remove(item);
                        }

                    } else {
                        ///not needed to remove
                    }
                }

            }
        });
        ////Set Checkbox checked In editCase
        boolean isChecked = false;

        for (String s:AddAppointmentActivity.contactListToCheckForUpdateCase) {
            if (getItem(position)!=null && getItem(position).equals(s)) {
                isChecked = true;
                break;
            }
        }
        holder.checkBox.setChecked(isChecked);

        ///
        return convertView;
    }

    class ViewHolder {
        TextView name;
        CheckBox checkBox;
    }


    public ArrayList<String> getSelectedList() {
        return selectedItemList;
    }
}
