package org.tsystems.oauth2demo.proxy.controller;

import java.io.Serializable;

/**
 * Created by t-systems
 */
public class  OAuth2AuthenticationRequest implements Serializable {

    private static final long serialVersionUID = -8445943548965154778L;

    private String username;
    private String password;

    public OAuth2AuthenticationRequest() {
        super();
    }

    public OAuth2AuthenticationRequest(String username, String password) {
        this.setUsername(username);
        this.setPassword(password);
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}