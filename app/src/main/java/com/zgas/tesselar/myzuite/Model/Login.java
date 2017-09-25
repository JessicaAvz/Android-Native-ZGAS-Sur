package com.zgas.tesselar.myzuite.Model;

/**
 * Created by jarvizu on 25/09/2017.
 */

public class Login {
    private int loginId;
    private String loginEmail;
    private String loginPassword;
    private String loginApiToken;

    public Login() {
    }

    public Login(int loginId, String loginEmail, String loginPassword, String loginApiToken) {
        this.loginId = loginId;
        this.loginEmail = loginEmail;
        this.loginPassword = loginPassword;
        this.loginApiToken = loginApiToken;
    }

    public int getLoginId() {
        return loginId;
    }

    public void setLoginId(int loginId) {
        this.loginId = loginId;
    }

    public String getLoginEmail() {
        return loginEmail;
    }

    public void setLoginEmail(String loginEmail) {
        this.loginEmail = loginEmail;
    }

    public String getLoginPassword() {
        return loginPassword;
    }

    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword;
    }

    public String getLoginApiToken() {
        return loginApiToken;
    }

    public void setLoginApiToken(String loginApiToken) {
        this.loginApiToken = loginApiToken;
    }
}
