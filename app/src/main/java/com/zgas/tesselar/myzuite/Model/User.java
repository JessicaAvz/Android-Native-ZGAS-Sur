package com.zgas.tesselar.myzuite.Model;

import com.google.gson.Gson;

/**
 * Created by jarvizu on 28/08/2017.
 */

public class User {
    private String userId;
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
        LEAKAGE("Técnico de Fugas");

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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
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

    /**
     * @return un String con formato json con las propiedades correspondientes.
     */

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    /**
     * @param json en formato correcto de json, ej: {"height":1.6,"id":1001,"isAdmin":true,"name":"Jessica"}
     * @return una instancia de tipo User con los valores que estaban en el json.
     */
    public static User fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, User.class);
    }
}
