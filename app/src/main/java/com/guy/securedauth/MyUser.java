package com.guy.securedauth;

import java.util.ArrayList;

public class MyUser {

    private String uid = "";
    private String phone;
    private String email;
    private String name;
    private ArrayList<String> courses = new ArrayList<>();

    public MyUser() { }

    public String getUid() {
        return uid;
    }

    public MyUser setUid(String uid) {
        this.uid = uid;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public MyUser setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public MyUser setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getName() {
        return name;
    }

    public MyUser setName(String _name) {
        if (_name == null  ||  _name.equals("")) {
            return this;
        }
        this.name = _name;
        return this;
    }

    public ArrayList<String> getCourses() {
        return courses;
    }

    public MyUser setCourses(ArrayList<String> courses) {
        this.courses = courses;
        return this;
    }
}
