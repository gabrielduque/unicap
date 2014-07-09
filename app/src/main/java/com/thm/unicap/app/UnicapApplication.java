package com.thm.unicap.app;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.content.ContentResolver;
import android.content.SyncStatusObserver;
import android.os.Bundle;
import android.util.Log;

import com.activeandroid.app.Application;
import com.activeandroid.query.Select;
import com.github.johnpersano.supertoasts.SuperActivityToast;
import com.github.johnpersano.supertoasts.SuperToast;
import com.thm.unicap.app.auth.AccountGeneral;
import com.thm.unicap.app.model.Student;
import com.thm.unicap.app.sync.UnicapContentProvider;
import com.thm.unicap.app.util.DatabaseListener;

import java.util.ArrayList;

public class UnicapApplication extends Application {
    public static final String TAG = "Unicap";

    private static Student mCurrentStudent = null;
    private static Account mCurrentAccount = null;
    private static ArrayList<DatabaseListener> mDatabaseListeners;

    @Override
    public void onCreate() {
        super.onCreate();
        mDatabaseListeners = new ArrayList<DatabaseListener>();
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

    public static void addStudentListener(DatabaseListener listener) {
        if(!mDatabaseListeners.contains(listener))
            mDatabaseListeners.add(listener);
    }

    public static void removeStudentListener(DatabaseListener listener) {
        mDatabaseListeners.remove(listener);
    }

    public static void notifyDatabaseChanged() {
        Log.d("UNICAP", "CHANGED!");
        if(mCurrentStudent != null) {
            for (DatabaseListener listener : mDatabaseListeners) {
                listener.databaseChanged();
            }
        }
    }

    public static Account getCurrentAccount() {
        return mCurrentAccount;
    }

    public static void setCurrentAccount(Account account) {
        UnicapApplication.mCurrentAccount = account;
    }

}
