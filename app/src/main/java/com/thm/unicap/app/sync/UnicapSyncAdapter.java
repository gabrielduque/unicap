package com.thm.unicap.app.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SyncResult;
import android.os.Bundle;

import com.activeandroid.ActiveAndroid;
import com.thm.unicap.app.UnicapApplication;
import com.thm.unicap.app.auth.AccountGeneral;
import com.thm.unicap.app.auth.StudentCredentials;
import com.thm.unicap.app.connection.UnicapDataManager;
import com.thm.unicap.app.connection.UnicapRequest;
import com.thm.unicap.app.connection.UnicapRequestException;
import com.thm.unicap.app.notification.UnicapNotification;

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
        UnicapDataManager.cleanUserData(account.name);
        try {
            UnicapApplication.log("SYNC - 1/7 [====>                              ] - loginRequest");
            UnicapRequest.loginRequest(new StudentCredentials(account.name, mAccountManager.getPassword(account)));
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

        if(successful) UnicapNotification.notifyNewGrades(getContext());

        getContext().sendBroadcast(resultIntent);
    }
}
