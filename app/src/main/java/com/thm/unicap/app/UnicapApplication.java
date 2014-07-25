package com.thm.unicap.app;

import android.accounts.Account;
import android.app.Application;
import android.content.ContentResolver;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

import com.activeandroid.ActiveAndroid;
import com.thm.unicap.app.model.Student;
import com.thm.unicap.app.sync.UnicapContentProvider;
import com.thm.unicap.app.sync.UnicapSyncReceiver;
import com.thm.unicap.app.util.DatabaseListener;

import java.util.ArrayList;

public class UnicapApplication extends Application {
    public static final String TAG = "Unicap";

    private static Student mCurrentStudent = null;
    private static Account mCurrentAccount = null;
    private static ArrayList<DatabaseListener> mDatabaseListeners;
    private static boolean isSyncing;

    @Override
    public void onCreate() {
        super.onCreate();
        ActiveAndroid.initialize(this);
        mDatabaseListeners = new ArrayList<DatabaseListener>();
        registerReceiver(new UnicapSyncReceiver(), new IntentFilter(UnicapSyncReceiver.SYNC_ACTION));
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        ActiveAndroid.dispose();
    }

    public static Student getCurrentStudent() {
        return mCurrentStudent;
    }

    public static void setCurrentStudent(Student student) {
        UnicapApplication.mCurrentStudent = student;
    }

    public static boolean hasStudentData() {
        return mCurrentStudent != null;
    }

    public static void addDatabaseListener(DatabaseListener listener) {
        if(!mDatabaseListeners.contains(listener))
            mDatabaseListeners.add(listener);
    }

    public static void removeDatabaseListener(DatabaseListener listener) {
        mDatabaseListeners.remove(listener);
    }

    public static void notifyDatabaseSyncing() {
        isSyncing = true;
        for (DatabaseListener listener : mDatabaseListeners) {
            listener.databaseSyncing();
        }
    }

    public static void notifyDatabaseUpdated() {
        isSyncing = false;
        if(mCurrentStudent != null) {
            for (DatabaseListener listener : mDatabaseListeners) {
                listener.databaseUpdated();
            }
        }
    }

    public static void notifyDatabaseUnreachable(String message) {
        isSyncing = false;
        if(mCurrentStudent == null) {
            for (DatabaseListener listener : mDatabaseListeners) {
                listener.databaseUnreachable(message);
            }
        }
    }

    public static boolean hasCurrentAccount() {
        return mCurrentAccount != null;
    }

    public static Account getCurrentAccount() {
        return mCurrentAccount;
    }

    public static void setCurrentAccount(Account account) {
        UnicapApplication.mCurrentAccount = account;
    }

    public static void log(String message) {
        Log.d(TAG, message);
    }

    public static void error(String message) {
        Log.e(TAG, message);
    }

    public static void forceSync() {
        Bundle settingsBundle = new Bundle();
        settingsBundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        settingsBundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);

        ContentResolver.requestSync(UnicapApplication.getCurrentAccount(), UnicapContentProvider.AUTHORITY, settingsBundle);
    }

    public static boolean isSyncing() {
        return isSyncing;
    }

    public static void setIsSyncing(boolean isSyncing) {
        UnicapApplication.isSyncing = isSyncing;
    }
}
