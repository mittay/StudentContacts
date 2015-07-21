package com.nix.dimablyznyuk.student.contacts.model;

/**
 * Created by Dima Blyznyuk on 06.07.15.
 */
public class Contact {

    private int id;
    private String name;
    private String address;
    private String phoneNumber;
    private int gender; // Male = 0; Female = 1
    private String photoPath;
    private String dateBirthday;
    private boolean isSelected;

    public Contact() {
    }

    public Contact(int id, String name, String address, String phoneNumber, int gender,
                   String photo, String dateBirthday) {

        this.id = id;
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.photoPath = photo;
        this.dateBirthday = dateBirthday;
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

    public int getGender() {
        return gender;
    }

    public String getPhoto() {
        return photoPath;
    }

    public String getDateBirthday() {
        return dateBirthday;
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

    public void setGender(int gender) {
        this.gender = gender;
    }

    public void setDateBirthday(String dateBirthday) {
        this.dateBirthday = dateBirthday;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public void setPhoto(String photo) {
        this.photoPath = photo;
    }

    public String toString() {
        return getName();
    }

}
