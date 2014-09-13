package com.thm.unicap.app.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SyncResult;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.activeandroid.ActiveAndroid;
import com.thm.unicap.app.MainActivity;
import com.thm.unicap.app.R;
import com.thm.unicap.app.UnicapApplication;
import com.thm.unicap.app.connection.UnicapRequest;
import com.thm.unicap.app.connection.UnicapRequestException;
import com.thm.unicap.app.grade.GradesActivity;
import com.thm.unicap.app.model.SubjectTest;

import java.util.List;

public class UnicapSyncAdapter extends AbstractThreadedSyncAdapter {
    private AccountManager mAccountManager;

    public UnicapSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mAccountManager = AccountManager.get(getContext());
    }

    public UnicapSyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        mAccountManager = AccountManager.get(getContext());
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {

        UnicapApplication.setIsSyncing(true);

        boolean successful = false;

        Intent startIntent = new Intent();
        startIntent.setAction(UnicapSyncReceiver.SYNC_ACTION);
        startIntent.putExtra(UnicapSyncReceiver.SYNC_ACCOUNT_PARAM, account.name);
        startIntent.putExtra(UnicapSyncReceiver.SYNC_STATUS_PARAM, UnicapSyncReceiver.SYNC_STATUS_STARTED);
        getContext().sendBroadcast(startIntent);

        Intent resultIntent = new Intent();
        resultIntent.setAction(UnicapSyncReceiver.SYNC_ACTION);
        resultIntent.putExtra(UnicapSyncReceiver.SYNC_ACCOUNT_PARAM, account.name);

        ActiveAndroid.beginTransaction();
        try {
            UnicapApplication.log("SYNC - 1/7 [====>                              ] - loginRequest");
            UnicapRequest.loginRequest(account.name, mAccountManager.getPassword(account));
            UnicapApplication.log("SYNC - 2/7 [=========>                         ] - receivePersonalData");
            UnicapRequest.receivePersonalData();
            UnicapApplication.log("SYNC - 3/7 [==============>                    ] - receivePastSubjectsData");
            UnicapRequest.receivePastSubjectsData();
            UnicapApplication.log("SYNC - 4/7 [===================>               ] - receiveActualSubjectsData");
            UnicapRequest.receiveActualSubjectsData();
            UnicapApplication.log("SYNC - 5/7 [========================>          ] - receivePendingSubjectsData");
            UnicapRequest.receivePendingSubjectsData();
            UnicapApplication.log("SYNC - 6/7 [=============================>     ] - receiveSubjectsCalendarData");
            UnicapRequest.receiveSubjectsCalendarData();
            UnicapApplication.log("SYNC - 7/7 [===================================] - receiveSubjectsGradesData");
            UnicapRequest.receiveSubjectsGradesData();

            ActiveAndroid.setTransactionSuccessful();
            successful = true;

            resultIntent.putExtra(UnicapSyncReceiver.SYNC_STATUS_PARAM, UnicapSyncReceiver.SYNC_STATUS_OK);

        } catch (UnicapRequestException e) {

            UnicapApplication.error("SYNC - " + e.getMessageFromContext(getContext()));

            syncResult.stats.numIoExceptions++;

            resultIntent.putExtra(UnicapSyncReceiver.SYNC_STATUS_PARAM, UnicapSyncReceiver.SYNC_STATUS_FAIL);
            resultIntent.putExtra(UnicapSyncReceiver.SYNC_MESSAGE_PARAM, e.getMessageFromContext(getContext()));
        } finally {
            ActiveAndroid.endTransaction();
        }

        if(successful) notifyNewGrades();

        getContext().sendBroadcast(resultIntent);
    }

    private void notifyNewGrades() {

        if(UnicapApplication.getCurrentStudent() == null) return;

        List<SubjectTest> newSubjectTests = UnicapApplication.getCurrentStudent().getNewSubjectTests();

        for (SubjectTest subjectTest : newSubjectTests) {

            NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext())
                    .setSmallIcon(R.drawable.ic_stat_unicap)
                    .setContentTitle(subjectTest.subject.name)
                    .setSubText(getContext().getString(R.string.app_name))
                    .setAutoCancel(true);

            switch (subjectTest.degree) {
                case FIRST_DEGREE:
                    builder.setContentText(getContext().getString(R.string.first_degree_available));
                    break;
                case SECOND_DEGREE:
                    builder.setContentText(getContext().getString(R.string.second_degree_available));
                    break;
                case FINAL_DEGREE:
                    builder.setContentText(getContext().getString(R.string.final_degree_available));
                    break;
            }

            // Creates an explicit intent for an Activity in your app
            Intent resultIntent = new Intent(getContext(), GradesActivity.class);
            resultIntent.putExtra("subject_id", subjectTest.subject.getId());

            // The stack builder object will contain an artificial back stack for the
            // started Activity.
            // This ensures that navigating backward from the Activity leads out of
            // your application to the Home screen.
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(getContext());
            // Adds the back stack for the Intent (but not the Intent itself)
            stackBuilder.addParentStack(MainActivity.class);
            // Adds the Intent that starts the Activity to the top of the stack
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(resultPendingIntent);
            NotificationManager mNotificationManager =
                    (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
            // mId allows you to update the notification later on.

            int mId = subjectTest.getId().intValue();
            mNotificationManager.notify(mId, builder.build());
            subjectTest.notify = false;
            subjectTest.save();
        }
    }
}
