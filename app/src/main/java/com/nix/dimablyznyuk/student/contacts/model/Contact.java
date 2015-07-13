package com.nix.dimablyznyuk.student.contacts.model;

/**
 * Created by Dima Blyznyuk on 06.07.15.
 */
public class Contact {

    private int id;
    private String name;
    private String address;
    private String phoneNumber;
    private String gender;
    private boolean isSelected;

    public Contact() {
    }

    public Contact(int id, String name, String address, String phoneNumber, String gender) {

        this.id = id;
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getGender() {
        return gender;
    }

    public boolean getSelected() {
        return isSelected;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public String toString() {
        return getName();
    }


}
