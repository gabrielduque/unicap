package com.thm.unicap.app.connection;

import android.content.Context;

public class UnicapSyncResult {
    private boolean success;
    private UnicapSyncException exception;

    public UnicapSyncResult(boolean success) {
        this.success = success;
    }

    public UnicapSyncResult(boolean success, UnicapSyncException exception) {
        this.success = success;
        this.exception = exception;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public UnicapSyncException getException() {
        return exception;
    }

    public void setException(UnicapSyncException exception) {
        this.exception = exception;
    }

    public String getExceptionMessage(Context context) {
        if (exception != null)
            return exception.getMessageFromContext(context);
        else
            return "";
    }
}
