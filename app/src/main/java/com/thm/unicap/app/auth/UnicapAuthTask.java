package com.thm.unicap.app.auth;

import android.os.AsyncTask;
import android.util.Pair;

import com.thm.unicap.app.connection.OnTaskCancelled;
import com.thm.unicap.app.connection.OnTaskCompleted;
import com.thm.unicap.app.connection.UnicapRequest;
import com.thm.unicap.app.connection.UnicapRequestException;
import com.thm.unicap.app.connection.UnicapRequestResult;

public class UnicapAuthTask extends AsyncTask<Void, Pair<Integer, Integer>, UnicapRequestResult> {

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
    protected UnicapRequestResult doInBackground(Void... params) {

        try {
            authToken = UnicapRequest.loginRequest(registration, password);
        } catch (UnicapRequestException e) {
            return new UnicapRequestResult(false, e);
        }

        return new UnicapRequestResult(true);
    }

    @Override
    protected void onPostExecute(UnicapRequestResult result) {

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
