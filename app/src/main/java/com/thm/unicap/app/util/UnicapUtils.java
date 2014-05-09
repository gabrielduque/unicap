package com.thm.unicap.app.util;

public class UnicapUtils {

    public static boolean isRegistrationValid(String email) {

        if(email.length() != 11) return false;

        for (int i = 0; i < email.length(); i++) {
            if(i==9) {
                if(email.charAt(i) != '-') return false;
            } else {
                if(!Character.isDigit(email.charAt(i))) return false;
            }
        }

        return true;
    }

    public static boolean isPasswordValid(String password) {

        if(password.length() != 6) return false;

        for (int i = 0; i < password.length(); i++) {
            if(!Character.isDigit(password.charAt(i))) return false;
        }

        return true;
    }

}
