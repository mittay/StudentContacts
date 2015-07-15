package com.nix.dimablyznyuk.student.contacts;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.nix.dimablyznyuk.student.contacts.model.Contact;
import com.nix.dimablyznyuk.student.contacts.model.Manager;
import com.nix.dimablyznyuk.student.contacts.model.SQLiteContactManager;

import java.io.File;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Dima Blyznyuk on 07.07.15.
 */
public class ContactEditAddActivity extends AppCompatActivity {

    public static final String EXTRA_CONTACT_ID = "com.nix.dimablyznyuk.student.contacts.id";
    public static final int EXTRA_PHOTO = 1;
    public static final String PHOTO_FOLDER = "ContactsPhoto";
    public final static String TAG = "ContactEditAddActivity";
    private String imagePath;
    private Contact contact;
    private ArrayAdapter<CharSequence> spinnerAdapter;
    private Manager manager;
    private String path;


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
    @Bind(R.id.ivEditPhoto)
    ImageView ivDetailPhoto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        ThemeUtils.onActivityCreateSetTheme(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edite_contact);
        ButterKnife.bind(this);

        manager = new SQLiteContactManager(this);
        manager.open();

        Intent intent = getIntent();

        Bundle extras = intent.getExtras();

        if (extras != null) {
            int contactId = extras.getInt(EXTRA_CONTACT_ID);
            contact = manager.getContact(contactId);
            this.setTitle(R.string.edit_contact);
            btnAddEdit.setText(R.string.save);

        } else {
            this.setTitle(R.string.add_contact);
            btnAddEdit.setText(R.string.add_contact);
        }
        ivDetailPhoto.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showChooseDialog();

            }
        });

        setupAdapter(R.array.gender_array);

        if (contact != null) {
            edtName.setText(contact.getName());
            edtAddress.setText(contact.getAddress());
            edtPhoneNumber.setText(contact.getPhoneNumber());
            spGender.setSelection(getPosition(contact));
            imagePath = contact.getPhoto();
            try {
                ivDetailPhoto.setImageDrawable(new GetImageTask(this).execute(imagePath).get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
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
                int id = manager.createContact(name, address, phoneNumber, gender, imagePath);
                Toast.makeText(this, R.string.contact_added, Toast.LENGTH_LONG).show();
                this.setResult(RESULT_OK, new Intent().putExtra(
                        ContactEditAddActivity.EXTRA_CONTACT_ID, id));
            } else {
                contact.setName(name);
                contact.setAddress(address);
                contact.setPhoneNumber(phoneNumber);
                contact.setGender(gender);
                contact.setPhoto(imagePath);

                manager.updateContact(contact);
                Toast.makeText(this, R.string.contact_changed, Toast.LENGTH_LONG).show();
                this.setResult(RESULT_OK);

            }
            finish();
        } else {
            Toast.makeText(this, R.string.please_fill_all_fields, Toast.LENGTH_LONG).show();
        }
    }

    private void showChooseDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.select_photo_from));
        builder.setPositiveButton(getString(R.string.Camera), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        takePhoto();
                    }
                }
        );

        builder.setNeutralButton(getString(R.string.gallery), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                        photoPickerIntent.setType("image/*");
                        startActivityForResult(photoPickerIntent, EXTRA_PHOTO);
                    }
                }
        );
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                }
        );
        AlertDialog dialog = builder.create();
        builder.show();
    }

    public void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File folder = new File(Environment.getExternalStorageDirectory() + "/" + PHOTO_FOLDER);
        if (!folder.exists()) {
            folder.mkdir();
        }
        final Calendar c = Calendar.getInstance();
        String newDate = c.get(Calendar.DAY_OF_MONTH) + "_" + ((c.get(Calendar.MONTH)) + 1)
                + "_" + c.get(Calendar.YEAR) + "_" + c.get(Calendar.HOUR)
                + "_" + c.get(Calendar.MINUTE) + "_" + c.get(Calendar.SECOND);
        path = String.format(Environment.getExternalStorageDirectory()
                + "/" + PHOTO_FOLDER + "/" + "%s.png", newDate);
        File photo = new File(path);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));
        startActivityForResult(intent, 2);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            Uri photoUri = data.getData();
            if (photoUri != null) {
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(photoUri, filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String filePath = cursor.getString(columnIndex);
                cursor.close();
                imagePath = filePath;

                try {
                    ivDetailPhoto.setImageDrawable(new GetImageTask(this)
                            .execute(imagePath).get());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }

        if (requestCode == 2 && resultCode == RESULT_OK) {
            imagePath = path;
            try {
                ivDetailPhoto.setImageDrawable(new GetImageTask(this).execute(imagePath).get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }
}
