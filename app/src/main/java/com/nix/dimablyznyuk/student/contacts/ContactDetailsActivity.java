package com.nix.dimablyznyuk.student.contacts;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.nix.dimablyznyuk.student.contacts.model.Contact;
import com.nix.dimablyznyuk.student.contacts.model.Manager;
import com.nix.dimablyznyuk.student.contacts.model.SQLiteContactManager;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Dima Blyznyuk on 06.07.15.
 */
public class ContactDetailsActivity extends AppCompatActivity {

    @Bind(R.id.tvShowName)
    TextView tvShowName;
    @Bind(R.id.tvShowAddress)
    TextView tvShowAddress;
    @Bind(R.id.tvShowPhoneNumber)
    TextView tvShowPhoneNumber;
    @Bind(R.id.tvShowGender)
    TextView tvShowGender;

    private Contact contact;
    private Manager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        ButterKnife.bind(this);

//        manager = ContactManager.getInstance();
        manager = new SQLiteContactManager(this);
        manager.open();


        int contactId = getIntent().getExtras().getInt(ContactEditAddActivity
                .EXTRA_CONTACT_ID);
        contact = manager.getContact(contactId);

//        tvShowName = (TextView) findViewById(R.id.tvShowName);
//        tvShowAddress = (TextView) findViewById(R.id.tvShowAddress);
//        tvShowPhoneNumber = (TextView) findViewById(R.id.tvShowPhoneNumber);
//        tvShowGender = (TextView) findViewById(R.id.tvShowGender);

        tvShowName.setText(contact.getName());
        tvShowAddress.setText(contact.getAddress());
        tvShowPhoneNumber.setText(contact.getPhoneNumber());
        tvShowGender.setText(contact.getGender());
    }
    @Override
    protected void onResume() {
        manager.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        manager.close();
        super.onPause();
    }
}
