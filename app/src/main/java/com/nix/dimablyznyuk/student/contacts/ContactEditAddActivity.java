package com.nix.dimablyznyuk.student.contacts;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.nix.dimablyznyuk.student.contacts.model.Contact;
import com.nix.dimablyznyuk.student.contacts.model.Manager;
import com.nix.dimablyznyuk.student.contacts.model.SQLiteContactManager;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Dima Blyznyuk on 07.07.15.
 */
public class ContactEditAddActivity extends AppCompatActivity {

    public static final String EXTRA_CONTACT_ID = "com.nix.dimablyznyuk.student.contacts.id";

    @Bind(R.id.edtEditName)
    EditText edtName;
    @Bind(R.id.edtEditAddress)
    EditText edtAddress;
    @Bind(R.id.edtEditPhoneNumber)
    EditText edtPhoneNumber;
    @Bind(R.id.btnAddEditContact)
    Button btnAddEdit;
    @Bind(R.id.spEditGender)
    Spinner spGender;

    private Contact contact;
    private ArrayAdapter<CharSequence> spinnerAdapter;
    private Manager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edite_contact);
        ButterKnife.bind(this);

//        manager = ContactManager.getInstance();
        manager = new SQLiteContactManager(this);
        manager.open();

//        btnAddEdit = (Button) findViewById(R.id.btnAddEditContact);
//        edtName = (EditText) findViewById(R.id.edtEditName);
//        edtAddress = (EditText) findViewById(R.id.edtEditAddress);
//        edtPhoneNumber = (EditText) findViewById(R.id.edtEditPhoneNumber);
//        spGender = (Spinner) findViewById(R.id.spEditGender);

        Intent intent = getIntent();

        Bundle extras = intent.getExtras();

        if (extras != null) {
            int contactId = extras.getInt(EXTRA_CONTACT_ID);
            ;
            contact = manager.getContact(contactId);
            this.setTitle(R.string.edit_contact);
            btnAddEdit.setText(R.string.save);

        } else {
            this.setTitle(R.string.add_contact);
            btnAddEdit.setText(R.string.add_contact);
        }


        setupAdapter(R.array.gender_array);

        if (contact != null) {
            edtName.setText(contact.getName());
            edtAddress.setText(contact.getAddress());
            edtPhoneNumber.setText(contact.getPhoneNumber());
            spGender.setSelection(getPosition(contact));
        }
    }

    private int getPosition(Contact c) {
        return spinnerAdapter.getPosition(c.getGender());
    }

    private void setupAdapter(int stringArray) {
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

            if (contact == null) {
                int id = manager.createContact(name, address, phoneNumber, gender);
                Toast.makeText(this, R.string.contact_added, Toast.LENGTH_LONG).show();
                this.setResult(RESULT_OK, new Intent().putExtra(
                        ContactEditAddActivity.EXTRA_CONTACT_ID, id));
            } else {
                contact.setName(name);
                contact.setAddress(address);
                contact.setPhoneNumber(phoneNumber);
                contact.setGender(gender);

                manager.updateContact(contact);
                Toast.makeText(this, R.string.contact_changed, Toast.LENGTH_LONG).show();
                this.setResult(RESULT_OK);

            }
            finish();
        } else {
            Toast.makeText(this, R.string.please_fill_all_fields, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        manager.open();
    }

    @Override
    protected void onPause() {
        super.onPause();
        manager.close();
    }
}
