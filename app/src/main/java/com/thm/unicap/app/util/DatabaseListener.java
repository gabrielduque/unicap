package com.thm.unicap.app.util;

public interface DatabaseListener {
    void databaseSyncing();
    void databaseUpdated();
    void databaseUnreachable(String message);
}
