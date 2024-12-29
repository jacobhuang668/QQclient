package com.huangjian.qqcommon;

import java.io.Serializable;

public class User implements Serializable {

    private static final long serialVersionUID = 2845064624791970491L;
    private String password;
    private String userId;
    public User() {
    }

    public User(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }



    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
