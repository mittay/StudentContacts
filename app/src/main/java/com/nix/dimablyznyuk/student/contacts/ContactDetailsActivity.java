package com.nix.dimablyznyuk.student.contacts;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.nix.dimablyznyuk.student.contacts.model.Contact;
import com.nix.dimablyznyuk.student.contacts.model.Manager;
import com.nix.dimablyznyuk.student.contacts.model.SQLiteContactManager;

import java.util.concurrent.ExecutionException;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Dima Blyznyuk on 06.07.15.
 */
public class ContactDetailsActivity extends AppCompatActivity {

    private Contact contact;
    private Manager manager;

    @Bind(R.id.tvShowName)
    TextView tvShowName;
    @Bind(R.id.tvShowAddress)
    TextView tvShowAddress;
    @Bind(R.id.tvShowPhoneNumber)
    TextView tvShowPhoneNumber;
    @Bind(R.id.tvShowGender)
    TextView tvShowGender;
    @Bind(R.id.ivDetailPhoto)
    ImageView ivDetailPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        ThemeUtils.onActivityCreateSetTheme(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        ButterKnife.bind(this);

        manager = new SQLiteContactManager(this);
        manager.open();


        int contactId = getIntent().getExtras().getInt(ContactEditAddActivity
                .EXTRA_CONTACT_ID);
        contact = manager.getContact(contactId);

        tvShowName.setText(contact.getName());
        tvShowAddress.setText(contact.getAddress());
        tvShowPhoneNumber.setText(contact.getPhoneNumber());
        tvShowGender.setText(contact.getGender());

        showPhoto();
    }

    private void showPhoto() {
        try {
            ivDetailPhoto.setImageDrawable(new GetImageTask(this).execute(contact.getPhoto()).get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
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
