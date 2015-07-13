package com.nix.dimablyznyuk.student.contacts.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.nix.dimablyznyuk.student.contacts.MySQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dima Blyznyuk on 09.07.15.
 */
public class SQLiteContactManager implements Manager {


    public static final int COLUMN_ID = 0;
    public static final int COLUMN_NAME = 1;
    public static final int COLUMN_ADDRESS = 2;
    public static final int COLUMN_PHONE = 3;
    public static final int COLUMN_GENDER = 4;
    public final static String TAG = "SQLiteContactManager";

    private SQLiteDatabase database;
    private MySQLiteOpenHelper dbHelper;
    private String[] allColumns = {MySQLiteOpenHelper.COLUMN_ID,
            MySQLiteOpenHelper.COLUMN_NAME,
            MySQLiteOpenHelper.COLUMN_ADDRESS,
            MySQLiteOpenHelper.COLUMN_PHONE,
            MySQLiteOpenHelper.COLUMN_GENDER};

    public SQLiteContactManager(Context context) {
        dbHelper = new MySQLiteOpenHelper(context);
    }

    @Override
    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    @Override
    public void close() {
        dbHelper.close();
    }

    @Override
    public void addContacts(List<Contact> list) {
        for (Contact c : list) {
            createContact(c.getName(), c.getAddress(), c.getPhoneNumber(), c.getGender());
        }
    }

    @Override
    public int createContact(String name, String address, String phone, String gender) {
        Log.d(TAG, "addContact");

        ContentValues values = new ContentValues();

        values.put(MySQLiteOpenHelper.COLUMN_NAME, name);
        values.put(MySQLiteOpenHelper.COLUMN_ADDRESS, address);
        values.put(MySQLiteOpenHelper.COLUMN_PHONE, phone);
        values.put(MySQLiteOpenHelper.COLUMN_GENDER, gender);
        long insertId = database.insert(MySQLiteOpenHelper.TABLE_CONTACTS, null,
                values);
        Log.d(TAG, "addContact" + insertId);
        Cursor cursor = database.query(MySQLiteOpenHelper.TABLE_CONTACTS,
                allColumns, MySQLiteOpenHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Contact contact = cursorToContact(cursor);
        cursor.close();
        return contact.getId();
    }

    @Override
    public boolean isContactExist(Contact contact) {
        Log.d(TAG, "isContactExist" + contact.getId());
        Cursor cursor = database.query(MySQLiteOpenHelper.TABLE_CONTACTS,
                allColumns, COLUMN_ID + " = " + contact.getId(), null, null, null, null);
        if (cursor.getCount() <= 0) {
            return false;
        }
        return true;
    }

    @Override
    public List<Contact> getContacts() {
        Log.d(TAG, "getContacts");
        List<Contact> contacts = new ArrayList<Contact>();

        Cursor cursor = database.query(MySQLiteOpenHelper.TABLE_CONTACTS,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Contact contact = cursorToContact(cursor);
            contacts.add(contact);
            cursor.moveToNext();
        }
        cursor.close();
        return contacts;
    }

    private Contact cursorToContact(Cursor cursor) {
        Log.d(TAG, "cursorToContact");
        Contact contacts = new Contact();
        contacts.setId(cursor.getInt(COLUMN_ID));
        contacts.setName(cursor.getString(COLUMN_NAME));
        contacts.setAddress(cursor.getString(COLUMN_ADDRESS));
        contacts.setPhoneNumber(cursor.getString(COLUMN_PHONE));
        contacts.setGender(cursor.getString(COLUMN_GENDER));

        return contacts;
    }

    @Override
    public void removeContact(Contact contact) {
        Log.d(TAG, "cursorToContact" + contact.getId());
        int id = contact.getId();
        database.delete(MySQLiteOpenHelper.TABLE_CONTACTS, MySQLiteOpenHelper.COLUMN_ID
                + " = " + id, null);
    }

    public void removeAllContacts() {
        Log.d(TAG, "removeAllContacts");
        database.delete(MySQLiteOpenHelper.TABLE_CONTACTS, null, null);

    }

    @Override
    public Contact getContact(int id) {
        Log.d(TAG, "getContact" + id);
        Cursor cursor = database.query(MySQLiteOpenHelper.TABLE_CONTACTS,
                allColumns, MySQLiteOpenHelper.COLUMN_ID + " = " + id, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Contact contact = cursorToContact(cursor);
            return contact;
        }
        return null;
    }

    @Override
    public List<Contact> getContactsFemale() {
        List<Contact> contactsFemale = new ArrayList<Contact>();

        Cursor cursor = database.query(MySQLiteOpenHelper.TABLE_CONTACTS,
                allColumns, MySQLiteOpenHelper.COLUMN_GENDER + " = " + "'Female'", null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Contact contact = cursorToContact(cursor);
            contactsFemale.add(contact);
            cursor.moveToNext();
        }
        cursor.close();
        return contactsFemale;
    }

    @Override
    public List<Contact> getContactsMale() {
        Log.d(TAG, "getContactsMale");
        List<Contact> contactsMale = new ArrayList<Contact>();

        Cursor cursor = database.query(MySQLiteOpenHelper.TABLE_CONTACTS,
                allColumns, MySQLiteOpenHelper.COLUMN_GENDER + " = " + "'Male'", null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Contact contact = cursorToContact(cursor);
            contactsMale.add(contact);
            cursor.moveToNext();
        }
        cursor.close();
        return contactsMale;
    }

    @Override
    public void removeContacts(List<Contact> list) {
        Log.d(TAG, "removeContacts" + list.size());
        for (Contact c : list) {
            database.delete(MySQLiteOpenHelper.TABLE_CONTACTS, MySQLiteOpenHelper.COLUMN_ID
                    + " = " + c.getId(), null);
        }
    }

    @Override
    public void updateContact(Contact contact) {

        ContentValues values = new ContentValues();

        values.put(MySQLiteOpenHelper.COLUMN_NAME, contact.getName());
        values.put(MySQLiteOpenHelper.COLUMN_ADDRESS, contact.getAddress());
        values.put(MySQLiteOpenHelper.COLUMN_PHONE, contact.getPhoneNumber());
        values.put(MySQLiteOpenHelper.COLUMN_GENDER, contact.getGender());

        database.update(MySQLiteOpenHelper.TABLE_CONTACTS,
                values, MySQLiteOpenHelper.COLUMN_ID + " = " + contact.getId(), null);
    }


}


