package com.rasamadev.ardustato.models;

public class User {
    public String id;
    public String fullname;
    public String mail;
    public String pass;

    public User(String id, String fullname, String mail, String pass) {
        this.id = id;
        this.fullname = fullname;
        this.mail = mail;
        this.pass = pass;
    }

    public User(String fullname, String mail, String pass) {
        this.fullname = fullname;
        this.mail = mail;
        this.pass = pass;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", fullname='" + fullname + '\'' +
                ", mail='" + mail + '\'' +
                ", pass='" + pass + '\'' +
                '}';
    }
}
