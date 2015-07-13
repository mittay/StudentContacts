package com.nix.dimablyznyuk.student.contacts;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.nix.dimablyznyuk.student.contacts.adapter.ContactListAdapter;
import com.nix.dimablyznyuk.student.contacts.model.Contact;
import com.nix.dimablyznyuk.student.contacts.model.Manager;
import com.nix.dimablyznyuk.student.contacts.model.SQLiteContactManager;

import java.util.List;

/**
 * Created by Dima Blyznyuk on 06.07.15.
 */

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_EDIT = 1;
    public static final int REQUEST_CREATE = 2;

    public static final String EXTRA_ID = "id";

    public final static String TAG = "MainActivity";

    public static final String MALE = "Male";
    public static final String FEMALE = "Female";
    private ListView lvContacts;
    private Button btnCancel;
    private ContactListAdapter contactsAdapter;
    private SharedPreferences sherdPref;
    private Manager manager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sherdPref = PreferenceManager.getDefaultSharedPreferences(this);

        lvContacts = (ListView) findViewById(R.id.list_view);
        btnCancel = (Button) findViewById(R.id.btnCancel);

        btnCancel.setVisibility(View.GONE);

//        manager = ContactManager.getInstance();

        manager = new SQLiteContactManager(this);
        manager.open();

        contactsAdapter = new ContactListAdapter(this, manager.getContacts());
        lvContacts.setAdapter(contactsAdapter);

        lvContacts.setTextFilterEnabled(true);

        lvContacts.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                Contact contact = (Contact) parent.getItemAtPosition(position);
                Intent intent = new Intent(MainActivity.this, ContactEditAddActivity.class);
                intent.putExtra(ContactEditAddActivity.EXTRA_CONTACT_ID, contact.getId());
                startActivity(intent);
                return true;
            }
        });
        lvContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Log.i(TAG, "Count = " + parent.getCount() + "Adapter count = " + parent.getAdapter().getCount());
                Contact contact = (Contact) parent.getItemAtPosition(position);
                Intent intent = new Intent(MainActivity.this, ContactDetailsActivity.class);
                intent.putExtra(ContactEditAddActivity.EXTRA_CONTACT_ID, contact.getId());
                startActivityForResult(intent, REQUEST_EDIT);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "Main.onResume()");
        manager.open();
        updateListView();
    }

    public void updateListView() {
        Log.d(TAG, "Main.updateListView()");
        if (sherdPref.getString(PrefActivity.PREF_KEY_GENDER_FILTER,
                PrefActivity.DEFAULT_GENDER_FILTER)
                .equals(PrefActivity.VALUE_GENDER_MALE)) {
            contactsAdapter = new ContactListAdapter(this, manager.getContactsMale());
            lvContacts.setAdapter(contactsAdapter);
        } else if (sherdPref.getString(PrefActivity.PREF_KEY_GENDER_FILTER,
                PrefActivity.DEFAULT_GENDER_FILTER).equals(PrefActivity.VALUE_GENDER_FEMALE)) {
            contactsAdapter = new ContactListAdapter(this, manager.getContactsFemale());
            lvContacts.setAdapter(contactsAdapter);
        } else {
            contactsAdapter = new ContactListAdapter(this, manager.getContacts());
            lvContacts.setAdapter(contactsAdapter);
        }
    }

    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnNewContact:
                startActivityForResult(new Intent(this, ContactEditAddActivity.class),
                        REQUEST_CREATE);
                break;

            case R.id.btnDelete:
                if (contactsAdapter.getCheckedContacts().isEmpty()) {
                    btnCancel.setVisibility(View.VISIBLE);
                    contactsAdapter.setVisibleCheckBox(true);
                    contactsAdapter.notifyDataSetChanged();
                } else {
                    List<Contact> chackedContactList = contactsAdapter.getCheckedContacts();
                    manager.removeContacts(chackedContactList);
                    contactsAdapter.removeChacked();
                    contactsAdapter.notifyDataSetChanged();
                }
                break;

            case R.id.btnCancel:
                contactsAdapter.clearCheckedContacts();
                btnCancel.setVisibility(View.INVISIBLE);
                contactsAdapter.setVisibleCheckBox(false);
                contactsAdapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.settings:
                startActivity(new Intent(this, PrefActivity.class));
                return true;
            case R.id.menu_export:
                createExportDialog();
                return true;
            case R.id.menu_import:
                createImportDialog();
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        switch (requestCode) {
            case REQUEST_CREATE:
                if (resultCode == RESULT_OK) {
                    int id = data.getIntExtra(ContactEditAddActivity.EXTRA_CONTACT_ID, 0);
                    manager.open();
                    contactsAdapter.add(manager.getContact(id));
                }
                break;

            case REQUEST_EDIT:
                break;
        }
    }

    @Override
    protected void onPause() {
        manager.close();
        super.onPause();
    }

    private void createExportDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.export_contacts_to);
        builder.setPositiveButton(R.string.internal, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        new ContactXMLSerializer(MainActivity.this)
                                .writeContacts(manager.getContacts(), ContactXMLSerializer.INTERNAL);
                    }
                }
        );

        builder.setNegativeButton(R.string.external, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        new ContactXMLSerializer(MainActivity.this)
                                .writeContacts(manager.getContacts(), ContactXMLSerializer.EXTERNAL);

                    }
                }
        );
        AlertDialog dialog = builder.create();
        builder.show();
    }

    private void createImportDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.import_contacts_from);
        builder.setPositiveButton(R.string.internal, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        manager.addContacts(new ContactXMLSerializer(MainActivity.this)
                                .loadContacts(ContactXMLSerializer.INTERNAL));
                        updateListView();

                    }
                }
        );

        builder.setNegativeButton(R.string.external, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        manager.removeAllContacts();
                        manager.addContacts(new ContactXMLSerializer(MainActivity.this)
                                .loadContacts(ContactXMLSerializer.EXTERNAL));
                        updateListView();
                    }
                }
        );
        AlertDialog dialog = builder.create();
        builder.show();
    }
}
