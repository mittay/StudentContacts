package com.nix.dimablyznyuk.student.contacts.model;

import java.util.List;
import java.util.UUID;

/**
 * Created by Dima Blyznyuk on 08.07.15.
 */
public interface Manager<E> {

    public abstract boolean addContact(Contact contact);
    public abstract List<Contact> getContacts();
    public abstract void deleteContact(Contact c);
    public abstract Contact getContact(UUID id);


}

