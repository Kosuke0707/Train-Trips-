package com.example.myapplication;
public class User {
    private String name;
    private String email;
    private String phoneNumber;
    private String gender;
    private String dob;
    // Empty constructor (required by Firebase)
    public User() {
    }

    public User(String name, String email, String phoneNumber, String gender, String dob) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.dob= dob;
    }

    // Getters and setters
    // You can generate them automatically in most IDEs

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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getdob() {
        return dob;
    }

    public void setdob(String dob) {
        this.dob = dob;
    }

}
