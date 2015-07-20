package com.nix.dimablyznyuk.student.contacts;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.util.Xml;
import android.widget.Toast;

import com.nix.dimablyznyuk.student.contacts.model.Contact;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dima Blyznyuk on 10.07.15.
 */
public class ContactXMLSerializer {

    public static final String FILE_NAME = "contacts.xml";

    public static final String EXTERNAL = "External";
    public static final String INTERNAL = "Internal";

    public static final String ID_TAG = "id";
    public static final String NAME_TAG = "name";
    public static final String ADDRESS_TAG = "address";
    public static final String PHONE_TAG = "phone";
    public static final String GENDER_TAG = "gender";
    public static final String PHOTO_TAG = "photo";
    public static final String DATE_TAG = "date";
    public static final String CONTACT_TAG = "contact";
    public static final String CONTACTS_TAG = "contacts";

    public static final String TAG = "ContactXMLSerializer";
    private Context context;
    private File file;

    ContactXMLSerializer(Context context) {
        this.context = context;
    }

    List<Contact> loadContacts(String storage) {
        List<Contact> result = new ArrayList<Contact>();

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();

            FileInputStream fis = null;
            if (storage.equals(INTERNAL)) {
                Log.d(TAG, "load Internal");
                fis = context.openFileInput(FILE_NAME);
            } else if (storage.equals(EXTERNAL)) {
                Log.d(TAG, "load External");

                file = new File(Environment.getExternalStorageDirectory() + "/" + FILE_NAME);
                fis = new FileInputStream(file);
            }

            parser.setInput(new InputStreamReader(fis));

            int eventType = parser.getEventType();
            String currentTag = null;
            Integer id = null;
            String name = null;
            String address = null;
            String phone = null;
            int gender = 0;
            int date = 0;
            String photo = null;


            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    currentTag = parser.getName();
                } else if (eventType == XmlPullParser.TEXT) {
                    if (ID_TAG.equals(currentTag)) {
                        id = Integer.valueOf(parser.getText());
                        Log.d(TAG, "id" + id);
                    }
                    if (NAME_TAG.equals(currentTag)) {
                        name = parser.getText();
                        Log.d(TAG, "name" + name);
                    }
                    if (ADDRESS_TAG.equals(currentTag)) {
                        address = parser.getText();
                        Log.d(TAG, "address" + name);
                    }
                    if (PHONE_TAG.equals(currentTag)) {
                        phone = parser.getText();
                        Log.d(TAG, "phone" + name);
                    }
                    if (GENDER_TAG.equals(currentTag)) {
                        gender = Integer.valueOf(parser.getText());
                        Log.d(TAG, "gender" + name);
                    }
                    if (PHOTO_TAG.equals(currentTag)) {
                        photo = (parser.getText().equals("")) ? null : parser.getText();
                    }
                    if (DATE_TAG.equals(currentTag)) {
                        date = Integer.valueOf(parser.getText());
                        Log.d(TAG, "date" + name);
                    }

                } else if (eventType == XmlPullParser.END_TAG) {
                    if (CONTACT_TAG.equals(parser.getName())) {
                        result.add(new Contact(id, name, address, phone, gender, photo, date));
                    }
                }
                eventType = parser.next();
            }
            return result;
        } catch (Exception e) {
            Toast.makeText(context,
                    context.getString(R.string.error_in_loading_xml) + e.toString(),
                    Toast.LENGTH_LONG)
                    .show();

        }
        return result;
    }

    public void writeContacts(List<Contact> list, String storage) {
        Log.d(TAG, "writeContacts" + list.size() + storage);
        Writer writer = null;
        String xmlString = getXMLString(list);
        try {
            FileOutputStream out = null;
            if (storage.equals(INTERNAL)) {
                Log.d(TAG, "Internal" + list.size() + storage);
                out = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            } else if (storage.equals(EXTERNAL)) {
                Log.d(TAG, "External" + list.size() + storage + xmlString);

                File file = new File(Environment.getExternalStorageDirectory(), "/" + FILE_NAME);
                out = new FileOutputStream(file);
            }
            out.write(xmlString.getBytes());
            out.close();
        } catch (IOException e) {
            Log.d(TAG, "Exeption");
            e.printStackTrace();
        } finally {
            Log.d(TAG, "finally");
            if (writer != null)
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }


    private String getXMLString(List<Contact> contacts) {
        XmlSerializer serializer = Xml.newSerializer();
        StringWriter writer = new StringWriter();
        try {
            serializer.setOutput(writer);
            serializer.startDocument("UTF-8", true);
            serializer.startTag("", CONTACTS_TAG);
            serializer.attribute("", "number", String.valueOf(contacts.size()));
            for (Contact contact : contacts) {
                serializer.startTag("", CONTACT_TAG);
                serializer.startTag("", ID_TAG);
                serializer.text(String.valueOf(contact.getId()));
                serializer.endTag("", ID_TAG);
                serializer.startTag("", NAME_TAG);
                serializer.text(contact.getName());
                serializer.endTag("", NAME_TAG);
                serializer.startTag("", ADDRESS_TAG);
                serializer.text(contact.getAddress());
                serializer.endTag("", ADDRESS_TAG);
                serializer.startTag("", PHONE_TAG);
                serializer.text(contact.getPhoneNumber());
                serializer.endTag("", PHONE_TAG);
                serializer.startTag("", GENDER_TAG);
                serializer.text(String.valueOf(contact.getGender()));
                serializer.endTag("", GENDER_TAG);
                serializer.startTag("", PHOTO_TAG);
                serializer.text((contact.getPhoto() == null) ? "" : contact.getPhoto());
                serializer.endTag("", PHOTO_TAG);
                serializer.startTag("", DATE_TAG);
                serializer.text(String.valueOf(contact.getGender()));
                serializer.endTag("", DATE_TAG);
                serializer.endTag("", CONTACT_TAG);
            }
            serializer.endTag("", CONTACTS_TAG);
            serializer.endDocument();
            return writer.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
