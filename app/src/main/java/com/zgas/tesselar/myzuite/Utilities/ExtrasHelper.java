package com.zgas.tesselar.myzuite.Utilities;

/**
 * Created by jarvizu on 19/10/2017.
 */

public class ExtrasHelper {

    public static final String LOGIN_JSON_OBJECT_TOKEN = "access_token";
    public static final String LOGIN_JSON_OBJECT_ID = "id";
    public static final String LOGIN_JSON_OBJECT_INSTANCE = "instance_url";
    public static final String LOGIN_JSON_OBJECT_TOKEN_TYPE = "token_type";
    public static final String LOGIN_JSON_OBJECT_ISSUED_AT = "issued_at";
    public static final String LOGIN_JSON_OBJECT_SIGNATURE = "signature";
    public static final String LOGIN_JSON_OBJECT_EMAIL = "email";
    public static final String LOGIN_JSON_OBJECT_PASSWORD = "password";

    public static final String EXTRA_JSON_OBJECT_ID = "Id";
    /*Hora en la que se asigna el pedido en SF*/
    public static final String EXTRA_JSON_OBJECT_TIME_ASSIGNMENT = "DateTimeAssignment";
    public static final String EXTRA_JSON_OBJECT_TIME_SEEN = "CaseTimeSeen";
    public static final String EXTRA_JSON_OBJECT_TIME_ARRIVAL = "CaseTimeArrival";
    /*Hora en que se programa el pedido. Si el cliente no especifica una hora, es al mismo tiempo que DateTimeAssignment*/
    public static final String EXTRA_JSON_OBJECT_TIME_PROGRAMMED = "ScheduledDate";
    public static final String EXTRA_JSON_OBJECT_STATUS = "Status";
    public static final String EXTRA_JSON_OBJECT_PRIORITY = "Priority";
    public static final String EXTRA_JSON_OBJECT_USER_NAME = "AccountName";
    public static final String EXTRA_JSON_OBJECT_SERVICE_TYPE = "ServiceType";
    public static final String EXTRA_JSON_OBJECT_ADDRESS = "Address";
    public static final String EXTRA_JSON_OBJECT_TYPE = "RecordTypeName";
    public static final String EXTRA_JSON_OBJECT_CLIENT_NAME = "AccountName";

    public static final String EXTRA_USER_ID = "Id";
    public static final String EXTRA_USER_NAME = "Name";
    public static final String EXTRA_USER_EMAIL = "Email";
    public static final String EXTRA_USER_TYPE = "Position";
    public static final String EXTRA_USER_STATUS = "Status";
    public static final String EXTRA_USER_ROUTE = "Route";
    public static final String EXTRA_USER_ZONE = "Zone";

    public static final String EMAIL_TAG = "email";
    public static final String ADMIN_TOKEN = "access_token";
}
