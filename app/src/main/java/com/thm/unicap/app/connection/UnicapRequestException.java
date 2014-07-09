package com.thm.unicap.app.connection;

import android.content.Context;

import com.thm.unicap.app.R;

public class UnicapRequestException extends Exception {

    private Code code;

    public UnicapRequestException(Code code) {
        this.code = code;
    }

    public enum Code {
        CONNECTION_FAILED,
        INCORRECT_REGISTRATION,
        INCORRECT_PASSWORD,
        MAX_TRIES_EXCEEDED,
        DATA_PARSE_EXCEPTION,
        AUTH_NEEDED,
    }

    public String getMessageFromContext(Context context) {
        switch (code) {
            case CONNECTION_FAILED:
                return context.getString(R.string.error_connection_failed);
            case INCORRECT_REGISTRATION:
                return context.getString(R.string.error_incorrect_registration);
            case INCORRECT_PASSWORD:
                return context.getString(R.string.error_incorrect_password);
            case MAX_TRIES_EXCEEDED:
                return context.getString(R.string.error_max_tries_exceeded);
            case DATA_PARSE_EXCEPTION:
                return context.getString(R.string.error_generic_message);
            case AUTH_NEEDED:
                return context.getString(R.string.error_auth_needed);
            default:
                return context.getString(R.string.error_generic_message);
        }
    }
}
