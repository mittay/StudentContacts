package com.nix.dima_blyznyuk.student.contacts;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.nix.dima_blyznyuk.student.contacts.adapter.ContactListAdapter;
import com.nix.dima_blyznyuk.student.contacts.model.Contact;
import com.nix.dima_blyznyuk.student.contacts.model.Contacts;

import java.util.List;

/**
 * Created by Dima Blyznyuk on 06.07.15.
 */

public class MainActivity extends AppCompatActivity {

    private ListView lvContacts;
    private Button btnCancel;
    private ContactListAdapter contactsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvContacts = (ListView) findViewById(R.id.list_view);
        btnCancel = (Button) findViewById(R.id.btnCancel);

        btnCancel.setVisibility(View.GONE);

        contactsAdapter = new ContactListAdapter(this, Contacts.getInstance().getContacts());
        lvContacts.setAdapter(contactsAdapter);

        lvContacts.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                Contact contact = (Contact) lvContacts.getItemAtPosition(position);
                Intent intent = new Intent(MainActivity.this, ContactEditActivity.class);
                intent.putExtra(ContactDetailsActivity.EXTRA_CONTACT_ID, contact.getId());
                startActivity(intent);
                return true;
            }
        });
        lvContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Contact contact = (Contact) lvContacts.getItemAtPosition(position);
                Intent intent = new Intent(MainActivity.this, ContactDetailsActivity.class);
                intent.putExtra(ContactDetailsActivity.EXTRA_CONTACT_ID, contact.getId());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }

    public void updateUI() {
        contactsAdapter.notifyDataSetChanged();
    }

    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnNewContact:
                startActivity(new Intent(this, NewContactActivity.class));
                break;

            case R.id.btnDelete:
                if (contactsAdapter.getCheckedContacts().isEmpty()) {
                    btnCancel.setVisibility(View.VISIBLE);
                    contactsAdapter.setVisibleCheckBox(true);
                    updateUI();
                } else {
                    List<Contact> chackedContactList = contactsAdapter.getCheckedContacts();
                    Contacts.getInstance().getContacts().removeAll(chackedContactList);
                    updateUI();
                }

                break;

            case R.id.btnCancel:
                contactsAdapter.clearCheckedContacts();
                btnCancel.setVisibility(View.INVISIBLE);
                contactsAdapter.setVisibleCheckBox(false);
                updateUI();
                break;

        }
    }

}
