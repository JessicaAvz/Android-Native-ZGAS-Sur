package com.zgas.tesselar.myzuite.Controller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.zgas.tesselar.myzuite.Model.Login;
import com.zgas.tesselar.myzuite.R;
import com.zgas.tesselar.myzuite.Utilities.ExtrasHelper;
import com.zgas.tesselar.myzuite.Utilities.UrlHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Formatter;

/**
 * Created by jarvizu on 23/10/2017.
 */

public class RefreshTokenTask extends AsyncTask<URL, JSONObject, JSONObject> {

    private static final String DEBUG_TAG = "RefreshTokenTask";
    private static final String METHOD = "POST";
    private static final String JSON_OBJECT_ERROR = "error";

    private Context context;
    private JSONObject params;
    private RefreshTokenListener refreshTokenListener;
    private UserPreferences userPreferences;
    private String adminToken;
    private boolean isError = false;

    /**
     * @param context
     * @param params
     */
    public RefreshTokenTask(Context context, JSONObject params) {
        this.context = context;
        this.params = params;
        userPreferences = new UserPreferences(context);
    }

    /**
     * @param urls
     * @return
     */
    @Override
    protected JSONObject doInBackground(URL... urls) {
        JSONObject jsonObject = null;

        try {

            Formatter formatter_admin = new Formatter();
            String format_admin = formatter_admin.format(UrlHelper.LOGIN_URL, UrlHelper.GRANT_TYPE, UrlHelper.CLIENT_ID, UrlHelper.CLIENT_SECRET, UrlHelper.ADMIN_EMAIL, UrlHelper.ADMIN_PASS).toString();
            Log.d(DEBUG_TAG, format_admin);
            URL url = new URL(format_admin);
            ConnectionController connection = new ConnectionController(null, url, METHOD);
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
        }

        return jsonObject;
    }

    /**
     * @param jsonObject
     */
    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        super.onPostExecute(jsonObject);
        Log.d(DEBUG_TAG, jsonObject.toString());
        Login login = null;

        try {
            if (jsonObject == null) {
                refreshTokenListener.refreshErrorResponse(context.getResources().getString(R.string.login_error));
                isError = true;
            } else if (jsonObject.has(JSON_OBJECT_ERROR)) {
                Log.d(DEBUG_TAG, "Error: " + jsonObject.get(JSON_OBJECT_ERROR).toString());
                if (jsonObject.get(JSON_OBJECT_ERROR).toString().equals("400")) {
                    refreshTokenListener.refreshErrorResponse(context.getResources().getString(R.string.login_error));
                    isError = true;
                }
            } else if (jsonObject.has(ExtrasHelper.LOGIN_JSON_OBJECT_TOKEN)) {
                login = new Login();
                login.setLoginEmail(params.get(ExtrasHelper.LOGIN_JSON_OBJECT_EMAIL).toString());
                login.setLoginAccessToken(jsonObject.get(ExtrasHelper.LOGIN_JSON_OBJECT_TOKEN).toString());
                login.setLoginId(jsonObject.get(ExtrasHelper.LOGIN_JSON_OBJECT_ID).toString());
                login.setLoginInstanceUrl(jsonObject.get(ExtrasHelper.LOGIN_JSON_OBJECT_INSTANCE).toString());
                login.setLoginIssuedAt(jsonObject.get(ExtrasHelper.LOGIN_JSON_OBJECT_ISSUED_AT).toString());
                login.setLoginTokenType(jsonObject.get(ExtrasHelper.LOGIN_JSON_OBJECT_TOKEN_TYPE).toString());
                login.setLoginSignature(jsonObject.get(ExtrasHelper.LOGIN_JSON_OBJECT_SIGNATURE).toString());
                isError = false;
            }

            if (isError == false) {
                refreshTokenListener.refreshSuccessResponse(login);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    /**
     *
     */
    @Override
    protected void onCancelled() {
        super.onCancelled();
        refreshTokenListener.refreshErrorResponse(context.getResources().getString(R.string.connection_error));
    }

    /**
     * @param refreshTokenListener
     */
    public void setRefreshTokenListener(RefreshTokenListener refreshTokenListener) {
        this.refreshTokenListener = refreshTokenListener;
    }

    /**
     *
     */
    public interface RefreshTokenListener {
        void refreshErrorResponse(String error);

        void refreshSuccessResponse(Login login);
    }
}
