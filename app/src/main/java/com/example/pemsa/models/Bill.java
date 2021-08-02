package com.example.pemsa.models;

import java.io.Serializable;

public class Bill implements Serializable {

    String dateOfBill, currentDate, currentTime, uri;
    int bill, status;
    String documentId;

    String name, email, address, idCitizen;

    public Bill() {
    }

//    public Bill(String dateOfBill, String currentDate, String currentTime, int bill, int status) {
//        this.dateOfBill = dateOfBill;
//        this.currentDate = currentDate;
//        this.currentTime = currentTime;
//        this.bill = bill;
//        this.status = status;
//    }

    public Bill(String dateOfBill, String currentDate, String currentTime, String uri, int bill, int status) {
        this.dateOfBill = dateOfBill;
        this.currentDate = currentDate;
        this.currentTime = currentTime;
        this.uri = uri;
        this.bill = bill;
        this.status = status;
    }

    public Bill(String dateOfBill, String currentDate, String currentTime, String uri, int bill, int status, String name, String email, String address) {
        this.dateOfBill = dateOfBill;
        this.currentDate = currentDate;
        this.currentTime = currentTime;
        this.uri = uri;
        this.bill = bill;
        this.status = status;
        this.name = name;
        this.email = email;
        this.address = address;
    }

    public Bill(String dateOfBill, String currentDate, String currentTime, String uri, int bill, int status, String name, String email, String address, String idCitizen) {
        this.dateOfBill = dateOfBill;
        this.currentDate = currentDate;
        this.currentTime = currentTime;
        this.uri = uri;
        this.bill = bill;
        this.status = status;
        this.name = name;
        this.email = email;
        this.address = address;
        this.idCitizen = idCitizen;
    }

    public String getIdCitizen() {
        return idCitizen;
    }

    public void setIdCitizen(String idCitizen) {
        this.idCitizen = idCitizen;
    }

    public String getDateOfBill() {
        return dateOfBill;
    }

    public void setDateOfBill(String dateOfBill) {
        this.dateOfBill = dateOfBill;
    }

    public int getBill() {
        return bill;
    }

    public void setBill(int bill) {
        this.bill = bill;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
