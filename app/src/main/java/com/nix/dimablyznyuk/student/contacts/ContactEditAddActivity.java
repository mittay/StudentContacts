package com.nix.dimablyznyuk.student.contacts;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nix.dimablyznyuk.student.contacts.model.Contact;
import com.nix.dimablyznyuk.student.contacts.model.Manager;
import com.nix.dimablyznyuk.student.contacts.model.SQLiteContactManager;

import java.io.File;
import java.text.ParseException;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Dima Blyznyuk on 07.07.15.
 */
public class ContactEditAddActivity extends AppCompatActivity {

    public static final String EXTRA_CONTACT_ID = "com.nix.dimablyznyuk.student.contacts.id";

    public static final int REQUEST_CODE_GALLERY = 1;
    public static final int REQUEST_CODE_CAMERA = 2;
    public static final int REQUEST_CODE_DATE = 3;

    public static final String DEFAULT_IMAGE = "default_image";
    public static final String PHOTO_FOLDER = "ContactsPhoto";
    public final static String TAG = "ContactEditAddActivity";
    private String imagePath = DEFAULT_IMAGE;
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
    @Bind(R.id.tvEditDateBirthday)
    TextView tvEditDateBirthday;
    @Bind(R.id.edtEditdateBirthday)
    EditText edtEditdateBirthday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        ThemeUtils.onActivityCreateSetTheme(this);
        LocaleUtils.onActivityCreateSetLocale(this);
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
            edtEditdateBirthday.setText((contact.getDateBirthday() != 0)
                    ? MyDateUtils.fromMilliseconds(contact.getDateBirthday()) : "");

            try {
                ivDetailPhoto.setImageBitmap(new GetImageTask(this).execute(imagePath).get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        edtEditdateBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(ContactEditAddActivity.this,
                        CalendarActivity.class), REQUEST_CODE_DATE);
            }
        });
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

        return spinnerAdapter.getPosition(c.getGender() == MainActivity.MALE ? getResources().getString(R.string.male) :
                getResources().getString(R.string.female));
    }

    private void setupAdapter(int stringArray) {
        spinnerAdapter = ArrayAdapter.createFromResource(this,
                stringArray, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spGender.setAdapter(spinnerAdapter);
    }

    public void onClick(View v) throws ParseException {

        String name = edtName.getText().toString();
        String address = edtAddress.getText().toString();
        String phoneNumber = edtPhoneNumber.getText().toString();
        int gender = spGender.getSelectedItemPosition();
        String date = edtEditdateBirthday.getText().toString();

        if ((name.length() != 0) && (address.length() != 0)
                && (phoneNumber.length() != 0)) {

            if (contact == null) {
                int id = manager.createContact(name, address, phoneNumber, gender,
                        imagePath, (date.length() == 0) ? 0 : MyDateUtils.toMilliseconds(date));
                Toast.makeText(this, R.string.contact_added, Toast.LENGTH_LONG).show();
                this.setResult(RESULT_OK, new Intent().putExtra(
                        ContactEditAddActivity.EXTRA_CONTACT_ID, id));
            } else {
                contact.setName(name);
                contact.setAddress(address);
                contact.setPhoneNumber(phoneNumber);
                contact.setGender(gender);
                contact.setPhoto(imagePath);
                contact.setDateBirthday((date.length() ==0) ? 0 : MyDateUtils.toMilliseconds(date));

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
                        startActivityForResult(photoPickerIntent, REQUEST_CODE_GALLERY);
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
        startActivityForResult(intent, REQUEST_CODE_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK) {
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
                    ivDetailPhoto.setImageBitmap(new GetImageTask(this)
                            .execute(imagePath).get());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }

        if (requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK) {
            imagePath = path;
            try {
                ivDetailPhoto.setImageBitmap(new GetImageTask(this).execute(imagePath).get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        if (requestCode == REQUEST_CODE_DATE && resultCode == RESULT_OK) {
            if (data == null) {
                return;
            }
            long date = data.getLongExtra(CalendarActivity.EXTRA_DATE, 0);
            Log.d(TAG, " " + date);
            edtEditdateBirthday.setText(MyDateUtils.fromMilliseconds(date));

        }
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        int orientation = this.getResources().getConfiguration().orientation;

        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            setContentView(R.layout.activity_edite_contact);
        } else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.activity_edite_contact);
        }

    }

}
