package com.zgas.tesselar.myzuite.Utilities;

/**
 * Created by jarvizu on 19/10/2017.
 */

public class ExtrasHelper {

    //Login Object
    public static final String LOGIN_JSON_OBJECT_TOKEN = "access_token";
    public static final String LOGIN_JSON_OBJECT_ID = "id";
    public static final String LOGIN_JSON_OBJECT_INSTANCE = "instance_url";
    public static final String LOGIN_JSON_OBJECT_TOKEN_TYPE = "token_type";
    public static final String LOGIN_JSON_OBJECT_ISSUED_AT = "issued_at";
    public static final String LOGIN_JSON_OBJECT_SIGNATURE = "signature";
    public static final String LOGIN_JSON_OBJECT_EMAIL = "email";
    public static final String LOGIN_JSON_OBJECT_PASSWORD = "password";
    public static final String EMAIL_TAG = "email";
    public static final String ADMIN_TOKEN = "access_token";

    //OrdersObject
    public static final String ORDER_JSON_OBJECT_ID = "Id";
    /*Hora en la que se asigna el pedido en SF*/
    public static final String ORDER_JSON_OBJECT_TIME_ASSIGNMENT = "DateTimeAssignment";
    public static final String ORDER_JSON_OBJECT_TIME_SEEN = "CaseTimeSeen";
    public static final String ORDER_JSON_OBJECT_TIME_ARRIVAL = "CaseTimeArrival";
    /*Hora en que se programa el pedido. Si el cliente no especifica una hora, es al mismo tiempo que DateTimeAssignment*/
    public static final String ORDER_JSON_OBJECT_TIME_SCHEDULED = "ScheduledDate";
    public static final String ORDER_JSON_OBJECT_STATUS = "Status";
    public static final String ORDER_JSON_OBJECT_PRIORITY = "Priority";
    public static final String ORDER_JSON_OBJECT_USER_NAME = "AccountName";
    /*Tipo de servicio: estacionario, cilindro...*/
    public static final String ORDER_JSON_OBJECT_SERVICE_TYPE = "ServiceType";
    public static final String ORDER_JSON_OBJECT_ADDRESS = "Address";
    public static final String ORDER_JSON_OBJECT_TYPE = "RecordTypeName";
    public static final String ORDER_JSON_OBJECT_ACCOUNT_NAME = "AccountName";
    public static final String ORDER_JSON_OBJECT_CONTACT_NAME = "ContactName";
    public static final String ORDER_JSON_OBJECT_PAYMENT_METHOD = "PaymentMethod";
    public static final String ORDER_JSON_OBJECT_RECORD_TYPE = "RecordTypeName";

    //UserObject
    public static final String EXTRA_USER_ID = "Id";
    public static final String EXTRA_USER_NAME = "Name";
    public static final String EXTRA_USER_EMAIL = "Email";
    public static final String EXTRA_USER_TYPE = "Position";
    public static final String EXTRA_USER_STATUS = "Status";
    public static final String EXTRA_USER_ROUTE = "Route";
    public static final String EXTRA_USER_ZONE = "Zone";

    //LeakObject
    public static final String LEAK_JSON_OBJECT_ID = "Id";
    public static final String LEAK_JSON_OBJECT_WHO_REPORTS = "WhoReports";
    public static final String LEAK_JSON_OBJECT_SERVICE_TYPE = "RecordTypeName";
    public static final String LEAK_JSON_OBJECT_SUBJECT = "Subject";
    public static final String LEAK_JSON_OBJECT_STATUS = "Status";
    public static final String LEAK_JSON_OBJECT_PRIORITY = "Priority";
    public static final String LEAK_JSON_OBJECT_OPERATOR = "Operator";
    public static final String LEAK_JSON_OBJECT_SALES_NOTE = "FolioSalesNote";
    public static final String LEAK_JSON_OBJECT_DATE_TECHNICIAN = "DateTimeTechnician";
    public static final String LEAK_JSON_OBJECT_DATE_SCHEDULED = "DateTimeScheduled";
    public static final String LEAK_JSON_OBJECT_DATE_END = "DateTimeEnd";
    public static final String LEAK_JSON_OBJECT_SEEN = "TimeSeen";
    public static final String LEAK_JSON_OBJECT_DATE_DEPARTURE = "DateTimeDeparture";
    public static final String LEAK_JSON_OBJECT_CYLINDER_CAPACITY = "CylinderCapacity";
    public static final String LEAK_JSON_OBJECT_COLOR = "Color";
    public static final String LEAK_JSON_OBJECT_CHANNEL = "Chanel";
    public static final String LEAK_JSON_OBJECT_ADDRESS = "Address";
}
