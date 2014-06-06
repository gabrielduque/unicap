package com.thm.unicap.app.util;

public class UnicapUtils {

    @SuppressWarnings("SpellCheckingInspection")
    private static String[][] nameExceptions = {
            {"Da", "da"},
            {"De", "de"},
            {"Do", "do"},
    };

    @SuppressWarnings("SpellCheckingInspection")
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
            {"Conclusao", "Conclusão"},
            {"Conhecimen", "Conhecimento"},
            {"Cr", "Créditos"},
            {"Da", "da"},
            {"De", "de"},
            {"Dif", "Diferencial"},
            {"Dir", "Direito"},
            {"Distribuidos", "Distribuídos"},
            {"Do", "do"},
            {"E", "e"},
            {"Em", "em"},
            {"Eletronica", "Eletrônica"},
            {"Estatistica", "Estatística"},
            {"Estagio", "Estágio"},
            {"Etica", "Ética"},
            {"Fil", "Filosofia"},
            {"Grafica", "Gráfica"},
            {"Ii", "II"},
            {"Iii", "III"},
            {"Iv", "IV"},
            {"Informacao", "Informação"},
            {"Informatica", "Informática"},
            {"Ingles", "Inglês"},
            {"Inteligencia", "Inteligência"},
            {"Introd", "Introdução"},
            {"Introducao", "Introdução"},
            {"Jurid", "Jurídica"},
            {"Ling", "Linguagens"},
            {"Logica", "Lógica"},
            {"Matematica", "Matemática"},
            {"Metodos", "Métodos"},
            {"Multimidia", "Multimídia"},
            {"Numericos", "Numéricos"},
            {"Org", "Organização"},
            {"Paradig", "Paradigmas"},
            {"Portugues", "Português"},
            {"Pratica", "Prática"},
            {"Prat", "Prática"},
            {"Programacao", "Programação"},
            {"Publico", "Público"},
            {"Tecnologico", "Tecnológico"},
            {"Transcendencia", "Transcendência"},
            {"Tributario", "Tributário"},
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

    public static String replaceNameExceptions (String str) {

        for (String[] exception : nameExceptions) {
            str = str.replaceAll("(?i)\\b"+exception[0]+"\\b", exception[1]);
        }
        return str;
    }

    public static String replaceExceptions (String str) {

        for (String[] exception : exceptions) {
            str = str.replaceAll("(?i)\\b"+exception[0]+"\\b", exception[1]);
        }
        return str;
    }

}
