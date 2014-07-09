package com.thm.unicap.app.connection;

import com.activeandroid.ActiveAndroid;
import com.thm.unicap.app.UnicapApplication;
import com.thm.unicap.app.model.Student;
import com.thm.unicap.app.model.SubjectStatus;
import com.thm.unicap.app.model.SubjectTest;
import com.thm.unicap.app.util.UnicapUtils;
import com.thm.unicap.app.util.WordUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class UnicapSync {

    private static String actionURL;

    public static void syncAll() throws UnicapSyncException {
        receivePersonalData();
        receivePastSubjectsData();
        receiveActualSubjectsData();
        receivePendingSubjectsData();
        receiveSubjectsCalendarData();
        receiveSubjectsGradesData();
    }

    public static String loginRequest(String registration, String password) throws UnicapSyncException {

        Document document;

        // Request to get temporary session to be used on login request
        try {
            document = Jsoup.connect(RequestUtils.REQUEST_BASE_URL)
                    .timeout(RequestUtils.REQUEST_TIMEOUT)
                    .get();

            String loginURL = document.select("form").first().attr("action");

            // Request to get permanent session to all future requests
            document = Jsoup.connect(RequestUtils.REQUEST_BASE_URL + loginURL)
                    .timeout(RequestUtils.REQUEST_TIMEOUT)
                    .data(RequestUtils.Params.ROUTINE, RequestUtils.Values.ROUTINE_LOGIN)
                    .data(RequestUtils.Params.REGISTRATION, registration.substring(0, 9))
                    .data(RequestUtils.Params.DIGIT, registration.substring(10))
                    .data(RequestUtils.Params.PASSWORD, password)
                    .get();

        } catch (IOException e) {
            throw new UnicapSyncException(UnicapSyncException.Code.CONNECTION_FAILED);
        }

        if(document.select(".msg").text().matches(".*Matr.cula.*"))
            throw new UnicapSyncException(UnicapSyncException.Code.INCORRECT_REGISTRATION);
        else if(document.select(".msg").text().matches(".*Senha.*"))
            throw new UnicapSyncException(UnicapSyncException.Code.INCORRECT_PASSWORD);
        else if(document.select(".msg").text().matches(".*limite.*"))
            throw new UnicapSyncException(UnicapSyncException.Code.MAX_TRIES_EXCEEDED);

        actionURL = document.select("form").first().attr("action");

//        UnicapDataManager.initStudent(registration, password);

        return actionURL.split("=")[1];
    }

    public static void receivePersonalData() throws UnicapSyncException {

        if (actionURL == null) throw new UnicapSyncException(UnicapSyncException.Code.AUTH_NEEDED);

        // Personal data request
        Document document = null;
        try {
            document = Jsoup.connect(RequestUtils.REQUEST_BASE_URL + actionURL)
                    .data(RequestUtils.Params.ROUTINE, RequestUtils.Values.ROUTINE_PERSONAL)
                    .timeout(RequestUtils.REQUEST_TIMEOUT)
                    .get();
        } catch (IOException e) {
            throw new UnicapSyncException(UnicapSyncException.Code.CONNECTION_FAILED);
        }

        String registration = document.select(".tab_texto").get(0).text();
        String name = UnicapUtils.replaceNameExceptions(WordUtils.capitalizeFully(document.select(".tab_texto").get(1).text(), null));
        String course = UnicapUtils.replaceExceptions(WordUtils.capitalizeFully(document.select(".tab_texto").get(2).text()).replace("Curso De ", ""));
        String shift = document.select(".tab_texto").get(4).text();
        String gender = WordUtils.capitalizeFully(document.select(".tab_texto").get(5).text(), null);
        String birthday = WordUtils.capitalizeFully(document.select(".tab_texto").get(6).text(), null);
        String email = document.select(".tab_texto").get(20).text();

        UnicapDataManager.persistPersonalData(
                registration,
                name,
                course,
                shift,
                gender,
                birthday,
                email
        );
    }

    public static void receivePastSubjectsData() throws UnicapSyncException {

        if (actionURL == null) throw new UnicapSyncException(UnicapSyncException.Code.AUTH_NEEDED);

        // Subjects data request
        Document document = null;
        try {
            document = Jsoup.connect(RequestUtils.REQUEST_BASE_URL + actionURL)
                    .data(RequestUtils.Params.ROUTINE, RequestUtils.Values.ROUTINE_SUBJECTS_PAST)
                    .timeout(RequestUtils.REQUEST_TIMEOUT)
                    .get();
        } catch (IOException e) {
            throw new UnicapSyncException(UnicapSyncException.Code.CONNECTION_FAILED);
        }

        Elements subjectsTable = document.select("table").get(6).select("tr");

        subjectsTable.remove(0); // Removing header

        // Using transactions to speed up the process
        ActiveAndroid.beginTransaction();

        for (Element subjectRow : subjectsTable) {

            Elements subjectColumns = subjectRow.select(".tab_texto");

            String code = subjectColumns.get(1).text();
            String name = UnicapUtils.replaceExceptions(WordUtils.capitalizeFully(subjectColumns.get(2).text()));
            String paidIn = null;
            String rawPaidIn = subjectColumns.get(0).text();

            if(!rawPaidIn.isEmpty()) {
                if(rawPaidIn.length() == 5)
                    paidIn = new StringBuilder(rawPaidIn).insert(4, ".").toString();
                else
                    paidIn = rawPaidIn;
            }

            Float average = Float.parseFloat(subjectColumns.get(3).text());

            SubjectStatus.Situation situation;
            if (subjectColumns.get(4).text().equals("AP")) situation = SubjectStatus.Situation.APPROVED;
            else if (subjectColumns.get(4).text().equals("RM")) situation = SubjectStatus.Situation.REPROVED;
            else if (subjectColumns.get(4).text().equals("CI")) situation = SubjectStatus.Situation.IMPORTED;
            else situation = SubjectStatus.Situation.UNKNOWN;

            UnicapDataManager.persistPastSubject(code, name, paidIn, average, situation);

        }

        ActiveAndroid.setTransactionSuccessful();
        ActiveAndroid.endTransaction();

    }

    public static void receiveActualSubjectsData() throws UnicapSyncException {

        if (actionURL == null) throw new UnicapSyncException(UnicapSyncException.Code.AUTH_NEEDED);

        // Subjects data request
        Document document = null;
        try {
            document = Jsoup.connect(RequestUtils.REQUEST_BASE_URL + actionURL)
                    .data(RequestUtils.Params.ROUTINE, RequestUtils.Values.ROUTINE_SUBJECTS_ACTUAL)
                    .timeout(RequestUtils.REQUEST_TIMEOUT)
                    .get();
        } catch (IOException e) {
            throw new UnicapSyncException(UnicapSyncException.Code.CONNECTION_FAILED);
        }

        Elements subjectsTable = document.select("table").get(5).select("tr");

        subjectsTable.remove(0); // Removing header
        subjectsTable.remove(subjectsTable.size()-1); // Removing 'sum' row

        // Using transactions to speed up the process
        ActiveAndroid.beginTransaction();

        for (Element subjectRow : subjectsTable) {

            Elements subjectColumns = subjectRow.select(".tab_texto");

            String code = subjectColumns.get(0).text();
            String paidIn = new StringBuilder(document.select("table").get(4).select("td").get(1).text()).insert(4, ".").toString();

            String name = UnicapUtils.replaceExceptions(WordUtils.capitalizeFully(subjectColumns.get(1).text()));
            Integer workload = Integer.parseInt(subjectColumns.get(5).text());
            Integer credits = Integer.parseInt(subjectColumns.get(6).text());
            Integer period = Integer.parseInt(subjectColumns.get(7).text());
            String team = subjectColumns.get(2).text();
            String room = subjectColumns.get(3).text();
            String schedule = subjectColumns.get(4).text();

            UnicapDataManager.persistActualSubject(code, paidIn, name, workload, credits, period, team, room, schedule);
        }

        ActiveAndroid.setTransactionSuccessful();
        ActiveAndroid.endTransaction();

    }

    public static void receivePendingSubjectsData() throws UnicapSyncException {

        if (actionURL == null) throw new UnicapSyncException(UnicapSyncException.Code.AUTH_NEEDED);

        // Subjects data request
        Document document = null;
        try {
            document = Jsoup.connect(RequestUtils.REQUEST_BASE_URL + actionURL)
                    .data(RequestUtils.Params.ROUTINE, RequestUtils.Values.ROUTINE_SUBJECTS_PENDING)
                    .timeout(RequestUtils.REQUEST_TIMEOUT)
                    .get();
        } catch (IOException e) {
            throw new UnicapSyncException(UnicapSyncException.Code.CONNECTION_FAILED);
        }

        Elements subjectsTable = document.select("table").get(6).select("tr");

        subjectsTable.remove(0); // Removing header

        //TODO: refactor and improve
        //Fixing cases with repeated subject codes (Using pattern: ABC, ABC-2, ABC-3, ..., ABC-N)
        HashMap<String, Integer> codeCounts = new HashMap<String, Integer>();
        for (Element subjectRow : subjectsTable) {
            Elements subjectColumns = subjectRow.select(".tab_texto");
            String code = subjectColumns.get(2).text();
            Integer count = 1;

            if(codeCounts.containsKey(code)) {
                codeCounts.put(code, codeCounts.get(code)+1);
                subjectColumns.get(2).text(code + "-" + codeCounts.get(code).toString());
                count = codeCounts.get(code);
            }

            codeCounts.put(code, count);
        }


        // Using transactions to speed up the process
        ActiveAndroid.beginTransaction();

        for (Element subjectRow : subjectsTable) {

            Elements subjectColumns = subjectRow.select(".tab_texto");

            String code = subjectColumns.get(2).text();
            int period = Integer.parseInt(subjectColumns.get(0).text());
            String name = UnicapUtils.replaceExceptions(WordUtils.capitalizeFully(subjectColumns.get(4).text()));
            int credits = Integer.parseInt(subjectColumns.get(5).text());
            int workload = Integer.parseInt(subjectColumns.get(6).text());

            UnicapDataManager.persistPendingSubject(code, period, name, credits, workload);

        }

        ActiveAndroid.setTransactionSuccessful();
        ActiveAndroid.endTransaction();

    }

    public static void receiveSubjectsCalendarData() throws UnicapSyncException {

        if (actionURL == null) throw new UnicapSyncException(UnicapSyncException.Code.AUTH_NEEDED);

        // Subjects data request
        Document document = null;
        try {
            document = Jsoup.connect(RequestUtils.REQUEST_BASE_URL + actionURL)
                    .data(RequestUtils.Params.ROUTINE, RequestUtils.Values.ROUTINE_SUBJECTS_CALENDAR)
                    .timeout(RequestUtils.REQUEST_TIMEOUT)
                    .get();
        } catch (IOException e) {
            throw new UnicapSyncException(UnicapSyncException.Code.CONNECTION_FAILED);
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Elements subjectsTable = document.select("table").get(5).select("tr");

        subjectsTable.remove(0); // Removing header

        // Using transactions to speed up the process
        ActiveAndroid.beginTransaction();

        for (Element subjectRow : subjectsTable) {

            Elements subjectColumns = subjectRow.select(".tab_texto");

            String code = subjectColumns.get(0).text();

            Date first_degree_date1, first_degree_date2, second_degree_date1, final_degree_date1, final_degree_date2;

            try {
                first_degree_date1 = simpleDateFormat.parse(subjectColumns.get(3).text());
            } catch (ParseException e) {
                first_degree_date1 = null;
            }
            try {
                first_degree_date2 = simpleDateFormat.parse(subjectColumns.get(4).text());
            } catch (ParseException e) {
                first_degree_date2 = null;
            }
            try {
                second_degree_date1 = simpleDateFormat.parse(subjectColumns.get(5).text());
            } catch (ParseException e) {
                second_degree_date1 = null;
            }
            try {
                final_degree_date1 = simpleDateFormat.parse(subjectColumns.get(6).text());
            } catch (ParseException e) {
                final_degree_date1 = null;
            }
            try {
                final_degree_date2 = simpleDateFormat.parse(subjectColumns.get(7).text());
            } catch (ParseException e) {
                final_degree_date2 = null;
            }

            UnicapDataManager.persistSubjectCalendar(code, SubjectTest.Degree.FIRST_DEGREE, first_degree_date1, first_degree_date2);
            UnicapDataManager.persistSubjectCalendar(code, SubjectTest.Degree.SECOND_DEGREE, second_degree_date1, null); // SECOND_DEGREE doesn't have date2
            UnicapDataManager.persistSubjectCalendar(code, SubjectTest.Degree.FINAL_DEGREE, final_degree_date1, final_degree_date2);

        }

        ActiveAndroid.setTransactionSuccessful();
        ActiveAndroid.endTransaction();

    }

    public static void receiveSubjectsGradesData() throws UnicapSyncException {

        if (actionURL == null) throw new UnicapSyncException(UnicapSyncException.Code.AUTH_NEEDED);

        // Subjects data request
        Document document = null;
        try {
            document = Jsoup.connect(RequestUtils.REQUEST_BASE_URL + actionURL)
                    .data(RequestUtils.Params.ROUTINE, RequestUtils.Values.ROUTINE_SUBJECTS_TESTS)
                    .timeout(RequestUtils.REQUEST_TIMEOUT)
                    .get();
        } catch (IOException e) {
            throw new UnicapSyncException(UnicapSyncException.Code.CONNECTION_FAILED);
        }

        Elements subjectsTable = document.select("table").get(7).select("tr");

        subjectsTable.remove(0); // Removing header

        // Using transactions to speed up the process
        ActiveAndroid.beginTransaction();

        for (Element subjectRow : subjectsTable) {

            Elements subjectColumns = subjectRow.select(".tab_texto");

            String code = subjectColumns.get(0).text();

            SubjectTest.Degree first_degree = SubjectTest.Degree.FIRST_DEGREE;
            String first_gradeText = subjectColumns.get(2).text();
            try {
                UnicapDataManager.persistSubjectGrade(code, first_degree, Float.parseFloat(first_gradeText));
            } catch (Exception ignore) {
                UnicapDataManager.persistSubjectGrade(code, first_degree, null);
            }

            SubjectTest.Degree second_degree = SubjectTest.Degree.SECOND_DEGREE;
            String second_gradeText = subjectColumns.get(3).text();
            try {
                UnicapDataManager.persistSubjectGrade(code, second_degree, Float.parseFloat(second_gradeText));
            } catch (Exception ignore) {
                UnicapDataManager.persistSubjectGrade(code, second_degree, null);
            }

            String averageText = subjectColumns.get(4).text();
            try {
                UnicapDataManager.persistSubjectAverage(code, Float.parseFloat(averageText));
            } catch (Exception ignore) {
                UnicapDataManager.persistSubjectAverage(code, null);
            }

            SubjectTest.Degree final_degree = SubjectTest.Degree.FINAL_DEGREE;
            String final_gradeText = subjectColumns.get(5).text();
            try {
                UnicapDataManager.persistSubjectGrade(code, final_degree, Float.parseFloat(final_gradeText));
            } catch (Exception ignore) {
                UnicapDataManager.persistSubjectGrade(code, final_degree, null);
            }

            String final_averageText = subjectColumns.get(6).text();
            try {
                UnicapDataManager.persistSubjectFinalAverage(code, Float.parseFloat(final_averageText));
            } catch (Exception ignore) {
                UnicapDataManager.persistSubjectFinalAverage(code, null);
            }


        }

        ActiveAndroid.setTransactionSuccessful();
        ActiveAndroid.endTransaction();

    }

}
