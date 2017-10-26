package com.zgas.tesselar.myzuite.Controller;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.zgas.tesselar.myzuite.Model.Leak;
import com.zgas.tesselar.myzuite.Model.Order;
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
 * Created by jarvizu on 22/09/2017.
 */

public class GetLeakagesTask extends AsyncTask<URL, JSONObject, JSONObject> {

    private static final String DEBUG_TAG = "GetLeakagesTask";
    private static final String CASES_ARRAY = "Leaks";
    private static final String CASE_ERROR = "error";
    private static final String METHOD = "GET";
    private static final String JSON_OBJECT_ERROR = "error";
    private static final String USER_ID = "Id";

    private Context context;
    private JSONObject params;
    private LeakagesTaskListener leakagesTaskListener;
    private UserPreferences userPreferences;
    private String adminToken;
    private Leak aLeak;
    private List<Leak> leaksList;
    private ProgressDialog progressDialog;
    private boolean isError = false;

    public GetLeakagesTask(Context context, JSONObject params) {
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
            String format = formatter.format(UrlHelper.GET_LEAKS_URL, params.get(USER_ID)).toString();
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

        List<Leak> leaksList = new ArrayList<>();
        JSONArray leaksArray;

        try {
            if (jsonObject == null) {
                leakagesTaskListener.getLeakagesErrorResponse(jsonObject.getString(JSON_OBJECT_ERROR));
                isError = true;
            } else if (jsonObject.has(JSON_OBJECT_ERROR)) {
                if (jsonObject.get(JSON_OBJECT_ERROR).toString().equals("400")) {
                    leakagesTaskListener.getLeakagesErrorResponse(context.getResources().getString(R.string.cases_data_error));
                    isError = true;
                }
            } else if (jsonObject.has(CASES_ARRAY)) {
                Log.d(DEBUG_TAG, "HOLA, SI SIRVE");
                leaksArray = jsonObject.getJSONArray(CASES_ARRAY);

                for (int i = 0; i < leaksArray.length(); i++) {
                    JSONObject caseObject = leaksArray.getJSONObject(i);
                    aLeak = new Leak();
                    aLeak.setLeakId(caseObject.getString(ExtrasHelper.LEAK_JSON_OBJECT_ID));
                    Log.d(DEBUG_TAG, "Id del caso: " + aLeak.getLeakId());
                    aLeak.setLeakTimeDeparture(caseObject.getString(ExtrasHelper.LEAK_JSON_OBJECT_DATE_DEPARTURE));
                    Log.d(DEBUG_TAG, "Llegada del caso: " + aLeak.getLeakTimeDeparture());
                    aLeak.setLeakTimeScheduled(caseObject.getString(ExtrasHelper.LEAK_JSON_OBJECT_DATE_SCHEDULED));
                    Log.d(DEBUG_TAG, "Hora programada del caso: " + aLeak.getLeakTimeScheduled());
                    //aLeak.setCaseTimeSeen(caseObject.getString(CASE_TIME_SEEN));
                    aLeak.setLeakTimeAssignment(caseObject.getString(ExtrasHelper.LEAK_JSON_OBJECT_DATE_TECHNICIAN));
                    Log.d(DEBUG_TAG, "Hora de asignación del caso: " + aLeak.getLeakTimeAssignment());
                    aLeak.setLeakTimeEnd(caseObject.getString(ExtrasHelper.LEAK_JSON_OBJECT_DATE_END));
                    Log.d(DEBUG_TAG, "Hora de terminación del caso: " + aLeak.getLeakTimeEnd());
                    aLeak.setLeakAccountName(caseObject.getString(ExtrasHelper.LEAK_JSON_OBJECT_WHO_REPORTS));
                    Log.d(DEBUG_TAG, "Cliente del caso: " + aLeak.getLeakAccountName());
                    aLeak.setLeakAddress(caseObject.getString(ExtrasHelper.LEAK_JSON_OBJECT_ADDRESS));
                    Log.d(DEBUG_TAG, "Dirección del caso: " + aLeak.getLeakAddress());
                    aLeak.setLeakSubject(caseObject.getString(ExtrasHelper.LEAK_JSON_OBJECT_SUBJECT));
                    Log.d(DEBUG_TAG, "Descripción del caso: " + aLeak.getLeakSubject());
                    aLeak.setLeakCylinderCapacity(caseObject.getString(ExtrasHelper.LEAK_JSON_OBJECT_CYLINDER_CAPACITY));
                    Log.d(DEBUG_TAG, "Capacidad: " + aLeak.getLeakCylinderCapacity());
                    aLeak.setLeakCylinderColor(caseObject.getString(ExtrasHelper.LEAK_JSON_OBJECT_COLOR));
                    Log.d(DEBUG_TAG, "Color del cilindro: " + aLeak.getLeakCylinderColor());
                    aLeak.setLeakChannel(caseObject.getString(ExtrasHelper.LEAK_JSON_OBJECT_CHANNEL));
                    Log.d(DEBUG_TAG, "Chanel: " + aLeak.getLeakChannel());
                    aLeak.setLeakFolioSalesNote(caseObject.getString(ExtrasHelper.LEAK_JSON_OBJECT_SALES_NOTE));
                    Log.d(DEBUG_TAG, "Folio: " + aLeak.getLeakFolioSalesNote());

                    //<-------------------------Agregar tipo de fuga --------------------------------------------->
                    String leakType = caseObject.get(ExtrasHelper.LEAK_JSON_OBJECT_SERVICE_TYPE).toString();
                    Log.d(DEBUG_TAG, "CaseType " + leakType);
                    String leakStatus = caseObject.get(ExtrasHelper.LEAK_JSON_OBJECT_STATUS).toString();
                    Log.d(DEBUG_TAG, "CaseStatus " + leakStatus);
                    String leakPriority = caseObject.get(ExtrasHelper.LEAK_JSON_OBJECT_PRIORITY).toString();
                    Log.d(DEBUG_TAG, "CasePriority " + leakPriority);

                    if (leakType.equals(Leak.leakType.ORDER.toString())) {
                        aLeak.setLeakType(Leak.leakType.ORDER);
                    } else if (leakType.equals(Order.caseTypes.LEAKAGE.toString())) {
                        aLeak.setLeakType(Leak.leakType.LEAKAGE);
                    } else if (leakType.equals(Order.caseTypes.CUT.toString())) {
                        aLeak.setLeakType(Leak.leakType.CUT);
                    } else if (leakType.equals(Order.caseTypes.RECONNECTION.toString())) {
                        aLeak.setLeakType(Leak.leakType.RECONNECTION);
                    } else if (leakType.equals(Order.caseTypes.CUSTOM_SERVICE.toString())) {
                        aLeak.setLeakType(Leak.leakType.CUSTOM_SERVICE);
                    }
                    Log.d(DEBUG_TAG, "Tipo del caso: " + aLeak.getLeakType());

                    if (leakStatus.equals(Leak.leakStatus.INPROGRESS.toString())) {
                        aLeak.setLeakStatus(Leak.leakStatus.INPROGRESS);
                    } else if (leakStatus.equals(Leak.leakStatus.CANCELLED.toString())) {
                        aLeak.setLeakStatus(Leak.leakStatus.CANCELLED);
                    } else if (leakStatus.equals(Leak.leakStatus.FINISHED.toString())) {
                        aLeak.setLeakStatus(Leak.leakStatus.FINISHED);
                    } else if (leakStatus.equals(Leak.leakStatus.NEW.toString())) {
                        aLeak.setLeakStatus(Leak.leakStatus.NEW);
                    } else if (leakStatus.equals(Leak.leakStatus.ASSIGNED.toString())) {
                        aLeak.setLeakStatus(Leak.leakStatus.ASSIGNED);
                    } else if (leakStatus.equals(Leak.leakStatus.ACCEPTED.toString())) {
                        aLeak.setLeakStatus(Leak.leakStatus.ACCEPTED);
                    } else if (leakStatus.equals(Leak.leakStatus.RETIRED.toString())) {
                        aLeak.setLeakStatus(Leak.leakStatus.RETIRED);
                    } else if (leakStatus.equals(Leak.leakStatus.CLOSED.toString())) {
                        aLeak.setLeakStatus(Leak.leakStatus.CLOSED);
                    }
                    Log.d(DEBUG_TAG, "Status del caso: " + aLeak.getLeakStatus());

                    if (leakPriority.equals(Leak.leakPriority.HIGH.toString())) {
                        aLeak.setLeakPriority(Leak.leakPriority.HIGH);
                    } else if (leakPriority.equals(Leak.leakPriority.LOW.toString())) {
                        aLeak.setLeakPriority(Leak.leakPriority.LOW);
                    } else if (leakPriority.equals(Leak.leakPriority.MEDIUM.toString())) {
                        aLeak.setLeakPriority(Leak.leakPriority.MEDIUM);
                    }
                    Log.d(DEBUG_TAG, "Prioridad del caso: " + aLeak.getLeakPriority());

                    isError = false;
                    leaksList.add(aLeak);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if (leakagesTaskListener != null) {
                leakagesTaskListener.getLeakagesSuccessResponse(leaksList);
            }
        }
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        progressDialog.dismiss();
        leakagesTaskListener.getLeakagesErrorResponse(context.getResources().getString(R.string.connection_error));
    }

    public void setLeakagesTaskListener(LeakagesTaskListener leakagesTaskListener) {
        this.leakagesTaskListener = leakagesTaskListener;
    }

    public interface LeakagesTaskListener {
        void getLeakagesErrorResponse(String error);

        void getLeakagesSuccessResponse(List<Leak> orderList);
    }
}
