package com.zgas.tesselar.myzuite.Service;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.zgas.tesselar.myzuite.Model.User;
import com.zgas.tesselar.myzuite.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jarvizu on 25/09/2017.
 */

public class GetUsersTask extends AsyncTask<URL, JSONObject, JSONObject> {

    private static final String DEBUG_TAG = "GetUsersTask";
    private static final String USERS_ARRAY = "users";
    private static final String USER_ID = "userId";
    private static final String USER_NAME = "userName";
    private static final String USER_LASTNAME = "userLastname";
    private static final String USER_EMAIL = "userEmail";
    private static final String USER_ROUTE = "userRoute";
    private static final String USER_ZONE = "userZone";
    private static final String USER_TYPE = "userType";
    private static final String USER_STATUS = "userStatus";
    private static final String URL = "https://my-json-server.typicode.com/JessicaAvz/jsons/get_users";

    private Context context;
    private JSONObject params;
    private GetUsersTaskListener usersTaskListener;
    private ProgressDialog progressDialog;

    public GetUsersTask(Context context, JSONObject params) {
        this.context = context;
        this.params = params;
    }

    @Override
    protected void onPreExecute() {
        progressDialog = ProgressDialog.show(context, null, context.getResources().getString(R.string.wait_users_message), false);
    }

    @Override
    protected JSONObject doInBackground(URL... urls) {

        JSONObject jsonObject = null;

        try {
            URL url = new URL(URL);
            ConnectionController connection = new ConnectionController(url, "GET", params);
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

    @Override
    protected void onPostExecute(JSONObject jsonObjectResult) {
        super.onPostExecute(jsonObjectResult);
        progressDialog.dismiss();

        List<User> usersList = new ArrayList<>();
        JSONArray usersArray;
        User user;

        try {
            usersArray = jsonObjectResult.getJSONArray(USERS_ARRAY);

            for (int i = 0; i < usersArray.length(); i++) {
                JSONObject userObject = usersArray.getJSONObject(i);
                user = new User();
                user.setUserId(userObject.getInt(USER_ID));
                user.setUserName(userObject.getString(USER_NAME));
                user.setUserLastname(userObject.getString(USER_LASTNAME));
                user.setUserEmail(userObject.getString(USER_EMAIL));
                user.setUserZone(userObject.getString(USER_ZONE));
                user.setUserRoute(userObject.getString(USER_ROUTE));

                String userType = userObject.get(USER_TYPE).toString();
                String userStatus = userObject.get(USER_STATUS).toString();

                if (userType.equals(User.userType.OPERATOR.toString())) {
                    user.setUserType(User.userType.OPERATOR);
                } else if (userType.equals(User.userType.LEAKAGE.toString())) {
                    user.setUserType(User.userType.LEAKAGE);
                } else if (userType.equals(User.userType.SUPERVISOR.toString())) {
                    user.setUserType(User.userType.SUPERVISOR);
                } else if (userType.equals(User.userType.SERVICE.toString())) {
                    user.setUserType(User.userType.SERVICE);
                }

                if (userStatus.equals(User.userStatus.ACTIVE.toString())) {
                    user.setUserstatus(User.userStatus.ACTIVE);
                } else if (userStatus.equals(User.userStatus.NOTACTIVE.toString())) {
                    user.setUserstatus(User.userStatus.NOTACTIVE);
                }

                usersList.add(user);

                Log.d(DEBUG_TAG, "Id del usuario: " + user.getUserId());
                Log.d(DEBUG_TAG, "Tipo de usuario: " + user.getUserType());
                Log.d(DEBUG_TAG, "Nombre del usuario: " + user.getUserName());
                Log.d(DEBUG_TAG, "Apellido del usuario: " + user.getUserLastname());
                Log.d(DEBUG_TAG, "Correo del usuario: " + user.getUserEmail());
                Log.d(DEBUG_TAG, "Ruta del usuario: " + user.getUserRoute());
                Log.d(DEBUG_TAG, "Zona del usuario: " + user.getUserZone());
                Log.d(DEBUG_TAG, "Estatus del usuario: " + user.getUserstatus());

            }
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if (usersTaskListener != null) {
                usersTaskListener.getUsersSuccessResponse(usersList);
            }
        }


    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        progressDialog.dismiss();
        usersTaskListener.getUsersErrorResponse(context.getResources().getString(R.string.connection_error));
    }

    public void setUsersTaskListener(GetUsersTaskListener getUsersTaskListener) {
        this.usersTaskListener = getUsersTaskListener;
    }

    public interface GetUsersTaskListener {
        void getUsersErrorResponse(String error);

        void getUsersSuccessResponse(List<User> userList);
    }
}
