package com.thm.unicap.app.auth;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class UnicapAuthenticatorService extends Service {
    @Override
    public IBinder onBind(Intent intent) {

        UnicapAuthenticator authenticator = new UnicapAuthenticator(this);
        return authenticator.getIBinder();
    }
}
