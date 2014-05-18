package com.thm.unicap.app.connection;

import android.os.AsyncTask;
import android.util.Pair;

import com.thm.unicap.app.R;

public class UnicapSyncTask extends AsyncTask<Void, Pair<Integer, Integer>, UnicapSyncResult> {

    private String registration;
    private String password;

    private OnTaskCompleted onTaskCompleted;
    private OnTaskCancelled onTaskCancelled;
    private OnTaskProgressUpdated onTaskProgressUpdated;

    public UnicapSyncTask(String registration, String password) {
        this.registration = registration;
        this.password = password;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected UnicapSyncResult doInBackground(Void... params) {

        try {
            publishProgress(new Pair<Integer, Integer>(R.string.progress_1, 0));
            UnicapSync.loginRequest(registration, password);
            publishProgress(new Pair<Integer, Integer>(R.string.progress_2, 15));
            UnicapSync.receivePersonalData();
            publishProgress(new Pair<Integer, Integer>(R.string.progress_3, 30));
            UnicapSync.receivePastSubjectsData();
            publishProgress(new Pair<Integer, Integer>(R.string.progress_4, 45));
            UnicapSync.receiveActualSubjectsData();
            publishProgress(new Pair<Integer, Integer>(R.string.progress_5, 60));
            UnicapSync.receivePendingSubjectsData();
            publishProgress(new Pair<Integer, Integer>(R.string.progress_6, 75));
            UnicapSync.receiveSubjectsCalendarData();
            publishProgress(new Pair<Integer, Integer>(R.string.progress_7, 90));
            UnicapSync.receiveSubjectsGradesData();
            publishProgress(new Pair<Integer, Integer>(R.string.progress_8, 100));

        } catch (UnicapSyncException e) {
            return new UnicapSyncResult(false, e);
        }

        return new UnicapSyncResult(true);
    }

    @Override
    protected void onProgressUpdate(Pair<Integer, Integer>... progress) {

        if(onTaskProgressUpdated != null)
            onTaskProgressUpdated.onTaskProgressUpdated(progress[0]);
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

    public void setOnTaskProgressUpdatedListener(OnTaskProgressUpdated listener) {
        onTaskProgressUpdated = listener;
    }

    public String getRegistration() {
        return registration;
    }

    public String getPassword() {
        return password;
    }

}
