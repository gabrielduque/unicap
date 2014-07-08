package com.thm.unicap.app.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class UnicapSyncService extends Service {
    // Storage for an instance of the sync adapter
    private static UnicapSyncAdapter sUnicapSyncAdapter = null;
    // Object to use as a thread-safe lock
    private static final Object sUnicapSyncAdapterLock = new Object();
    /*
     * Instantiate the sync adapter object.
     */
    @Override
    public void onCreate() {
        /*
         * Create the sync adapter as a singleton.
         * Set the sync adapter as syncable
         * Disallow parallel syncs
         */
        synchronized (sUnicapSyncAdapterLock) {
            if (sUnicapSyncAdapter == null) {
                sUnicapSyncAdapter = new UnicapSyncAdapter(getApplicationContext(), true);
            }
        }
    }
    /**
     * Return an object that allows the system to invoke
     * the sync adapter.
     *
     */
    @Override
    public IBinder onBind(Intent intent) {
        /*
         * Get the object that allows external processes
         * to call onPerformSync(). The object is created
         * in the base class code when the SyncAdapter
         * constructors call super()
         */
        return sUnicapSyncAdapter.getSyncAdapterBinder();
    }
}
