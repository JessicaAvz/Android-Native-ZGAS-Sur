package com.zgas.tesselar.myzuite.Model;

/**
 * Created by jarvizu on 28/08/2017.
 */

public class User {
    private int userId;
    private int userType;
    private String userName;
    private String userLastname;
    private String userEmail;
    private String userPassword;
    private String userRoute;
    private String userZone;
    private userType userstatus;

    public enum userType{
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

    public User(int userId, int userType, String userName, String userLastname, String userEmail, String userPassword, String userRoute, String userZone, User.userType userstatus) {
        this.userId = userId;
        this.userType = userType;
        this.userName = userName;
        this.userLastname = userLastname;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userRoute = userRoute;
        this.userZone = userZone;
        this.userstatus = userstatus;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserLastname() {
        return userLastname;
    }

    public void setUserLastname(String userLastname) {
        this.userLastname = userLastname;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
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

    public User.userType getUserstatus() {
        return userstatus;
    }

    public void setUserstatus(User.userType userstatus) {
        this.userstatus = userstatus;
    }
}
