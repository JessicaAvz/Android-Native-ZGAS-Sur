package com.zgas.tesselar.myzuite.Model;

/**
 * Created by jarvizu on 28/08/2017.
 */

public class Order {

    private String orderId;
    private String orderUserId;
    private String orderTimeAssignment;
    private String orderTimeSeen;
    private String orderTimeDeparture;
    private String oderTimeEnd;
    private String orderTimeScheduled;
    private String orderServiceType;
    private String orderAccountName;
    private String orderContactName;
    private String orderAddress;
    private String orderSubject;
    private String orderNotice;
    private String orderPaymentMethod;
    private caseStatus orderStatus;
    private caseTypes orderType;
    private casePriority orderPriority;

    public Order() {
    }

    public Order(String orderId, String orderUserId, String orderTimeAssignment, String orderTimeSeen, String orderTimeDeparture, String oderTimeEnd, String orderTimeScheduled, String orderServiceType, String orderAccountName, String orderContactName, String orderAddress, String orderSubject, String orderNotice, String orderPaymentMethod, caseStatus orderStatus, caseTypes orderType, casePriority orderPriority) {
        this.orderId = orderId;
        this.orderUserId = orderUserId;
        this.orderTimeAssignment = orderTimeAssignment;
        this.orderTimeSeen = orderTimeSeen;
        this.orderTimeDeparture = orderTimeDeparture;
        this.oderTimeEnd = oderTimeEnd;
        this.orderTimeScheduled = orderTimeScheduled;
        this.orderServiceType = orderServiceType;
        this.orderAccountName = orderAccountName;
        this.orderContactName = orderContactName;
        this.orderAddress = orderAddress;
        this.orderSubject = orderSubject;
        this.orderNotice = orderNotice;
        this.orderPaymentMethod = orderPaymentMethod;
        this.orderStatus = orderStatus;
        this.orderType = orderType;
        this.orderPriority = orderPriority;
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
        MEASURED("Medido"),
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

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderUserId() {
        return orderUserId;
    }

    public void setOrderUserId(String orderUserId) {
        this.orderUserId = orderUserId;
    }

    public String getOrderTimeAssignment() {
        return orderTimeAssignment;
    }

    public void setOrderTimeAssignment(String orderTimeAssignment) {
        this.orderTimeAssignment = orderTimeAssignment;
    }

    public String getOrderTimeSeen() {
        return orderTimeSeen;
    }

    public void setOrderTimeSeen(String orderTimeSeen) {
        this.orderTimeSeen = orderTimeSeen;
    }

    public String getOrderTimeDeparture() {
        return orderTimeDeparture;
    }

    public void setOrderTimeDeparture(String orderTimeDeparture) {
        this.orderTimeDeparture = orderTimeDeparture;
    }

    public caseStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(caseStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public casePriority getOrderPriority() {
        return orderPriority;
    }

    public void setOrderPriority(casePriority orderPriority) {
        this.orderPriority = orderPriority;
    }

    public String getOrderAccountName() {
        return orderAccountName;
    }

    public void setOrderAccountName(String orderAccountName) {
        this.orderAccountName = orderAccountName;
    }

    public String getOrderAddress() {
        return orderAddress;
    }

    public void setOrderAddress(String orderAddress) {
        this.orderAddress = orderAddress;
    }

    public caseTypes getOrderType() {
        return orderType;
    }

    public void setOrderType(caseTypes orderType) {
        this.orderType = orderType;
    }

    public String getOrderTimeScheduled() {
        return orderTimeScheduled;
    }

    public void setOrderTimeScheduled(String orderTimeScheduled) {
        this.orderTimeScheduled = orderTimeScheduled;
    }

    public String getOrderServiceType() {
        return orderServiceType;
    }

    public void setOrderServiceType(String orderServiceType) {
        this.orderServiceType = orderServiceType;
    }

    public String getOrderContactName() {
        return orderContactName;
    }

    public void setOrderContactName(String orderContactName) {
        this.orderContactName = orderContactName;
    }

    public String getOrderSubject() {
        return orderSubject;
    }

    public void setOrderSubject(String orderSubject) {
        this.orderSubject = orderSubject;
    }

    public String getOrderPaymentMethod() {
        return orderPaymentMethod;
    }

    public void setOrderPaymentMethod(String orderPaymentMethod) {
        this.orderPaymentMethod = orderPaymentMethod;
    }

    public String getOrderNotice() {
        return orderNotice;
    }

    public void setOrderNotice(String orderNotice) {
        this.orderNotice = orderNotice;
    }

    public String getOderTimeEnd() {
        return oderTimeEnd;
    }

    public void setOderTimeEnd(String oderTimeEnd) {
        this.oderTimeEnd = oderTimeEnd;
    }
}
