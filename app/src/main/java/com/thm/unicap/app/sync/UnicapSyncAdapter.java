package com.thm.unicap.app.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

import com.activeandroid.Model;
import com.activeandroid.query.Select;
import com.thm.unicap.app.UnicapApplication;
import com.thm.unicap.app.connection.UnicapSync;
import com.thm.unicap.app.connection.UnicapSyncException;
import com.thm.unicap.app.model.Student;

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
            UnicapSync.loginRequest(account.name, mAccountManager.getPassword(account));
            Log.d("UNICAP", "SYNC - receivePersonalData");
            UnicapSync.receivePersonalData();
            Log.d("UNICAP", "SYNC - receivePastSubjectsData");
            UnicapSync.receivePastSubjectsData();
            Log.d("UNICAP", "SYNC - receiveActualSubjectsData");
            UnicapSync.receiveActualSubjectsData();
            Log.d("UNICAP", "SYNC - receivePendingSubjectsData");
            UnicapSync.receivePendingSubjectsData();
            Log.d("UNICAP", "SYNC - receiveSubjectsCalendarData");
            UnicapSync.receiveSubjectsCalendarData();
            Log.d("UNICAP", "SYNC - receiveSubjectsGradesData");
            UnicapSync.receiveSubjectsGradesData();
            Log.d("UNICAP", "SYNC - Finish");

        } catch (UnicapSyncException e) {
            Log.e("UNICAP", "SYNC - "+e.getMessageFromContext(getContext()));
        }
    }
}
