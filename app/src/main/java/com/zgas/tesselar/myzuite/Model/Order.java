package com.zgas.tesselar.myzuite.Model;

/**
 * Created by jarvizu on 28/08/2017.
 */

public class Order {
    private String caseId;
    private String caseUserId;
    private String caseTimeAssignment;
    private String caseTimeSeen;
    private String caseTimeArrival;
    private String caseTimeScheduled;
    private String caseServiceType;
    private String caseAccountName;
    private String caseContactName;
    private String caseAddress;
    private String caseSubject;
    private caseStatus caseStatus;
    private caseTypes caseType;
    private casePriority casePriority;

    public Order() {
    }

    public Order(String caseId, String caseUserId, String caseTimeAssignment, String caseTimeSeen, String caseTimeArrival, String caseTimeScheduled, String caseServiceType, String caseAccountName, String caseContactName, String caseAddress, String caseSubject, Order.caseStatus caseStatus, caseTypes caseType, Order.casePriority casePriority) {
        this.caseId = caseId;
        this.caseUserId = caseUserId;
        this.caseTimeAssignment = caseTimeAssignment;
        this.caseTimeSeen = caseTimeSeen;
        this.caseTimeArrival = caseTimeArrival;
        this.caseTimeScheduled = caseTimeScheduled;
        this.caseServiceType = caseServiceType;
        this.caseAccountName = caseAccountName;
        this.caseContactName = caseContactName;
        this.caseAddress = caseAddress;
        this.caseSubject = caseSubject;
        this.caseStatus = caseStatus;
        this.caseType = caseType;
        this.casePriority = casePriority;
    }

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
        CLOSED("Closed"),
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
        CUT("Corte"),
        ORDER("Pedido"),
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

    public String getCaseAccountName() {
        return caseAccountName;
    }

    public void setCaseAccountName(String caseAccountName) {
        this.caseAccountName = caseAccountName;
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

    public String getCaseContactName() {
        return caseContactName;
    }

    public void setCaseContactName(String caseContactName) {
        this.caseContactName = caseContactName;
    }

    public String getCaseSubject() {
        return caseSubject;
    }

    public void setCaseSubject(String caseSubject) {
        this.caseSubject = caseSubject;
    }
}
