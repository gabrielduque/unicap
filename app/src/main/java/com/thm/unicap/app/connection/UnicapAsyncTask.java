package com.thm.unicap.app.connection;

import android.app.Activity;
import android.os.AsyncTask;

import com.github.johnpersano.supertoasts.SuperActivityToast;
import com.github.johnpersano.supertoasts.SuperToast;
import com.github.johnpersano.supertoasts.util.Style;
import com.thm.unicap.app.R;
import com.thm.unicap.app.model.Student;

public class UnicapAsyncTask extends AsyncTask<Student, Integer, Boolean> {

    private Activity activity;
    private SuperActivityToast superActivityToast;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        if(activity != null) {
            superActivityToast = new SuperActivityToast(activity, SuperToast.Type.PROGRESS_HORIZONTAL);
            superActivityToast.setText(activity.getString(R.string.synchronizing));
            superActivityToast.setIndeterminate(true);
            superActivityToast.setProgress(0);
            superActivityToast.show();
        }
    }

    @Override
    protected Boolean doInBackground(Student... students) {

        Student student = students[0];

        try {
            UnicapSync.loginRequest(student.registration, student.password);
            publishProgress(15);
            UnicapSync.receivePersonalData();
            publishProgress(30);
            UnicapSync.receivePastSubjectsData();
            publishProgress(45);
            UnicapSync.receiveActualSubjectsData();
            publishProgress(60);
            UnicapSync.receivePendingSubjectsData();
            publishProgress(75);
            UnicapSync.receiveSubjectsCalendarData();
            publishProgress(90);
            UnicapSync.receiveSubjectsGradesData();
            publishProgress(100);

        } catch (UnicapSyncException e) {
            return false;
        }

        return true;
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        if(superActivityToast != null)
            superActivityToast.setProgress(progress[0]);
    }

    @Override
    protected void onPostExecute(Boolean success) {
        if(superActivityToast != null)
            superActivityToast.dismiss();

        if(!success && activity != null) {
            superActivityToast = new SuperActivityToast(activity, Style.getStyle(Style.RED));
            superActivityToast.setText(activity.getString(R.string.error_generic_message));
            superActivityToast.setDuration(SuperToast.Duration.EXTRA_LONG);
            superActivityToast.setIcon(R.drawable.ic_action_warning, SuperToast.IconPosition.LEFT);
            superActivityToast.show();
        }
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        if(superActivityToast != null)
            superActivityToast.dismiss();
    }

    public UnicapAsyncTask setActivity(Activity activity) {
        this.activity = activity;
        return this;
    }
}
