package com.thm.unicap.app.util;

import android.content.Context;

import com.thm.unicap.app.R;
import com.thm.unicap.app.model.SubjectStatus;

import java.sql.Time;
import java.util.Calendar;

public class UnicapUtils {

    @SuppressWarnings("SpellCheckingInspection")
    private static String[][] nameExceptions = {
            {"Da", "da"},
            {"Das", "das"},
            {"De", "de"},
            {"Do", "do"},
    };

    @SuppressWarnings("SpellCheckingInspection")
    private static String[][] exceptions = {
            {"A", "a"},
            {"Aco", "Aço"},
            {"Administ", "Administração"},
            {"Administracao", "Administração"},
            {"Algebra", "Álgebra"},
            {"Analise", "Análise"},
            {"Analitica", "Analítica"},
            {"Arq", "Arquitetura"},
            {"Assist", "Assistido"},
            {"Basica", "Básica"},
            {"Basico", "Básico"},
            {"Cal", "Cálculo"},
            {"Calculo", "Cálculo"},
            {"Cien", "Ciência"},
            {"Ciencia", "Ciência"},
            {"Computacao", "Computação"},
            {"Conclusao", "Conclusão"},
            {"Conhecimen", "Conhecimento"},
            {"Const", "Construção"},
            {"Construcao", "Construção"},
            {"Cr", "Créditos"},
            {"Da", "da"},
            {"Das", "das"},
            {"De", "de"},
            {"Des", "Desenho"},
            {"Dif", "Diferencial"},
            {"Dir", "Direito"},
            {"Distribuidos", "Distribuídos"},
            {"Do", "do"},
            {"Dos", "dos"},
            {"E", "e"},
            {"Eco", "Economia"},
            {"Ecolog", "Ecologia"},
            {"Eletronica", "Eletrônica"},
            {"Em", "em"},
            {"Empreend", "Empreendimentos"},
            {"Eng", "Engenharia"},
            {"Estag", "Estágio"},
            {"Estagio", "Estágio"},
            {"Estatistica", "Estatística"},
            {"Etica", "Ética"},
            {"Fenomeno", "Fenômeno"},
            {"Fil", "Filosofia"},
            {"Financas", "Finanças"},
            {"Fisica", "Física"},
            {"Fundacoes", "Fundações"},
            {"Gerenc", "Gerenciamento"},
            {"Gestao", "Gestão"},
            {"Grafica", "Gráfica"},
            {"Hidraulica", "Hidráulica"},
            {"Hidrosanitarias", "Hidrosanitárias"},
            {"Hidrosanitarias", "Hidrosanitárias"},
            {"Ii", "II"},
            {"Iii", "III"},
            {"Informacao", "Informação"},
            {"Informatica", "Informática"},
            {"Ingles", "Inglês"},
            {"Instalacoes", "Instalações"},
            {"Instalacoes", "Instalações"},
            {"Inteligencia", "Inteligência"},
            {"Introd", "Introdução"},
            {"Introduc", "Introdução"},
            {"Introducao", "Introdução"},
            {"Iv", "IV"},
            {"Jurid", "Jurídica"},
            {"Ling", "Linguagens"},
            {"Logica", "Lógica"},
            {"Matematica", "Matemática"},
            {"Mecanica", "Mecânica"},
            {"Metodos", "Métodos"},
            {"Multimidia", "Multimídia"},
            {"Na", "na"},
            {"Numerico", "Numérico"},
            {"Numericos", "Numéricos"},
            {"Obrigatorio", "Obrigatório"},
            {"Org", "Organização"},
            {"Para", "para"},
            {"Paradig", "Paradigmas"},
            {"Por", "por"},
            {"Portugues", "Português"},
            {"Prat", "Prática"},
            {"Pratica", "Prática"},
            {"Princ", "Princípios"},
            {"Programacao", "Programação"},
            {"Publico", "Público"},
            {"Quimica", "Química"},
            {"Resistencia", "Resistência"},
            {"Segur", "Segurança"},
            {"Sociol", "Sociologia"},
            {"Superv", "Supervisionado"},
            {"Tec", "Técnico"},
            {"Tecnologico", "Tecnológico"},
            {"Trafego", "Tráfego"},
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

    public static SubjectStatus.ScheduleWeekday getCurrentScheduleWeekDay() {
        Calendar c = Calendar.getInstance();

        int day_of_week = c.get(Calendar.DAY_OF_WEEK);

        switch (day_of_week) {
            case 1:
                return SubjectStatus.ScheduleWeekday.Sun;
            case 2:
                return SubjectStatus.ScheduleWeekday.Mon;
            case 3:
                return SubjectStatus.ScheduleWeekday.Tue;
            case 4:
                return SubjectStatus.ScheduleWeekday.Wed;
            case 5:
                return SubjectStatus.ScheduleWeekday.Thu;
            case 6:
                return SubjectStatus.ScheduleWeekday.Fri;
            case 7:
                return SubjectStatus.ScheduleWeekday.Sat;
            default:
                return null;
        }
    }

    public static Time[] getTimesFromScheduleHour(char[] hoursArray) {

        Calendar begin = Calendar.getInstance();
        Calendar end = Calendar.getInstance();

        begin.set(Calendar.SECOND, 0);
        begin.set(Calendar.MILLISECOND, 0);
        end.set(Calendar.SECOND, 0);
        end.set(Calendar.MILLISECOND, 0);

        char beginChar = hoursArray[0];
        char endChar = hoursArray[hoursArray.length-1];

        switch (beginChar) {
            case 'A':
                begin.set(Calendar.HOUR_OF_DAY, 7);
                begin.set(Calendar.MINUTE, 30);
                break;
            case 'B':
                begin.set(Calendar.HOUR_OF_DAY, 8);
                begin.set(Calendar.MINUTE, 20);
                break;
            case 'C':
                begin.set(Calendar.HOUR_OF_DAY, 9);
                begin.set(Calendar.MINUTE, 10);
                break;
            case 'D':
                begin.set(Calendar.HOUR_OF_DAY, 10);
                begin.set(Calendar.MINUTE, 10);
                break;
            case 'E':
                begin.set(Calendar.HOUR_OF_DAY, 11);
                begin.set(Calendar.MINUTE, 10);
                break;
            case 'F':
                begin.set(Calendar.HOUR_OF_DAY, 12);
                begin.set(Calendar.MINUTE, 0);
                break;
            case 'G':
                begin.set(Calendar.HOUR_OF_DAY, 13);
                begin.set(Calendar.MINUTE, 0);
                break;
            case 'H':
                begin.set(Calendar.HOUR_OF_DAY, 13);
                begin.set(Calendar.MINUTE, 50);
                break;
            case 'I':
                begin.set(Calendar.HOUR_OF_DAY, 14);
                begin.set(Calendar.MINUTE, 50);
                break;
            case 'J':
                begin.set(Calendar.HOUR_OF_DAY, 15);
                begin.set(Calendar.MINUTE, 40);
                break;
            case 'L':
                begin.set(Calendar.HOUR_OF_DAY, 16);
                begin.set(Calendar.MINUTE, 40);
                break;
            case 'M':
                begin.set(Calendar.HOUR_OF_DAY, 17);
                begin.set(Calendar.MINUTE, 30);
                break;
            case 'N':
                begin.set(Calendar.HOUR_OF_DAY, 18);
                begin.set(Calendar.MINUTE, 30);
                break;
            case 'O':
                begin.set(Calendar.HOUR_OF_DAY, 19);
                begin.set(Calendar.MINUTE, 20);
                break;
            case 'P':
                begin.set(Calendar.HOUR_OF_DAY, 20);
                begin.set(Calendar.MINUTE, 20);
                break;
            case 'Q':
                begin.set(Calendar.HOUR_OF_DAY, 21);
                begin.set(Calendar.MINUTE, 10);
                break;
            default:
                return null;
        }

        switch (endChar) {
            case 'A':
                end.set(Calendar.HOUR_OF_DAY, 8);
                end.set(Calendar.MINUTE, 20);
                break;
            case 'B':
                end.set(Calendar.HOUR_OF_DAY, 9);
                end.set(Calendar.MINUTE, 10);
                break;
            case 'C':
                end.set(Calendar.HOUR_OF_DAY, 10);
                end.set(Calendar.MINUTE, 10);
                break;
            case 'D':
                end.set(Calendar.HOUR_OF_DAY, 11);
                end.set(Calendar.MINUTE, 0);
                break;
            case 'E':
                end.set(Calendar.HOUR_OF_DAY, 12);
                end.set(Calendar.MINUTE, 0);
                break;
            case 'F':
                end.set(Calendar.HOUR_OF_DAY, 12);
                end.set(Calendar.MINUTE, 50);
                break;
            case 'G':
                end.set(Calendar.HOUR_OF_DAY, 13);
                end.set(Calendar.MINUTE, 50);
                break;
            case 'H':
                end.set(Calendar.HOUR_OF_DAY, 14);
                end.set(Calendar.MINUTE, 40);
                break;
            case 'I':
                end.set(Calendar.HOUR_OF_DAY, 15);
                end.set(Calendar.MINUTE, 40);
                break;
            case 'J':
                end.set(Calendar.HOUR_OF_DAY, 16);
                end.set(Calendar.MINUTE, 30);
                break;
            case 'L':
                end.set(Calendar.HOUR_OF_DAY, 17);
                end.set(Calendar.MINUTE, 30);
                break;
            case 'M':
                end.set(Calendar.HOUR_OF_DAY, 18);
                end.set(Calendar.MINUTE, 20);
                break;
            case 'N':
                end.set(Calendar.HOUR_OF_DAY, 19);
                end.set(Calendar.MINUTE, 20);
                break;
            case 'O':
                end.set(Calendar.HOUR_OF_DAY, 20);
                end.set(Calendar.MINUTE, 10);
                break;
            case 'P':
                end.set(Calendar.HOUR_OF_DAY, 21);
                end.set(Calendar.MINUTE, 10);
                break;
            case 'Q':
                end.set(Calendar.HOUR_OF_DAY, 22);
                end.set(Calendar.MINUTE, 0);
                break;
            default:
                return null;
        }

        return new Time[]{
                new Time(begin.getTimeInMillis()),
                new Time(end.getTimeInMillis())
        };
    }

    public static String scheduleWeekDayToString(Context context, SubjectStatus.ScheduleWeekday scheduleWeekday) {
        switch (scheduleWeekday) {
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

    public static boolean equalsWithNulls(Object a, Object b) {
        if (a==b) return true;
        if ((a==null)||(b==null)) return false;
        return a.equals(b);
    }

    public static String capitalizeFirstLetter(String original){
        if(original.length() == 0)
            return original;
        return original.substring(0, 1).toUpperCase() + original.substring(1);
    }

}
