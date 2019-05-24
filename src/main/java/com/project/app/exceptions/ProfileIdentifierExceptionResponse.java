package com.project.app.exceptions;

public class ProfileIdentifierExceptionResponse {

    private String profileIdentifier;

    ProfileIdentifierExceptionResponse(String profileIdentifier) {
        this.profileIdentifier = profileIdentifier;
    }

    public String getProfileIdentifier() {
        return profileIdentifier;
    }

    public void setProfileIdentifier(String profileIdentifier) {
        this.profileIdentifier = profileIdentifier;
    }
}
