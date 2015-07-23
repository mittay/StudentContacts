package com.nix.dimablyznyuk.student.contacts;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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
    @Bind(R.id.tvDateBirthday)
    TextView tvDateBirthday;
    @Bind(R.id.tvDate)
    TextView tvDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        ThemeUtils.onActivityCreateSetTheme(this);
        LocaleUtils.onActivityCreateSetLocale(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        ButterKnife.bind(this);
        this.setTitle(R.string.contact_details);

        manager = new SQLiteContactManager(this);
        manager.open();

        int contactId = getIntent().getExtras().getInt(ContactEditAddActivity
                .EXTRA_CONTACT_ID);
        contact = manager.getContact(contactId);

        tvShowName.setText(contact.getName());
        tvShowAddress.setText(contact.getAddress());
        tvShowPhoneNumber.setText(contact.getPhoneNumber());
        tvShowGender.setText(contact.getGender() == MainActivity.MALE ? getResources()
                .getString(R.string.male) : getResources().getString(R.string.female));
        tvDate.setText((contact.getDateBirthday() != 0)
                ? MyDateUtils.fromMilliseconds(contact.getDateBirthday()) : "");
    }

    private void showPhoto() {
        try {
            ivDetailPhoto.setImageBitmap(new GetImageTask(this).execute(contact.getPhoto()).get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        manager.open();
        showPhoto();
    }

    @Override
    protected void onPause() {
        super.onPause();
        manager.close();
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        int orientation = this.getResources().getConfiguration().orientation;

        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            setContentView(R.layout.activity_contact);
        } else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.activity_contact);
        }

    }
    public void makeCall(View v){

        String uri = "tel:" + contact.getPhoneNumber().trim() ;
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse(uri));
        startActivity(intent);
    }
}
