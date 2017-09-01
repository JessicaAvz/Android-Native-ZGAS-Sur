package com.zgas.tesselar.myzuite.Model;

import java.util.Calendar;

/**
 * Created by jarvizu on 28/08/2017.
 */

public class Case {
    private int caseId;
    private int caseUserId;
    private Calendar caseTimeIn;
    private Calendar caseTimeSeen;
    private Calendar caseTimeArrival;
    private Calendar caseTimeProgrammed;
    private caseStatus caseStatus;
    private casePriority casePriority;
    private String caseClientName;
    private String caseClientLastname;
    private String caseAddress;
    private caseTypes caseType;

    public enum casePriority {
        HIGH("Alta"),
        MEDIUM("Media"),
        LOW("Baja");

        private final String name;

        private casePriority(String s) {
            name = s;
        }

        public boolean equalsName(String otherName) {
            return name.equals(otherName);
        }

        public String toString() {
            return this.name;
        }
    }

    public enum caseStatus {
        CANCELLED("Cancelado"),
        INPROGRESS("En curso"),
        FINISHED("Finalizado");

        private final String name;

        private caseStatus(String s) {
            name = s;
        }

        public boolean equalsName(String otherName) {
            return name.equals(otherName);
        }

        public String toString() {
            return this.name;
        }
    }

    public enum caseTypes {
        CANCELLATION("Cancelación"),
        LEAKAGE("Fuga"),
        CUT("Corte"),
        ORDER("Pedido"),
        RECONNECTION("Reconexión");

        private final String name;

        private caseTypes(String s) {
            name = s;
        }

        public boolean equalsName(String otherName) {
            return name.equals(otherName);
        }

        public String toString() {
            return this.name;
        }
    }

    public Case() {
    }

    public Case(int caseId, int caseUserId, Calendar caseTimeIn, Calendar caseTimeSeen, Calendar caseTimeArrival, Calendar caseTimeProgrammed, Case.caseStatus caseStatus, Case.casePriority casePriority, String caseClientName, String caseClientLastname, String caseAddress, caseTypes caseType) {
        this.caseId = caseId;
        this.caseUserId = caseUserId;
        this.caseTimeIn = caseTimeIn;
        this.caseTimeSeen = caseTimeSeen;
        this.caseTimeArrival = caseTimeArrival;
        this.caseTimeProgrammed = caseTimeProgrammed;
        this.caseStatus = caseStatus;
        this.casePriority = casePriority;
        this.caseClientName = caseClientName;
        this.caseClientLastname = caseClientLastname;
        this.caseAddress = caseAddress;
        this.caseType = caseType;
    }

    public int getCaseId() {
        return caseId;
    }

    public void setCaseId(int caseId) {
        this.caseId = caseId;
    }

    public int getCaseUserId() {
        return caseUserId;
    }

    public void setCaseUserId(int caseUserId) {
        this.caseUserId = caseUserId;
    }

    public Calendar getCaseTimeIn() {
        return caseTimeIn;
    }

    public void setCaseTimeIn(Calendar caseTimeIn) {
        this.caseTimeIn = caseTimeIn;
    }

    public Calendar getCaseTimeSeen() {
        return caseTimeSeen;
    }

    public void setCaseTimeSeen(Calendar caseTimeSeen) {
        this.caseTimeSeen = caseTimeSeen;
    }

    public Calendar getCaseTimeArrival() {
        return caseTimeArrival;
    }

    public void setCaseTimeArrival(Calendar caseTimeArrival) {
        this.caseTimeArrival = caseTimeArrival;
    }

    public caseStatus getCaseStatus() {
        return caseStatus;
    }

    public void setCaseStatus(caseStatus caseStatus) {
        this.caseStatus = caseStatus;
    }

    public casePriority getCasePriority() {
        return casePriority;
    }

    public void setCasePriority(casePriority casePriority) {
        this.casePriority = casePriority;
    }

    public String getCaseClientName() {
        return caseClientName;
    }

    public void setCaseClientName(String caseClientName) {
        this.caseClientName = caseClientName;
    }

    public String getCaseAddress() {
        return caseAddress;
    }

    public void setCaseAddress(String caseAddress) {
        this.caseAddress = caseAddress;
    }

    public caseTypes getCaseType() {
        return caseType;
    }

    public void setCaseType(caseTypes caseType) {
        this.caseType = caseType;
    }

    public String getCaseClientLastname() {
        return caseClientLastname;
    }

    public void setCaseClientLastname(String caseClientLastname) {
        this.caseClientLastname = caseClientLastname;
    }

    public Calendar getCaseTimeProgrammed() {
        return caseTimeProgrammed;
    }

    public void setCaseTimeProgrammed(Calendar caseTimeProgrammed) {
        this.caseTimeProgrammed = caseTimeProgrammed;
    }
}
