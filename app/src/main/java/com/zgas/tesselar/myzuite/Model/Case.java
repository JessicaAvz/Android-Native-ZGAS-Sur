package com.zgas.tesselar.myzuite.Model;

/**
 * Created by jarvizu on 28/08/2017.
 */

public class Case {
    private String caseId;
    private String caseUserId;
    private String caseTimeAssignment;
    private String caseTimeSeen;
    private String caseTimeArrival;
    private String caseTimeScheduled;
    private String caseServiceType;
    private caseStatus caseStatus;
    private casePriority casePriority;
    private String caseClientName;
    private String caseAddress;
    private caseTypes caseType;

    public enum casePriority {
        HIGH("High"),
        MEDIUM("Medium"),
        LOW("Low");

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
        NEW("New"),
        ASSIGNED("Asignado"),
        SEEN("Visto"),
        ACCEPTED("Aceptado"),
        INPROGRESS("En curso"),
        RETIRED("Cilindro retirado"),
        FINISHED("Entregado"),
        CANCELLED("Cancelado");

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
        LEAKAGE("Fuga"),
        CUT("Corte"),
        ORDER("Pedido"),
        CUSTOM_SERVICE("Servicio medido"),
        RECONNECTION("Re-conexión");

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

    public Case(String caseId, String caseUserId, String caseTimeAssignment, String caseTimeSeen, String caseTimeArrival, String caseTimeScheduled, String caseServiceType, Case.caseStatus caseStatus, Case.casePriority casePriority, String caseClientName, String caseAddress, caseTypes caseType) {
        this.caseId = caseId;
        this.caseUserId = caseUserId;
        this.caseTimeAssignment = caseTimeAssignment;
        this.caseTimeSeen = caseTimeSeen;
        this.caseTimeArrival = caseTimeArrival;
        this.caseTimeScheduled = caseTimeScheduled;
        this.caseServiceType = caseServiceType;
        this.caseStatus = caseStatus;
        this.casePriority = casePriority;
        this.caseClientName = caseClientName;
        this.caseAddress = caseAddress;
        this.caseType = caseType;
    }

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    public String getCaseUserId() {
        return caseUserId;
    }

    public void setCaseUserId(String caseUserId) {
        this.caseUserId = caseUserId;
    }

    public String getCaseTimeAssignment() {
        return caseTimeAssignment;
    }

    public void setCaseTimeAssignment(String caseTimeAssignment) {
        this.caseTimeAssignment = caseTimeAssignment;
    }

    public String getCaseTimeSeen() {
        return caseTimeSeen;
    }

    public void setCaseTimeSeen(String caseTimeSeen) {
        this.caseTimeSeen = caseTimeSeen;
    }

    public String getCaseTimeArrival() {
        return caseTimeArrival;
    }

    public void setCaseTimeArrival(String caseTimeArrival) {
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


    public String getCaseTimeScheduled() {
        return caseTimeScheduled;
    }

    public void setCaseTimeScheduled(String caseTimeScheduled) {
        this.caseTimeScheduled = caseTimeScheduled;
    }

    public String getCaseServiceType() {
        return caseServiceType;
    }

    public void setCaseServiceType(String caseServiceType) {
        this.caseServiceType = caseServiceType;
    }
}
