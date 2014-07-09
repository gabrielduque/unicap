package com.thm.unicap.app.auth;

import android.os.AsyncTask;
import android.util.Pair;

import com.thm.unicap.app.connection.OnTaskCancelled;
import com.thm.unicap.app.connection.OnTaskCompleted;
import com.thm.unicap.app.connection.UnicapSync;
import com.thm.unicap.app.connection.UnicapSyncException;
import com.thm.unicap.app.connection.UnicapSyncResult;

public class UnicapAuthTask extends AsyncTask<Void, Pair<Integer, Integer>, UnicapSyncResult> {

    private String registration;
    private String password;
    private String authToken;

    private OnTaskCompleted onTaskCompleted;
    private OnTaskCancelled onTaskCancelled;

    public UnicapAuthTask(String registration, String password) {
        this.registration = registration;
        this.password = password;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected UnicapSyncResult doInBackground(Void... params) {

        try {
            authToken = UnicapSync.loginRequest(registration, password);
        } catch (UnicapSyncException e) {
            return new UnicapSyncResult(false, e);
        }

        return new UnicapSyncResult(true);
    }

    @Override
    protected void onPostExecute(UnicapSyncResult result) {

        if(onTaskCompleted != null)
            onTaskCompleted.onTaskCompleted(result);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();

        if(onTaskCancelled != null)
            onTaskCancelled.onTaskCancelled();
    }

    public void setOnTaskCompletedListener(OnTaskCompleted listener) {
        onTaskCompleted = listener;
    }

    public void setOnTaskCancelledListener(OnTaskCancelled listener) {
        onTaskCancelled = listener;
    }

    public String getRegistration() {
        return registration;
    }

    public String getPassword() {
        return password;
    }

    public String getAuthToken() {
        return authToken;
    }
}
