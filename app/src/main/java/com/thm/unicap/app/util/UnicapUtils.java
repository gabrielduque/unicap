package com.thm.unicap.app.util;

public class UnicapUtils {

    private static String[][] exceptions = {
            {"A", "a"},
            {"Administracao", "Administração"},
            {"Algebra", "Álgebra"},
            {"Analise", "Análise"},
            {"Arq", "Arquitetura"},
            {"Basica", "Básica"},
            {"Calculo", "Cálculo"},
            {"Ciencia", "Ciência"},
            {"Computacao", "Computação"},
            {"Conhecimen", "Conhecimento"},
            {"Da", "da"},
            {"De", "de"},
            {"Dif", "Diferencial"},
            {"Do", "do"},
            {"E", "e"},
            {"Eletronica", "Eletrônica"},
            {"Estatistica", "Estatística"},
            {"Fil", "Filosofia"},
            {"Ii", "II"},
            {"Iii", "III"},
            {"Iv", "IV"},
            {"Informacao", "Informação"},
            {"Informatica", "Informática"},
            {"Ingles", "Inglês"},
            {"Inteligencia", "Inteligência"},
            {"Introd", "Introdução"},
            {"Introducao", "Introdução"},
            {"Ling", "Linguagens"},
            {"Logica", "Lógica"},
            {"Matematica", "Matemática"},
            {"Metodos", "Métodos"},
            {"Numericos", "Numéricos"},
            {"Org", "Organização"},
            {"Paradig", "Paradigmas"},
            {"Portugues", "Português"},
            {"Pratica", "Prática"},
            {"Programacao", "Programação"},
            {"Tecnologico", "Tecnológico"},
            {"Transcendencia", "Transcendência"},
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
            str = str.replaceAll("(?i)\\b"+exception[0]+"\\b", exception[1]);
        }
        return str;
    }

}
