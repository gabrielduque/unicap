package com.thm.unicap.app;

import android.accounts.Account;
import android.app.Application;
import android.content.ContentResolver;
import android.os.Bundle;
import android.util.Log;

import com.activeandroid.ActiveAndroid;
import com.halfbit.tinybus.TinyBus;
import com.thm.unicap.app.model.Student;
import com.thm.unicap.app.sync.UnicapContentProvider;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class UnicapApplication extends Application {
    public static final String TAG = "Unicap";

    private static Student currentStudent = null;
    private static Account currentAccount = null;
    private static boolean isSyncing;

    public static final TinyBus bus = new TinyBus();

    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault("fonts/Roboto-Regular.ttf", R.attr.fontPath);
        ActiveAndroid.initialize(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        ActiveAndroid.dispose();
    }

    public static Student getCurrentStudent() {
        return currentStudent;
    }

    public static void setCurrentStudent(Student student) {
        UnicapApplication.currentStudent = student;
    }

    public static boolean hasStudentData() {
        return currentStudent != null;
    }

    public static boolean hasCurrentAccount() {
        return currentAccount != null;
    }

    public static Account getCurrentAccount() {
        return currentAccount;
    }

    public static void setCurrentAccount(Account account) {
        UnicapApplication.currentAccount = account;
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
