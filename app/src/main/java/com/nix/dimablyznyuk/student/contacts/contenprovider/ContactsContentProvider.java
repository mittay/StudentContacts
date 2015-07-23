package com.nix.dimablyznyuk.student.contacts.contenprovider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.nix.dimablyznyuk.student.contacts.MySQLiteOpenHelper;

/**
 * Created by Dima Blyznyuk on 22.07.15.
 */
public class ContactsContentProvider extends ContentProvider {

    String TAG = "ContactsContentProvider";

    private MySQLiteOpenHelper dbHelper;
    private SQLiteDatabase db;
    private static final int DAY_SEC = 86400;

    public static final int URI_CONTACTS_NAME_TODAY_BIRTH = 1;
    private static final String AUTHORITY = "com.nix.dimablyznyuk.student.contacts.contenprovider";
    private static final String CONTACT_PATH = "contacts";
    public static final Uri CONTACT_CONTENT_URI = Uri.parse("content://"
            + AUTHORITY + "/" + CONTACT_PATH);
    static final String CONTACT_CONTENT_TYPE = "vnd.android.cursor.dir/vnd."
            + AUTHORITY + "." + CONTACT_PATH;
    static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, CONTACT_PATH, URI_CONTACTS_NAME_TODAY_BIRTH);

    }

    @Override
    public boolean onCreate() {
        dbHelper = new MySQLiteOpenHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Log.d(TAG, "query, " + uri.toString());

        switch (uriMatcher.match(uri)) {
            case URI_CONTACTS_NAME_TODAY_BIRTH:
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT name FROM contacts" +
                " WHERE strftime('%m-%d', date/1000 + '" + DAY_SEC + "', 'unixepoch')" +
                " = ( strftime('%m-%d', 'now'))", null);
        return cursor;
    }


    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case URI_CONTACTS_NAME_TODAY_BIRTH:
                return CONTACT_CONTENT_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
