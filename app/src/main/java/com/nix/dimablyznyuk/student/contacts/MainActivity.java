package com.nix.dimablyznyuk.student.contacts;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
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
import android.widget.Toast;

import com.nix.dimablyznyuk.student.contacts.adapter.ContactListAdapter;
import com.nix.dimablyznyuk.student.contacts.model.Contact;
import com.nix.dimablyznyuk.student.contacts.model.Manager;
import com.nix.dimablyznyuk.student.contacts.model.SQLiteContactManager;

import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Dima Blyznyuk on 06.07.15.
 */

public class MainActivity extends AppCompatActivity {


    public static final int REQUEST_EDIT = 1;
    public static final int REQUEST_CREATE = 2;
    public static final int REQUEST_PREF = 3;

    public static final String TAG = "MainActivity";
    public static final int MALE = 0;
    public static final int FEMALE = 1;

    SharedPreferences.OnSharedPreferenceChangeListener listener;

    @Bind(R.id.list_view)
    ListView lvContacts;
    @Bind(R.id.btnCancel)
    Button btnCancel;

    private ContactListAdapter contactsAdapter;
    private Manager manager;
    private SharedPreferences prefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        ThemeUtils.onActivityCreateSetTheme(this);
        LocaleUtils.onActivityCreateSetLocale(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        btnCancel.setVisibility(View.GONE);

        manager = new SQLiteContactManager(this);
        manager.open();

        updateListView();

        lvContacts.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                Contact contact = (Contact) lvContacts.getItemAtPosition(position);
                Intent intent = new Intent(MainActivity.this, ContactEditAddActivity.class);
                intent.putExtra(ContactEditAddActivity.EXTRA_CONTACT_ID, contact.getId());
                startActivityForResult(intent, REQUEST_EDIT);
                return true;
            }
        });
        lvContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Contact contact = (Contact) lvContacts.getItemAtPosition(position);
                Intent intent = new Intent(MainActivity.this, ContactDetailsActivity.class);
                intent.putExtra(ContactEditAddActivity.EXTRA_CONTACT_ID, contact.getId());
                startActivityForResult(intent, REQUEST_EDIT);
            }
        });
        listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                if (key.equals(PrefActivity.PREF_KEY_BUTTON_THEME)) {
                    Log.d(TAG, "PREF_KEY_BUTTON_THEME");

                    updateButtonStyle();
                }
                if (key.equals(PrefActivity.PREF_KEY_GENDER_FILTER)) {
                    Log.d(TAG, "PREF_KEY_GENDER_FILTER");
                    manager.open();
                    updateListView();
                }
                if (key.equals(PrefActivity.PREF_KEY_FEMALE_COLOR)
                        || key.equals(PrefActivity.PREF_KEY_MALE_COLOR)) {
                    Log.d(TAG, "PREF_KEY_FEMALE_COLOR");
                    manager.open();
                    updateListView();
                }
                if (key.equals(PrefActivity.PREF_KEY_LOCALE)) {
                    Log.d(TAG, "PREF_KEY_LOCALE");
                    updateLocale();
                }
            }
        };
        prefs.registerOnSharedPreferenceChangeListener(listener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        manager.open();
    }

    public void updateListView() {
        Log.d(TAG, "Main.updateListView()");
        if (prefs.getString(PrefActivity.PREF_KEY_GENDER_FILTER,
                PrefActivity.DEFAULT_GENDER_FILTER)
                .equals(PrefActivity.VALUE_GENDER_MALE)) {
            contactsAdapter = new ContactListAdapter(this, manager.getContactsMale());
            lvContacts.setAdapter(contactsAdapter);
        } else if (prefs.getString(PrefActivity.PREF_KEY_GENDER_FILTER,
                PrefActivity.DEFAULT_GENDER_FILTER).equals(PrefActivity.VALUE_GENDER_FEMALE)) {
            contactsAdapter = new ContactListAdapter(this, manager.getContactsFemale());
            lvContacts.setAdapter(contactsAdapter);
        } else {
            contactsAdapter = new ContactListAdapter(this, manager.getContacts());
            lvContacts.setAdapter(contactsAdapter);
        }
    }

    public void updateButtonStyle() {
        Log.d(TAG, "Main.updateButtonStyle()");
        if (prefs.getString(PrefActivity.PREF_KEY_BUTTON_THEME,
                PrefActivity.VALUE_BUTTON_STANDART)
                .equals(PrefActivity.VALUE_BUTTON_LIGTH)) {
            Log.d(TAG, "Main.ThemeUtils.THEME_LIGTH");
            ThemeUtils.changeToTheme(this, ThemeUtils.THEME_LIGTH);

        } else if (prefs.getString(PrefActivity.PREF_KEY_BUTTON_THEME,
                PrefActivity.VALUE_BUTTON_STANDART)
                .equals(PrefActivity.VALUE_BUTTON_DARK)) {
            Log.d(TAG, "Main.ThemeUtils.THEME_DARK");
            ThemeUtils.changeToTheme(this, ThemeUtils.THEME_DARK);
        }
    }

    public void updateLocale() {
        Log.d(TAG, "Main.updateLocale()");
        if (prefs.getString(PrefActivity.PREF_KEY_LOCALE,
                PrefActivity.VALUE_LOCALE_DEFAULT)
                .equals(PrefActivity.VALUE_LOCALE_ENGLISH)) {
            LocaleUtils.changeLocaleTo(this, LocaleUtils.LOCALE_EN);

        } else if (prefs.getString(PrefActivity.PREF_KEY_LOCALE,
                PrefActivity.VALUE_LOCALE_DEFAULT)
                .equals(PrefActivity.VALUE_LOCALE_RUSSIAN)) {
            LocaleUtils.changeLocaleTo(this, LocaleUtils.LOCALE_RU);
        } else {
            LocaleUtils.changeLocaleTo(this, LocaleUtils.LOCALE_DEFAULT);
        }
    }

    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnNewContact:

                startActivityForResult(new Intent(this, ContactEditAddActivity.class),
                        REQUEST_CREATE);
                break;

            case R.id.btnDelete:
                if (contactsAdapter.getCheckedContacts().isEmpty() &&
                        (contactsAdapter.getCount() != 0)) {
                    btnCancel.setVisibility(View.VISIBLE);
                    contactsAdapter.setVisibleCheckBox(true);
                    contactsAdapter.notifyDataSetChanged();
                } else {
                    List<Contact> checkedContactList = contactsAdapter.getCheckedContacts();
                    manager.removeContacts(checkedContactList);
                    updateListView();
                }
                break;

            case R.id.btnCancel:
                contactsAdapter.clearCheckedContacts();
                btnCancel.setVisibility(View.GONE);
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
                    Log.d(TAG, "Main.REQUEST_EDIT RESULT_OK");
                    int id = data.getIntExtra(ContactEditAddActivity.EXTRA_CONTACT_ID, 0);
                    manager.open();
                    updateListView();
                }
                break;

            case REQUEST_EDIT:
                if (resultCode == RESULT_OK) {
                    Log.d(TAG, "Main.REQUEST_EDIT RESULT_OK");
                    manager.open();
                    updateListView();
                }
                break;

            case REQUEST_PREF:
                if (resultCode == RESULT_OK) {
                    Log.d(TAG, "Main.TREQUEST_PREF");
                    updateButtonStyle();
                }
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
                        new ExportContactsTask()
                                .execute(ContactXMLSerializer.INTERNAL);
                    }
                }
        );

        builder.setNegativeButton(R.string.external, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (isExternalStorageWritable()) {
                            new ExportContactsTask()
                                    .execute(ContactXMLSerializer.EXTERNAL);
                        } else {
                            Toast.makeText(MainActivity.this,
                                    getString(R.string.no_external_storage_available)
                                    , Toast.LENGTH_LONG).show();
                        }
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
                        new ImportContactsTask()
                                .execute(ContactXMLSerializer.INTERNAL);
                    }
                }
        );

        builder.setNegativeButton(R.string.external, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (isExternalStorageReadable()) {
                            new ImportContactsTask().execute(ContactXMLSerializer.EXTERNAL);

                        } else {
                            Toast.makeText(MainActivity.this,
                                    getString(R.string.no_external_storage_available)
                                    , Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );
        AlertDialog dialog = builder.create();
        builder.show();
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    private class ImportContactsTask extends AsyncTask<String, Void, List<Contact>> {

        protected List<Contact> doInBackground(String... location) {
            String loc = location[0];

            List<Contact> contactsList = new ContactXMLSerializer(MainActivity.this)
                    .loadContacts(loc);

            return contactsList;
        }

        protected void onPostExecute(List<Contact> result) {
            Toast.makeText(MainActivity.this, MainActivity.this.getString(R.string.imported)
                            + result.size()
                            + MainActivity.this.getString(R.string.contacts),
                    Toast.LENGTH_LONG).show();
            manager.removeAllContacts();
            manager.addContacts(result);
            updateListView();

        }
    }

    private class ExportContactsTask extends AsyncTask<String, Void, Integer> {

        protected Integer doInBackground(String... location) {
            String loc = location[0];
            List<Contact> listContact = manager.getContacts();
            new ContactXMLSerializer(MainActivity.this)
                    .writeContacts(listContact,
                            loc);
            return listContact.size();
        }

        protected void onPostExecute(Integer result) {
            Toast.makeText(MainActivity.this, getString(R.string.exported) + result
                            + MainActivity.this.getString(R.string.contacts),
                    Toast.LENGTH_LONG).show();
        }
    }
}
