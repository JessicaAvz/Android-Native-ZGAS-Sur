package com.zgas.tesselar.myzuite.Model;

/**
 * Created by jarvizu on 04/01/2018.
 */

public class Incidence {
    private String idUser;
    private String idIncidence;
    private String descriptionIncidence;
    private String timeIncidence;

    public Incidence(String idUser, String idIncidence, String descriptionIncidence, String timeIncidence) {
        this.idUser = idUser;
        this.idIncidence = idIncidence;
        this.descriptionIncidence = descriptionIncidence;
        this.timeIncidence = timeIncidence;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getIdIncidence() {
        return idIncidence;
    }

    public void setIdIncidence(String idIncidence) {
        this.idIncidence = idIncidence;
    }

    public String getDescriptionIncidence() {
        return descriptionIncidence;
    }

    public void setDescriptionIncidence(String descriptionIncidence) {
        this.descriptionIncidence = descriptionIncidence;
    }

    public String getTimeIncidence() {
        return timeIncidence;
    }

    public void setTimeIncidence(String timeIncidence) {
        this.timeIncidence = timeIncidence;
    }
}
