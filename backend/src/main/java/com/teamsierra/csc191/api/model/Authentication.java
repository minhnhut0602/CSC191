package com.teamsierra.csc191.api.model;

/**
 * Created with IntelliJ IDEA.
 * User: scott
 * Date: 11/24/13
 * Time: 1:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class Authentication extends GenericModel{

    private String authToken;

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
