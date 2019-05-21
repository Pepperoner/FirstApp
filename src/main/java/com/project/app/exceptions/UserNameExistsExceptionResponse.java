package com.project.app.exceptions;

public class UserNameExistsExceptionResponse {

    private String username;

    UserNameExistsExceptionResponse(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
