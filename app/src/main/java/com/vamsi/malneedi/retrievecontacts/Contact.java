package com.vamsi.malneedi.retrievecontacts;

public class Contact {

    private String contactName = "";
    private String contactID = "";
    private String contactNumber = "";

    public Contact(String name, String id, String number) {
        contactName = name;
        contactID = id;
        contactNumber = number;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactID() {
        return contactID;
    }

    public void setContactID(String contactID) {
        this.contactID = contactID;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }
}
