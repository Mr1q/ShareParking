package com.example.qjh.comprehensiveactivity.utils;

import com.example.qjh.comprehensiveactivity.constant.Constants;

public class NewsRequest {

    @Override
    public String toString() {
        String retValue;
        retValue = "?" + "&username=" +username+ "&password=" + password;
       return retValue;
    }

    private String username;

    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
