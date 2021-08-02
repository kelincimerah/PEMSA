package com.example.pemsa.models;

import java.io.Serializable;

public class User implements Serializable {

    public String email, fullName, address, phone, password, url, id;
    public int type;

    public User() {
    }

    public User(String fullName, String email) {
        this.fullName = fullName;
        this.email = email;
    }

    public User(String email, String fullName, String address, String phone, int type) {
        this.email = email;
        this.fullName = fullName;
        this.address = address;
        this.phone = phone;
        this.type = type;
    }

    public User(String email, String fullName, String address, String phone, String url, int type) {
        this.email = email;
        this.fullName = fullName;
        this.address = address;
        this.phone = phone;
        this.url = url;
        this.type = type;
    }

    public User(String email, String fullName, String address, String phone, String url, String id, int type) {
        this.email = email;
        this.fullName = fullName;
        this.address = address;
        this.phone = phone;
        this.url = url;
        this.id = id;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
