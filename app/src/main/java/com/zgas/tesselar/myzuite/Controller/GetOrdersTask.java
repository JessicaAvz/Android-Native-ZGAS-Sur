package com.zgas.tesselar.myzuite.Controller;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.zgas.tesselar.myzuite.Model.Case;
import com.zgas.tesselar.myzuite.R;
import com.zgas.tesselar.myzuite.Utilities.ExtrasHelper;
import com.zgas.tesselar.myzuite.Utilities.UrlHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

/**
 * Created by jarvizu on 20/09/2017.
 */

public class GetOrdersTask extends AsyncTask<URL, JSONObject, JSONObject> {

    private static final String DEBUG_TAG = "GetOrdersTask";
    private static final String CASES_ARRAY = "Orders";
    private static final String DETAIL_ARRAY = "Detail";
    private static final String METHOD = "GET";
    private static final String USER_ID = "Id";
    private static final String JSON_OBJECT_ID = "Id";
    private static final String JSON_OBJECT_ERROR = "errorCode";

    private Context context;
    private JSONObject params;
    private UserPreferences userPreferences;
    private OrderTaskListener orderTaskListener;
    private ProgressDialog progressDialog;
    private boolean isError = false;
    private String adminToken;
    private Case aCase;
    private List<Case> casesList;

    public GetOrdersTask(Context context, JSONObject params) {
        this.context = context;
        this.params = params;
        userPreferences = new UserPreferences(context);
    }

    protected void onPreExecute() {
        progressDialog = ProgressDialog.show(context, null, context.getResources().getString(R.string.wait_cases_message), false);
    }

