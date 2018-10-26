package com.example.model;

/**
 * Created by ChangLiu on 10/25/18.
 */
public class Users {
    protected String userName;

    public Users(String userName) {
        this.userName = userName;
    }

    /** Getters and setters. */
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
