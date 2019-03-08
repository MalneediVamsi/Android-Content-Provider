package com.vamsi.malneedi.retrievecontacts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ContactsAdapter extends ArrayAdapter<Contact> {

    private List<Contact> myBeanLists;
    private Context mContext;

    public ContactsAdapter(Context context, List<Contact> myBeanList) {
        super(context, R.layout.contacts_list_item, myBeanList);
        this.myBeanLists = myBeanList;
        this.mContext = context;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(this.getContext())
                    .inflate(R.layout.contacts_list_item, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.name = (TextView) convertView.findViewById(R.id.contactName);
            viewHolder.id = (TextView) convertView.findViewById(R.id.contactID);
            viewHolder.number = (TextView) convertView.findViewById(R.id.contactNumber);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Contact item = getItem(position);
        if (item != null) {
            viewHolder.name.setText(item.getContactName());
            viewHolder.id.setText(item.getContactID());
            viewHolder.number.setText(item.getContactNumber());
        }

        return convertView;
    }

    private class ViewHolder {
        private TextView name;
        private TextView id;
        private TextView number;
    }
}