    @Override
    protected JSONObject doInBackground(URL... urls) {
        JSONObject jsonObject = null;
        try {
            Formatter formatter = new Formatter();
            String format = formatter.format(UrlHelper.GET_CASES_URL, params.get(USER_ID)).toString();
            adminToken = userPreferences.getAdminToken();
            Log.d(DEBUG_TAG, "Url del usuario: " + format);
            Log.d(DEBUG_TAG, "Token del admin: " + adminToken);

            URL url = new URL(format);
            ConnectionController connection = new ConnectionController(adminToken, url, METHOD);
            jsonObject = connection.execute();

            if (jsonObject == null) {
                cancel(true);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
            cancel(true);
        } catch (FileNotFoundException e) {
            cancel(true);
            e.printStackTrace();
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
            cancel(true);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        super.onPostExecute(jsonObject);
        progressDialog.dismiss();

        casesList = new ArrayList<>();
        JSONArray casesArray;

        try {
            if (jsonObject == null) {
                orderTaskListener.getCasesErrorResponse(jsonObject.getString(JSON_OBJECT_ERROR));
                isError = true;
            } else if (jsonObject.has(JSON_OBJECT_ERROR)) {
                if (jsonObject.get(JSON_OBJECT_ERROR).toString().equals("400")) {
                    orderTaskListener.getCasesErrorResponse(context.getResources().getString(R.string.cases_data_error));
                    isError = true;
                }
            } else if (jsonObject.has(CASES_ARRAY)) {
                Log.d(DEBUG_TAG, "HOLA, SI SIRVE");
                casesArray = jsonObject.getJSONArray(CASES_ARRAY);

                for (int i = 0; i < casesArray.length(); i++) {
                    JSONObject caseObject = casesArray.getJSONObject(i);
                    aCase = new Case();
                    aCase.setCaseId(caseObject.get(JSON_OBJECT_ID).toString());
                    Log.d(DEBUG_TAG, "Id del caso: " + aCase.getCaseId());
                    aCase.setCaseUserId(userPreferences.getUserObject().getUserId());
                    Log.d(DEBUG_TAG, "Id del operador: " + aCase.getCaseUserId());
                    aCase.setCaseTimeScheduled(caseObject.getString(ExtrasHelper.ORDER_JSON_OBJECT_TIME_SCHEDULED));
                    Log.d(DEBUG_TAG, "Hora programada: " + aCase.getCaseTimeScheduled());
                    //aCase.setCaseTimeSeen(caseObject.getString(CASE_TIME_SEEN));
                    aCase.setCaseTimeAssignment(caseObject.getString(ExtrasHelper.ORDER_JSON_OBJECT_TIME_ASSIGNMENT));
                    Log.d(DEBUG_TAG, "Hora de asignación: " + caseObject.getString(ExtrasHelper.ORDER_JSON_OBJECT_TIME_ASSIGNMENT));
                    aCase.setCaseAccountName(caseObject.getString(ExtrasHelper.ORDER_JSON_OBJECT_ACCOUNT_NAME));
                    Log.d(DEBUG_TAG, "Cliente: " + aCase.getCaseAccountName());
                    aCase.setCaseAddress(caseObject.getString(ExtrasHelper.ORDER_JSON_OBJECT_ADDRESS));
                    Log.d(DEBUG_TAG, "Dirección: " + aCase.getCaseAddress());
                    aCase.setCaseContactName(caseObject.getString(ExtrasHelper.ORDER_JSON_OBJECT_CONTACT_NAME));
                    Log.d(DEBUG_TAG, "Cuenta: " + aCase.getCaseAccountName());
                    aCase.setCaseServiceType(caseObject.getString(ExtrasHelper.ORDER_JSON_OBJECT_SERVICE_TYPE));
                    Log.d(DEBUG_TAG, "Tipo de servicio: " + aCase.getCaseServiceType());

                    String caseType = caseObject.get(ExtrasHelper.ORDER_JSON_OBJECT_TYPE).toString();
                    String caseStatus = caseObject.get(ExtrasHelper.ORDER_JSON_OBJECT_STATUS).toString();
                    String casePriority = caseObject.get(ExtrasHelper.ORDER_JSON_OBJECT_PRIORITY).toString();

                    if (caseType.equals(Case.caseTypes.ORDER.toString())) {
                        aCase.setCaseType(Case.caseTypes.ORDER);
                    } else if (caseType.equals(Case.caseTypes.LEAKAGE.toString())) {
                        aCase.setCaseType(Case.caseTypes.LEAKAGE);
                    } else if (caseType.equals(Case.caseTypes.CUT.toString())) {
                        aCase.setCaseType(Case.caseTypes.CUT);
                    } else if (caseType.equals(Case.caseTypes.RECONNECTION.toString())) {
                        aCase.setCaseType(Case.caseTypes.RECONNECTION);
                    } else if (caseType.equals(Case.caseTypes.CUSTOM_SERVICE.toString())) {
                        aCase.setCaseType(Case.caseTypes.CUSTOM_SERVICE);
                    }
                    Log.d(DEBUG_TAG, "Tipo de caso: " + aCase.getCaseType());

                    if (caseStatus.equals(Case.caseStatus.INPROGRESS.toString())) {
                        aCase.setCaseStatus(Case.caseStatus.INPROGRESS);
                    } else if (caseStatus.equals(Case.caseStatus.CANCELLED.toString())) {
                        aCase.setCaseStatus(Case.caseStatus.CANCELLED);
                    } else if (caseStatus.equals(Case.caseStatus.FINISHED.toString())) {
                        aCase.setCaseStatus(Case.caseStatus.FINISHED);
                    } else if (caseStatus.equals(Case.caseStatus.NEW.toString())) {
                        aCase.setCaseStatus(Case.caseStatus.NEW);
                    } else if (caseStatus.equals(Case.caseStatus.ASSIGNED.toString())) {
                        aCase.setCaseStatus(Case.caseStatus.ASSIGNED);
                    } else if (caseStatus.equals(Case.caseStatus.ACCEPTED.toString())) {
                        aCase.setCaseStatus(Case.caseStatus.ACCEPTED);
                    } else if (caseStatus.equals(Case.caseStatus.RETIRED.toString())) {
                        aCase.setCaseStatus(Case.caseStatus.RETIRED);
                    }
                    Log.d(DEBUG_TAG, "Status del caso : " + aCase.getCaseStatus());

                    if (casePriority.equals(Case.casePriority.HIGH.toString())) {
                        aCase.setCasePriority(Case.casePriority.HIGH);
                    } else if (casePriority.equals(Case.casePriority.LOW.toString())) {
                        aCase.setCasePriority(Case.casePriority.LOW);
                    } else if (casePriority.equals(Case.casePriority.MEDIUM.toString())) {
                        aCase.setCasePriority(Case.casePriority.MEDIUM);
                    }
                    Log.d(DEBUG_TAG, "Prioridad del caso: " + aCase.getCasePriority());

                    isError = false;
                    casesList.add(aCase);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if (orderTaskListener != null) {
                orderTaskListener.getCasesSuccessResponse(casesList);
            }
        }
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        progressDialog.dismiss();
        orderTaskListener.getCasesErrorResponse(context.getResources().getString(R.string.connection_error));
    }

    public void setOrderTaskListener(OrderTaskListener getOrderTaskListener) {
        this.orderTaskListener = getOrderTaskListener;
    }

    public interface OrderTaskListener {
        void getCasesErrorResponse(String error);

        void getCasesSuccessResponse(List<Case> caseList);
    }
}
