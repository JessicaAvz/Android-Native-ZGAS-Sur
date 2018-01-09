package com.zgas.tesselar.myzuite.Service;

import android.content.Context;
import android.os.AsyncTask;

import com.zgas.tesselar.myzuite.Model.Incidence;
import com.zgas.tesselar.myzuite.R;

import org.json.JSONObject;

import java.net.URL;

/**
 * Created by jarvizu on 04/01/2018.
 */

public class ReportIncidenceTask extends AsyncTask<URL, JSONObject, JSONObject> {

    private static final String DEBUG_TAG = "ReportIncidenceTask";
    private static final String METHOD = "POST";
    private static final String JSON_OBJECT_ERROR = "error";

    private Context context;
    private JSONObject params;
    private ReportIncidenceListener reportIncidenceListener;
    private UserPreferences userPreferences;
    private String adminToken;
    private boolean isError = false;

    /**
     * @param context
     * @param params
     */
    public ReportIncidenceTask(Context context, JSONObject params) {
        this.context = context;
        this.params = params;
        userPreferences = new UserPreferences(context);
    }

    /**
     *
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    /**
     * @param urls
     * @return
     */
    @Override
    protected JSONObject doInBackground(URL... urls) {
        return null;
    }

    /**
     * @param jsonObject
     */
    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        super.onPostExecute(jsonObject);
    }

    /**
     *
     */
    @Override
    protected void onCancelled() {
        super.onCancelled();
        reportIncidenceListener.reportErrorResponse(context.getResources().getString(R.string.connection_error));
    }

    /**
     * @param reportIncidenceListener
     */
    public void setReportIncidenceListener(ReportIncidenceListener reportIncidenceListener) {
        this.reportIncidenceListener = reportIncidenceListener;
    }

    /**
     *
     */
    public interface ReportIncidenceListener {
        void reportErrorResponse(String error);

        void reportSuccessResponse(Incidence incidence);
    }
}
