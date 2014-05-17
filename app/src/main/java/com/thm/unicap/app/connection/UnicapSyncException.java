package com.thm.unicap.app.connection;

import android.content.Context;

import com.thm.unicap.app.R;

public class UnicapSyncException extends Exception {

    private Code code;

    public UnicapSyncException(Code code) {
        this.code = code;
    }

    public enum Code {
        CONNECTION,
        REGISTRATION,
        PASSWORD,
        TRIES,
        PARSE,
    }

    public String getMessageFromContext(Context context) {
        switch (code) {
            case CONNECTION:
                return context.getString(R.string.error_connection_failed);
            case REGISTRATION:
                return context.getString(R.string.error_incorrect_registration);
            case PASSWORD:
                return context.getString(R.string.error_incorrect_password);
            case TRIES:
                return context.getString(R.string.error_max_tries_exceeded);
            case PARSE:
                return context.getString(R.string.error_generic_message);
            default:
                return context.getString(R.string.error_generic_message);
        }
    }
}
