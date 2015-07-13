package com.nix.dima_blyznyuk.student.contacts;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.nix.dima_blyznyuk.student.contacts.model.Contact;
import com.nix.dima_blyznyuk.student.contacts.model.Contacts;

import java.io.Serializable;
import java.util.UUID;

/**
 * Created by Dima Blyznyuk on 07.07.15.
 */
public class ContactEditAddActivity extends AppCompatActivity {

    public static final String MALE = "Male";
    public static final String FEMALE = "Female";
    public static final String MALE_FEMALE = "1";
    public static final String FEMALE_MALE = "2";
    private EditText edtName;
    private EditText edtAddress;
    private EditText edtPhoneNumber;
    private Spinner spGender;
    private Contact contact;
    private SharedPreferences sp;
    private ArrayAdapter<CharSequence> spinnerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edite_contact);

        sp = PreferenceManager.getDefaultSharedPreferences(this);

        Intent intent = getIntent();

        Serializable extras = intent.getSerializableExtra(ContactDetailsActivity.EXTRA_CONTACT_ID);
        if (extras != null) {
            UUID contactId = (UUID) extras;
            contact = Contacts.getInstance().getContact(contactId);
            this.setTitle(R.string.edit_contact);
        } else {
            this.setTitle(R.string.add_contact);
        }
        Toast.makeText(this, "not nullll", Toast.LENGTH_LONG).show();
        edtName = (EditText) findViewById(R.id.edtEditName);
        edtAddress = (EditText) findViewById(R.id.edtEditAddress);
        edtPhoneNumber = (EditText) findViewById(R.id.edtEditPhoneNumber);
        spGender = (Spinner) findViewById(R.id.spEditGender);

        setupAdapter(R.array.gender_array);

        if (contact != null) {
            Toast.makeText(this, "not null", Toast.LENGTH_LONG).show();
            edtName.setText(contact.getName());
            edtAddress.setText(contact.getAddress());
            edtPhoneNumber.setText(contact.getPhoneNumber());
            spGender.setSelection(getPosition(contact));
        } else {
            Toast.makeText(this, "create", Toast.LENGTH_LONG).show();
            contact = new Contact();
            updateSpinner();
        }
    }

    private void updateSpinner() {

        if (sp.getString ("gender_priority", "1").equals(MALE_FEMALE)) {
            setupAdapter(R.array.gender_array);
        } else {
            setupAdapter(R.array.reverse_gender_array);
        }
    }

    private int getPosition(Contact c) {
        return spinnerAdapter.getPosition(c.getGender());
    }

    private void setupAdapter(int stringArray){
        spinnerAdapter = ArrayAdapter.createFromResource(this,
                stringArray, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spGender.setAdapter(spinnerAdapter);
    }

    public void onClick(View v) {

        String name = edtName.getText().toString();
        String address = edtAddress.getText().toString();
        String phoneNumber = edtPhoneNumber.getText().toString();
        String gender = spGender.getSelectedItem().toString();

        if ((name.length() != 0) && (address.length() != 0) && (phoneNumber.length() != 0)) {

            contact.setName(name);
            contact.setAddress(address);
            contact.setPhoneNumber(phoneNumber);
            contact.setGender(gender);

            boolean isContactAdded = Contacts.getInstance().addContact(contact);
            Toast.makeText(this, ""+isContactAdded, Toast.LENGTH_LONG).show();
            if (isContactAdded) {
                Toast.makeText(this, R.string.contact_added, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, R.string.contact_changed, Toast.LENGTH_LONG).show();
            }
            finish();
        } else {
            Toast.makeText(this, R.string.please_fill_all_fields, Toast.LENGTH_LONG).show();
        }
    }
}
