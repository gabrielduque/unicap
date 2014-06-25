package com.thm.unicap.app.util;

import android.content.Context;

import com.thm.unicap.app.R;
import com.thm.unicap.app.model.SubjectStatus;

import java.sql.Time;
import java.util.Calendar;
import java.util.TimeZone;

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
        return replaceFromDictionary(str, nameExceptions);
    }

    public static String replaceExceptions (String str) {
        return replaceFromDictionary(str, exceptions);
    }

    public static String replaceFromDictionary (String str, String[][] replacePairs) {

        for (String[] replacePair : replacePairs)
            str = str.replaceAll("(?i)\\b"+replacePair[0]+"\\b", replacePair[1]);

        return str;
    }

    public static SubjectStatus.ScheduleWeekDay getCurrentScheduleWeekDay() {
        Calendar c = Calendar.getInstance();

        int day_of_week = c.get(Calendar.DAY_OF_WEEK);

        switch (day_of_week) {
            case 1:
                return SubjectStatus.ScheduleWeekDay.Sun;
            case 2:
                return SubjectStatus.ScheduleWeekDay.Mon;
            case 3:
                return SubjectStatus.ScheduleWeekDay.Tue;
            case 4:
                return SubjectStatus.ScheduleWeekDay.Wed;
            case 5:
                return SubjectStatus.ScheduleWeekDay.Thu;
            case 6:
                return SubjectStatus.ScheduleWeekDay.Fri;
            case 7:
                return SubjectStatus.ScheduleWeekDay.Sat;
            default:
                return null;
        }
    }

    public static Time[] getTimesFromScheduleHour(SubjectStatus.ScheduleHour hour) {

        Calendar begin = Calendar.getInstance();
        Calendar end = Calendar.getInstance();

        begin.set(Calendar.SECOND, 0);
        begin.set(Calendar.MILLISECOND, 0);
        end.set(Calendar.SECOND, 0);
        end.set(Calendar.MILLISECOND, 0);

        switch (hour) {
            case AB:
                begin.set(Calendar.HOUR_OF_DAY, 7);
                begin.set(Calendar.MINUTE, 30);
                end.set(Calendar.HOUR_OF_DAY, 9);
                end.set(Calendar.MINUTE, 10);
                return new Time[]{
                    new Time(begin.getTimeInMillis()),
                    new Time(end.getTimeInMillis())
                };
            case CD:
                begin.set(Calendar.HOUR_OF_DAY, 9);
                begin.set(Calendar.MINUTE, 20);
                end.set(Calendar.HOUR_OF_DAY, 11);
                end.set(Calendar.MINUTE, 0);
                return new Time[]{
                        new Time(begin.getTimeInMillis()),
                        new Time(end.getTimeInMillis())
                };
            case EF:
                begin.set(Calendar.HOUR_OF_DAY, 11);
                begin.set(Calendar.MINUTE, 10);
                end.set(Calendar.HOUR_OF_DAY, 12);
                end.set(Calendar.MINUTE, 50);
                return new Time[]{
                        new Time(begin.getTimeInMillis()),
                        new Time(end.getTimeInMillis())
                };
            case GH:
                begin.set(Calendar.HOUR_OF_DAY, 13);
                begin.set(Calendar.MINUTE, 0);
                end.set(Calendar.HOUR_OF_DAY, 14);
                end.set(Calendar.MINUTE, 40);
                return new Time[]{
                        new Time(begin.getTimeInMillis()),
                        new Time(end.getTimeInMillis())
                };
            case IJ:
                begin.set(Calendar.HOUR_OF_DAY, 14);
                begin.set(Calendar.MINUTE, 50);
                end.set(Calendar.HOUR_OF_DAY, 16);
                end.set(Calendar.MINUTE, 30);
                return new Time[]{
                        new Time(begin.getTimeInMillis()),
                        new Time(end.getTimeInMillis())
                };
            case LM:
                begin.set(Calendar.HOUR_OF_DAY, 16);
                begin.set(Calendar.MINUTE, 40);
                end.set(Calendar.HOUR_OF_DAY, 18);
                end.set(Calendar.MINUTE, 20);
                return new Time[]{
                        new Time(begin.getTimeInMillis()),
                        new Time(end.getTimeInMillis())
                };
            case NO:
                begin.set(Calendar.HOUR_OF_DAY, 18);
                begin.set(Calendar.MINUTE, 30);
                end.set(Calendar.HOUR_OF_DAY, 20);
                end.set(Calendar.MINUTE, 10);
                return new Time[]{
                        new Time(begin.getTimeInMillis()),
                        new Time(end.getTimeInMillis())
                };
            case PQ:
                begin.set(Calendar.HOUR_OF_DAY, 20);
                begin.set(Calendar.MINUTE, 20);
                end.set(Calendar.HOUR_OF_DAY, 22);
                end.set(Calendar.MINUTE, 0);
                return new Time[]{
                        new Time(begin.getTimeInMillis()),
                        new Time(end.getTimeInMillis())
                };
            default:
                return null;
        }
    }

    public static String scheduleWeekDayToString(Context context, SubjectStatus.ScheduleWeekDay scheduleWeekDay) {
        switch (scheduleWeekDay) {
            case Sun:
                return context.getString(R.string.sunday);
            case Mon:
                return context.getString(R.string.monday);
            case Tue:
                return context.getString(R.string.tuesday);
            case Wed:
                return context.getString(R.string.wednesday);
            case Thu:
                return context.getString(R.string.thusday);
            case Fri:
                return context.getString(R.string.friday);
            case Sat:
                return context.getString(R.string.saturday);
            default:
                return "";
        }
    }

}
