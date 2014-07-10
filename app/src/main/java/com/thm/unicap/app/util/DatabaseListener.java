package com.thm.unicap.app.util;

public interface DatabaseListener {
    void databaseUpdated();
    void databaseUnreachable(String message);
}
