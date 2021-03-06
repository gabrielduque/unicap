package com.thm.unicap.app.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;

import com.activeandroid.ActiveAndroid;
import com.thm.unicap.app.UnicapApplication;
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

        UnicapSyncEvent finalSyncEvent;

        Account currentAccount = UnicapApplication.getCurrentAccount();

        if (currentAccount != null && currentAccount.name.equals(account.name)) {
            UnicapApplication.bus.post(new UnicapSyncEvent(UnicapSyncEvent.EventType.SYNC_STARTED));
        }

        ActiveAndroid.beginTransaction();
        //TODO: solve problem related to cleaning up data - (cleaning breaks notification and not cleaning breaks period transition)
//        UnicapDataManager.cleanUserData(account.name);
        try {
            UnicapApplication.log("SYNC - " + account.name + " - 1/7 [====>                              ] - loginRequest");
            UnicapRequest.loginRequest(new StudentCredentials(account.name, mAccountManager.getPassword(account)));
            UnicapApplication.log("SYNC - " + account.name + " - 2/7 [=========>                         ] - receivePersonalData");
            UnicapRequest.receivePersonalData();
            UnicapApplication.log("SYNC - " + account.name + " - 3/7 [==============>                    ] - receivePastSubjectsData");
            UnicapRequest.receivePastSubjectsData();
            UnicapApplication.log("SYNC - " + account.name + " - 4/7 [===================>               ] - receiveActualSubjectsData");
            UnicapRequest.receiveActualSubjectsData();
            UnicapApplication.log("SYNC - " + account.name + " - 5/7 [========================>          ] - receivePendingSubjectsData");
            UnicapRequest.receivePendingSubjectsData();
            UnicapApplication.log("SYNC - " + account.name + " - 6/7 [=============================>     ] - receiveSubjectsCalendarData");
            UnicapRequest.receiveSubjectsCalendarData();
            UnicapApplication.log("SYNC - " + account.name + " - 7/7 [===================================] - receiveSubjectsGradesData");
            UnicapRequest.receiveSubjectsGradesData();

            ActiveAndroid.setTransactionSuccessful();
            successful = true;

            finalSyncEvent = new UnicapSyncEvent(UnicapSyncEvent.EventType.SYNC_COMPLETED);

        } catch (UnicapRequestException e) {

            UnicapApplication.error("SYNC - " + e.getMessageFromContext(getContext()));

            syncResult.stats.numIoExceptions++;

            finalSyncEvent = new UnicapSyncEvent(UnicapSyncEvent.EventType.SYNC_FAILED, e.getMessageFromContext(getContext()));
        } finally {
            ActiveAndroid.endTransaction();
        }

        if (successful) UnicapNotification.notifyNewGrades(getContext());

        if (currentAccount != null && currentAccount.name.equals(account.name)) {
            UnicapApplication.bus.post(finalSyncEvent);
        }
    }
}
