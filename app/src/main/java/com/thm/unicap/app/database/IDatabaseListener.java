package com.thm.unicap.app.database;

public interface IDatabaseListener {
    void databaseSyncing();
    void databaseUpdated();
    void databaseUnreachable(String message);
}
