package com.zgas.tesselar.myzuite.Model;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;

/**
 * Created by jarvizu on 28/08/2017.
 */

public class User {
    private int userId;
    private userType userType;
    private String userName;
    private String userEmail;
    private String userRoute;
    private String userZone;
    private userStatus userstatus;

    public enum userStatus {
        ACTIVE("Activo"),
        NOTACTIVE("No activo");

        private final String name;

        private userStatus(String s) {
            name = s;
        }

        public boolean equalsName(String otherName) {
            return name.equals(otherName);
        }

        public String toString() {
            return this.name;
        }

    }

    public enum userType {
        OPERATOR("Operador"),
        SERVICE("Servicio medido"),
        SUPERVISOR("Supervisor"),
        LEAKAGE("Técnico de fugas");

        private final String name;

        private userType(String s) {
            name = s;
        }

        public boolean equalsName(String otherName) {
            return name.equals(otherName);
        }

        public String toString() {
            return this.name;
        }
    }

    public User() {
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public User.userType getUserType() {
        return userType;
    }

    public void setUserType(User.userType userType) {
        this.userType = userType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserRoute() {
        return userRoute;
    }

    public void setUserRoute(String userRoute) {
        this.userRoute = userRoute;
    }

    public String getUserZone() {
        return userZone;
    }

    public void setUserZone(String userZone) {
        this.userZone = userZone;
    }

    public userStatus getUserstatus() {
        return userstatus;
    }

    public void setUserstatus(userStatus userstatus) {
        this.userstatus = userstatus;
    }
}
