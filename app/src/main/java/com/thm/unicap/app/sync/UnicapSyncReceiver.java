package com.thm.unicap.app.sync;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.activeandroid.query.Select;
import com.thm.unicap.app.UnicapApplication;
import com.thm.unicap.app.model.Student;

public class UnicapSyncReceiver extends BroadcastReceiver {

    public static final String SYNC_ACTION = "com.thm.unicap.app.sync";
    public static final String SYNC_STATUS_PARAM = "sync_status";
    public static final String SYNC_MESSAGE_PARAM = "sync_message";
    public static final String SYNC_STATUS_OK = "ok";
    public static final String SYNC_STATUS_FAIL = "fail";
    public static final String SYNC_STATUS_STARTED = "started";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();

        if(extras != null) {
            String sync_status = extras.getString(SYNC_STATUS_PARAM);

            if(sync_status != null) {
                if(sync_status.equals(SYNC_STATUS_OK) && UnicapApplication.getCurrentAccount() != null) {

                    Student student = new Select().from(Student.class).where("Student.Registration = ?", UnicapApplication.getCurrentAccount().name).executeSingle();

                    if(student != null) {
                        UnicapApplication.setCurrentStudent(student);
                        UnicapApplication.notifyDatabaseUpdated();
                    }
                } else if(sync_status.equals(SYNC_STATUS_FAIL)) {

                    String sync_message = extras.getString(SYNC_MESSAGE_PARAM);

                    UnicapApplication.notifyDatabaseUnreachable(sync_message);
                } else if(sync_status.equals(SYNC_STATUS_STARTED)) {
                    UnicapApplication.notifyDatabaseSyncing();
                }
            }
        }
    }
}
