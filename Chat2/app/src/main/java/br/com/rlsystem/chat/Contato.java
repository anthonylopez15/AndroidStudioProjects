package br.com.rlsystem.chat;

public class Contato {
    private int _id;

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public int getContact_user() {
        return contact_user;
    }

    public void setContact_user(int contact_user) {
        this.contact_user = contact_user;
    }

    public String getName_contact() {
        return name_contact;
    }

    public void setName_contact(String name_contact) {
        this.name_contact = name_contact;
    }

    private int id_user;
    private int contact_user;
    private String name_contact;

    public String getPhoto_contact() {
        return photo_contact;
    }

    public void setPhoto_contact(String photo_contact) {
        this.photo_contact = photo_contact;
    }

    private String photo_contact;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }
}
