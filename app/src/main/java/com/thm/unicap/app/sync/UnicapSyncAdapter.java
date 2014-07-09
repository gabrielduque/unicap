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
import android.util.Log;

import com.thm.unicap.app.MainActivity;
import com.thm.unicap.app.R;
import com.thm.unicap.app.UnicapApplication;
import com.thm.unicap.app.connection.UnicapRequest;
import com.thm.unicap.app.connection.UnicapRequestException;
import com.thm.unicap.app.grade.GradesActivity;
import com.thm.unicap.app.model.SubjectTest;

import java.util.List;

public class UnicapSyncAdapter extends AbstractThreadedSyncAdapter {
    private final ContentResolver mContentResolver;
    private AccountManager mAccountManager;

    public UnicapSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mContentResolver = context.getContentResolver();
        mAccountManager = AccountManager.get(getContext());
    }

    public UnicapSyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        mContentResolver = context.getContentResolver();
        mAccountManager = AccountManager.get(getContext());
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        try {

            Log.d("UNICAP", "SYNC - Begin");
            Log.d("UNICAP", "SYNC - loginRequest");
            UnicapRequest.loginRequest(account.name, mAccountManager.getPassword(account));
            Log.d("UNICAP", "SYNC - receivePersonalData");
            UnicapRequest.receivePersonalData();
            Log.d("UNICAP", "SYNC - receivePastSubjectsData");
            UnicapRequest.receivePastSubjectsData();
            Log.d("UNICAP", "SYNC - receiveActualSubjectsData");
            UnicapRequest.receiveActualSubjectsData();
            Log.d("UNICAP", "SYNC - receivePendingSubjectsData");
            UnicapRequest.receivePendingSubjectsData();
            Log.d("UNICAP", "SYNC - receiveSubjectsCalendarData");
            UnicapRequest.receiveSubjectsCalendarData();
            Log.d("UNICAP", "SYNC - receiveSubjectsGradesData");
            UnicapRequest.receiveSubjectsGradesData();
            Log.d("UNICAP", "SYNC - Finish");

            notifyNewGrades();

        } catch (UnicapRequestException e) {
            Log.e("UNICAP", "SYNC - "+e.getMessageFromContext(getContext()));
        }
    }

    private void notifyNewGrades() {
        List<SubjectTest> newSubjectTests = UnicapApplication.getCurrentStudent().getNewSubjectTests();

        for (SubjectTest subjectTest : newSubjectTests) {

            NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext())
                    .setSmallIcon(R.drawable.ic_stat_unicap)
                    .setContentTitle(subjectTest.subject.name)
                    .setSubText(getContext().getString(R.string.app_name))
                    .setAutoCancel(true);

            switch (subjectTest.degree) {
                case FIRST_DEGREE:
                    builder.setContentText("Primeiro GQ disponível");
                    break;
                case SECOND_DEGREE:
                    builder.setContentText("Segundo GQ disponível");
                    break;
                case FINAL_DEGREE:
                    builder.setContentText("Avaliação final disponível");
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
