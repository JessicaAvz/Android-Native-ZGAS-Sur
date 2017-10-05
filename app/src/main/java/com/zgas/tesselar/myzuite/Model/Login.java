package com.zgas.tesselar.myzuite.Model;

import com.google.gson.Gson;

/**
 * Created by jarvizu on 25/09/2017.
 */

public class Login {
    private String loginId;
    private String loginAccessToken;
    private String loginInstanceUrl;
    private String loginTokenType;
    private String loginIssuedAt;
    private String loginSignature;
    private String loginEmail;

    public Login() {
    }

    public Login(String loginId, String loginAccessToken, String loginInstanceUrl, String loginTokenType, String loginIssuedAt, String loginSignature, String loginEmail) {
        this.loginId = loginId;
        this.loginAccessToken = loginAccessToken;
        this.loginInstanceUrl = loginInstanceUrl;
        this.loginTokenType = loginTokenType;
        this.loginIssuedAt = loginIssuedAt;
        this.loginSignature = loginSignature;
        this.loginEmail = loginEmail;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getLoginAccessToken() {
        return loginAccessToken;
    }

    public void setLoginAccessToken(String loginAccessToken) {
        this.loginAccessToken = loginAccessToken;
    }

    public String getLoginInstanceUrl() {
        return loginInstanceUrl;
    }

    public void setLoginInstanceUrl(String loginInstanceUrl) {
        this.loginInstanceUrl = loginInstanceUrl;
    }

    public String getLoginTokenType() {
        return loginTokenType;
    }

    public void setLoginTokenType(String loginTokenType) {
        this.loginTokenType = loginTokenType;
    }

    public String getLoginIssuedAt() {
        return loginIssuedAt;
    }

    public void setLoginIssuedAt(String loginIssuedAt) {
        this.loginIssuedAt = loginIssuedAt;
    }

    public String getLoginSignature() {
        return loginSignature;
    }

    public void setLoginSignature(String loginSignature) {
        this.loginSignature = loginSignature;
    }

    public String getLoginEmail() {
        return loginEmail;
    }

    public void setLoginEmail(String loginEmail) {
        this.loginEmail = loginEmail;
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    /**
     * @param json en formato correcto de json, ej: {"height":1.6,"id":1001,"isAdmin":true,"name":"Jessica"}
     * @return una instancia de tipo User con los valores que estaban en el json.
     */
    public static Login fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, Login.class);
    }
}

