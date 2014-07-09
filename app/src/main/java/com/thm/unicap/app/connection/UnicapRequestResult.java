package com.thm.unicap.app.connection;

import android.content.Context;

public class UnicapRequestResult {
    private boolean success;
    private UnicapRequestException exception;

    public UnicapRequestResult(boolean success) {
        this.success = success;
    }

    public UnicapRequestResult(boolean success, UnicapRequestException exception) {
        this.success = success;
        this.exception = exception;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public UnicapRequestException getException() {
        return exception;
    }

    public void setException(UnicapRequestException exception) {
        this.exception = exception;
    }

    public String getExceptionMessage(Context context) {
        if (exception != null)
            return exception.getMessageFromContext(context);
        else
            return "";
    }
}
