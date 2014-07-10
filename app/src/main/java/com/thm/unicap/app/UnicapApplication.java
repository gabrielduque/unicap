package com.thm.unicap.app;

import android.accounts.Account;
import android.content.IntentFilter;

import com.activeandroid.app.Application;
import com.thm.unicap.app.model.Student;
import com.thm.unicap.app.sync.UnicapSyncReceiver;
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
        registerReceiver(new UnicapSyncReceiver(), new IntentFilter(UnicapSyncReceiver.SYNC_ACTION));
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

    public static void notifyDatabaseUpdated() {
        if(mCurrentStudent != null) {
            for (DatabaseListener listener : mDatabaseListeners) {
                listener.databaseUpdated();
            }
        }
    }

    public static void notifyDatabaseUnreachable(String message) {
        if(mCurrentStudent == null) {
            for (DatabaseListener listener : mDatabaseListeners) {
                listener.databaseUnreachable(message);
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
