package com.nix.dima_blyznyuk.student.contacts;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.nix.dima_blyznyuk.student.contacts.model.Contact;
import com.nix.dima_blyznyuk.student.contacts.model.Contacts;

import java.util.UUID;

/**
 * Created by Dima Blyznyuk on 06.07.15.
 */
public class ContactDetailsActivity extends AppCompatActivity {

    public static final String EXTRA_CONTACT_ID = "com.nix.dima_blyznyuk.student.contacts.contact_id";
    private TextView tvShowName;
    private TextView tvShowAddress;
    private TextView tvShowPhoneNumber;
    private TextView tvShowGender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        UUID contactId = (UUID) getIntent().getSerializableExtra(EXTRA_CONTACT_ID);
        Contact contact = Contacts.getInstance().getContact(contactId);

        tvShowName = (TextView) findViewById(R.id.tvShowName);
        tvShowAddress = (TextView) findViewById(R.id.tvShowAddress);
        tvShowPhoneNumber = (TextView) findViewById(R.id.tvShowPhoneNumber);
        tvShowGender = (TextView) findViewById(R.id.tvShowGender);

        tvShowName.setText(contact.getName());
        tvShowAddress.setText(contact.getAddress());
        tvShowPhoneNumber.setText(contact.getPhoneNumber());
        tvShowGender.setText(contact.getGender());
    }
}
