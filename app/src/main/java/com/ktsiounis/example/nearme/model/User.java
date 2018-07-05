package com.ktsiounis.example.nearme.model;

/**
 * Created by Konstantinos Tsiounis on 05-Jul-18.
 */
public class User {
    public String username;
    public String firstName;
    public String lastName;

    public User(String firstName, String username, String lastName) {
        this.firstName = firstName;
        this.username = username;
        this.lastName = lastName;
    }
}
