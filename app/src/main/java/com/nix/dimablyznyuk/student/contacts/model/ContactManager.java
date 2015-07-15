package com.nix.dimablyznyuk.student.contacts.model;

import com.nix.dimablyznyuk.student.contacts.MainActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dima Blyznyuk on 06.07.15.
 */
public class ContactManager implements Manager {

    private static ContactManager instance;
    private List<Contact> contacts;
    private static int id;

    private ContactManager() {
        contacts = new ArrayList<Contact>();
    }

    public static ContactManager getInstance() {
        if (null == instance) {
            instance = new ContactManager();
        }
        return instance;
    }

    public void addContacts(List<Contact> list) {
        for (Contact c : list) {
            contacts.add(new Contact(id++, c.getName(), c.getAddress()
                    , c.getPhoneNumber(), c.getGender(), c.getPhoto()));
        }
    }

    public int createContact(String name, String address, String phone, String gender,
                             String photo) {
        contacts.add(new Contact(id++, name, address, phone, gender, photo));
        return id;
    }

    public void updateContact(Contact contact) {
    }

    public boolean isContactExist(Contact contact) {
        for (Contact c : contacts) {
            if (c.getId() == (contact.getId())) {
                return true;
            }
        }
        return false;
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    public List<Contact> getContactsMale() {
        List<Contact> list = new ArrayList<Contact>();
        for (Contact c : contacts) {
            if (c.getGender().equals(MainActivity.MALE)) {
                list.add(c);
            }
        }
        return list;
    }

    public List<Contact> getContactsFemale() {
        List<Contact> list = new ArrayList<Contact>();
        for (Contact c : contacts) {
            if (c.getGender().equals(MainActivity.FEMALE)) {
                list.add(c);
            }
        }
        return list;
    }

    public Contact getContact(int id) {
        for (Contact c : contacts) {
            if (c.getId() == id) {
                return c;
            }
        }
        return null;
    }

    public void removeContact(Contact c) {
        contacts.remove(c);
    }

    public void removeContacts(List<Contact> listToDelete) {
        contacts.removeAll(listToDelete);
    }

    public void removeAllContacts() {
        contacts.clear();
    }

    public void close() {
    }

    public void open() {
    }
}
