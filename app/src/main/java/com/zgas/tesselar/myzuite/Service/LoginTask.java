package com.zgas.tesselar.myzuite.Service;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.zgas.tesselar.myzuite.Model.Login;
import com.zgas.tesselar.myzuite.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Formatter;

/**
 * Created by jarvizu on 25/09/2017.
 */

public class LoginTask extends AsyncTask<URL, JSONObject, JSONObject> {

    private static final String DEBUG_TAG = "LoginTask";
    private static final String JSON_OBJETC_TOKEN = "access_token";
    private static final String JSON_OBJECT_ID = "id";
    private static final String JSON_OBJECT_INSTANCE = "instance_url";
    private static final String JSON_OBJECT_TOKEN_TYPE = "token_type";
    private static final String JSON_OBJECT_ISSUED_AT = "issued_at";
    private static final String JSON_OBJECT_SIGNATURE = "signature";
    private static final String JSON_OBJECT_EMAIL = "email";
    private static final String JSON_OBJECT_PASSWORD = "password";

    private Context context;
    private JSONObject params;
    private LoginTaskListener loginTaskListener;
    private boolean isError = false;

    public LoginTask(Context context, JSONObject params) {
        this.context = context;
        this.params = params;
    }

    @Override
    protected JSONObject doInBackground(URL... urls) {
        JSONObject jsonObject = null;

        try {

            String preUrl = "https://test.salesforce.com/services/oauth2/token?grant_type=%1$s&client_id=%2$s&client_secret=%3$s&username=%4$s&password=%5$s";
            Formatter formatter = new Formatter();
            String format = formatter.format(preUrl, "password", "3MVG9Yb5IgqnkB4rDrl.nCuWZCFro49RPeNHPvoEZPXLlDMohYAWKqjwyclFpyDIbQ8umQ6qrv6wqps7rl003",
                    "631836681953146126", params.get(JSON_OBJECT_EMAIL), params.get(JSON_OBJECT_PASSWORD)).toString();

            Log.d(DEBUG_TAG, format);

            //URL url = new URL("https://test.salesforce.com/services/oauth2/token?grant_type=password&client_id=3MVG9Yb5IgqnkB4rDrl.nCuWZCFro49RPeNHPvoEZPXLlDMohYAWKqjwyclFpyDIbQ8umQ6qrv6wqps7rl003&client_secret=631836681953146126&username=mbravo@grupozeta.biz.dev1&password=sfgrupozeta16");
            URL url = new URL(format);
            ConnectionController connection = new ConnectionController(url, "POST");
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
        Log.d(DEBUG_TAG, jsonObject.toString());
        Login login = null;

        try {
            if (jsonObject == null) {
                loginTaskListener.loginErrorResponse(context.getResources().getString(R.string.login_error));
                isError = true;

            } else if (jsonObject.has(JSON_OBJETC_TOKEN)) {

                login = new Login();
                login.setLoginAccessToken(jsonObject.get(JSON_OBJETC_TOKEN).toString());
                login.setLoginId(jsonObject.get(JSON_OBJECT_ID).toString());
                login.setLoginInstanceUrl(jsonObject.get(JSON_OBJECT_INSTANCE).toString());
                login.setLoginIssuedAt(jsonObject.get(JSON_OBJECT_ISSUED_AT).toString());
                login.setLoginTokenType(jsonObject.get(JSON_OBJECT_TOKEN_TYPE).toString());
                login.setLoginSignature(jsonObject.get(JSON_OBJECT_SIGNATURE).toString());
                isError = false;
            }

            if (isError == false) {
                loginTaskListener.loginSuccessResponse(login);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        loginTaskListener.loginErrorResponse(context.getResources().getString(R.string.connection_error));
    }

    public void setLoginTaskListener(LoginTaskListener loginTaskListener) {
        this.loginTaskListener = loginTaskListener;
    }

    public interface LoginTaskListener {
        void loginErrorResponse(String error);

        void loginSuccessResponse(Login login);
    }
}
