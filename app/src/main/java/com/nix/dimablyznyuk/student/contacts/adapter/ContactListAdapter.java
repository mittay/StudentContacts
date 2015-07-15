package com.nix.dimablyznyuk.student.contacts.adapter;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.nix.dimablyznyuk.student.contacts.GetImageTask;
import com.nix.dimablyznyuk.student.contacts.MainActivity;
import com.nix.dimablyznyuk.student.contacts.PrefActivity;
import com.nix.dimablyznyuk.student.contacts.R;
import com.nix.dimablyznyuk.student.contacts.model.Contact;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Dima Blyznyuk on 07.07.15.
 */

public class ContactListAdapter extends ArrayAdapter<Contact> implements Filterable {

    private List<Contact> list = new ArrayList<Contact>();
    private final Activity context;
    private List<Contact> checkedContacts = new ArrayList<Contact>();
    private boolean isCheckBoxVisible;
    private SharedPreferences sharedPref;
    private Drawable drawable;


    public ContactListAdapter(Activity context, List<Contact> list) {
        super(context, R.layout.list_view_item, list);
        this.context = context;
        this.list.addAll(list);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    public void addContactList(List<Contact> contacts) {
        list.clear();
        list.addAll(contacts);
    }

    public void addContact(Contact contact) {
        list.add(contact);
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

    public void removeChacked() {
        list.removeAll(checkedContacts);
    }

    static class ViewHolder {

        protected ImageView imageView;
        protected TextView text;
        protected CheckBox checkbox;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        if (convertView == null) {
            LayoutInflater inflator = context.getLayoutInflater();
            view = inflator.inflate(R.layout.list_view_item, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.imageView = (ImageView) view.findViewById(R.id.ivPhoto);
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
        try {
            holder.imageView.setImageDrawable(new GetImageTask(context)
                    .execute(list.get(position).getPhoto()).get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        holder.text.setText(list.get(position).getName());
        holder.text.setTextColor(getColor(list.get(position)));
        holder.checkbox.setChecked(list.get(position).getSelected());
        holder.checkbox.setVisibility(isCheckBoxVisible ? View.VISIBLE : View.INVISIBLE);
        return view;
    }

    private int getColor(Contact c) {

        sharedPref = PreferenceManager.getDefaultSharedPreferences(context);

        if (c.getGender().equals(MainActivity.MALE)) {
            return (Color.parseColor(sharedPref.getString(PrefActivity.PREF_KEY_MALE_COLOR,
                    PrefActivity.DEFAULT_COLOR)));
        } else {
            return (Color.parseColor(sharedPref.getString(PrefActivity.PREF_KEY_FEMALE_COLOR,
                    PrefActivity.DEFAULT_COLOR)));
        }
    }
}