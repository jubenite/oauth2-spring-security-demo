package org.tsystems.oauth2demo.proxy.controller;

import java.io.Serializable;

/**
 * Created by t-systems
 */
public class OAuth2AuthenticationResponse implements Serializable {

    private static final long serialVersionUID = 1250166508152483573L;

    private final String accessToken;
    private final String refreshToken;

    public OAuth2AuthenticationResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public String getAccessToken() {
        return this.accessToken;
    }
    
    public String getRefreshToken() {
        return this.refreshToken;
    }
}
