package com.thm.unicap.app.util;

public class UnicapUtils {

    private static String[][] exceptions = {
            {"De", "de"},
            {"Do", "do"},
            {"Da", "da"},
            {"Ciencia", "Ciência"},
            {"Computacao", "Computação"},
            {"Paradig", "Paradigmas"},
            {"Ling", "Linguagens"},
            {"Programacao", "Programação"},
            {"Informacao", "Informação"},
            {"Metodos", "Métodos"},
            {"Numericos", "Numéricos"},
    };

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

    public static String replaceExceptions (String str) {

        for (String[] exception : exceptions) {
            str = str.replaceAll("(?i)"+exception[0], exception[1]);
        }
        return str;
    }

}
