package com.study.entity;

import java.io.Serializable;

public class UserToken implements Serializable {
    public UserToken() {}

    public UserToken(String username, Integer userId) {
        this.userId = userId;
        this.username = username;
    }
////
    /**
     * 用户id
     */
    private Integer userId;
    /**
     * 用户登录名
     */
    private String username;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "UserToken{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                '}';
    }
}
