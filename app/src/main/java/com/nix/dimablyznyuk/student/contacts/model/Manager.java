package com.nix.dimablyznyuk.student.contacts.model;

import java.util.List;

/**
 * Created by Dima Blyznyuk on 08.07.15.
 */
public interface Manager {

    int createContact(String name, String address, String phone, String gender);

    Contact getContact(int id);

    void updateContact(Contact contact);

    List<Contact> getContacts();

    void removeContact(Contact c);

    List<Contact> getContactsFemale();

    List<Contact> getContactsMale();

    void removeContacts(List<Contact> list);

    void removeAllContacts();

    boolean isContactExist(Contact c);

    void open();

    void close();

    void addContacts(List<Contact> list);


}

