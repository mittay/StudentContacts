package com.nix.dima_blyznyuk.student.contacts.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.nix.dima_blyznyuk.student.contacts.R;
import com.nix.dima_blyznyuk.student.contacts.model.Contact;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dima Blyznyuk on 07.07.15.
 */

public class ContactListAdapter extends ArrayAdapter<Contact> {

    private final List<Contact> list;
    private final Activity context;
    private List<Contact> checkedContacts = new ArrayList<Contact>();
    private boolean isCheckBoxVisible;

    public ContactListAdapter(Activity context, List<Contact> list) {
        super(context, R.layout.list_view_item, list);
        this.context = context;
        this.list = list;
    }

    static class ViewHolder {
        protected TextView text;
        protected CheckBox checkbox;
    }

    public void setVisibleCheckBox(boolean b) {
        isCheckBoxVisible = b;
    }

    public List<Contact> getCheckedContacts() {
        return checkedContacts;
    }

    public void clearCheckedContacts() {
        checkedContacts.clear();
        for (Contact c : list) {
            c.setSelected(false);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        if (convertView == null) {
            LayoutInflater inflator = context.getLayoutInflater();
            view = inflator.inflate(R.layout.list_view_item, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.text = (TextView) view.findViewById(R.id.tvContactName);
            viewHolder.checkbox = (CheckBox) view.findViewById(R.id.deleteCheckBox);
            viewHolder.checkbox
                    .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                        @Override
                        public void onCheckedChanged(CompoundButton buttonView,
                                                     boolean isChecked) {
                            Contact contact = (Contact) viewHolder.checkbox
                                    .getTag();
                            contact.setSelected(buttonView.isChecked());
                            if (buttonView.isChecked()) {
                                checkedContacts.add(contact);
                            } else {
                                checkedContacts.remove(contact);
                            }
                        }
                    });
            view.setTag(viewHolder);
            viewHolder.checkbox.setTag(list.get(position));
        } else {
            view = convertView;
            ((ViewHolder) view.getTag()).checkbox.setTag(list.get(position));
        }
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.text.setText(list.get(position).getName());
        holder.checkbox.setChecked(list.get(position).getSelected());
        holder.checkbox.setVisibility(isCheckBoxVisible ? View.VISIBLE : View.INVISIBLE);
        return view;
    }
}