package com.nix.dima_blyznyuk.student.contacts.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Dima Blyznyuk on 06.07.15.
 */
public class ContactManager {

    private static ContactManager instance;
    private List<Contact> contacts;

    private ContactManager(){
        contacts = new ArrayList<Contact>();
    }

    public static ContactManager getInstance() {
         if (null == instance) {
             instance = new ContactManager();
        }
        return instance;
    }

    public boolean addContact(Contact contact){
        for (Contact c : contacts){
            if(c.getId().equals(contact.getId())) {
                return false;
            }
        }
        contacts.add(contact);
        return true;
    }

    public List<Contact> getContacts(){
        return contacts;
    }

    public Contact getContact(UUID id) {
        for (Contact c : contacts) {
            if (c.getId().equals(id)) {
                return c;
            }
        }
        return null;
    }
    public void deleteContact(Contact c){
        contacts.remove(c);
    }
}
