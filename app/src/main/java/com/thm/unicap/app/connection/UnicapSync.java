package com.thm.unicap.app.connection;

import android.content.Context;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.thm.unicap.app.R;
import com.thm.unicap.app.model.Student;
import com.thm.unicap.app.model.Subject;
import com.thm.unicap.app.model.SubjectStatus;
import com.thm.unicap.app.model.SubjectTest;
import com.thm.unicap.app.util.UnicapUtils;
import com.thm.unicap.app.util.WordUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.SimpleDateFormat;
import java.util.Date;

public class UnicapSync {

    private static String actionURL;

    //TODO: port to use in UnicapSync
    public static void cleanDatabase() {
        new Delete().from(SubjectTest.class).execute();
        new Delete().from(SubjectStatus.class).execute();
        new Delete().from(Subject.class).execute();
        new Delete().from(Student.class).execute();
    }

    public static void loginRequest(Context context, String registration, String password) throws Exception {

        Document document;

        // Request to get temporary session to be used on login request
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

        if(document.select(".msg").text().matches(".*Matr.cula.*"))
            throw new UnicapSyncException(context.getString(R.string.error_incorrect_registration));
        else if(document.select(".msg").text().matches(".*Senha.*"))
            throw new UnicapSyncException(context.getString(R.string.error_incorrect_password));
        else if(document.select(".msg").text().matches(".*limite.*"))
            throw new UnicapSyncException(context.getString(R.string.error_max_tries_exceeded));

        actionURL = document.select("form").first().attr("action");
    }

    public static void receivePersonalData() throws Exception {

        if (actionURL == null) throw new Exception("Authenticate is required before any action.");

        // Personal data request
        Document document = Jsoup.connect(RequestUtils.REQUEST_BASE_URL + actionURL)
                .data(RequestUtils.Params.ROUTINE, RequestUtils.Values.ROUTINE_PERSONAL)
                .timeout(RequestUtils.REQUEST_TIMEOUT)
                .get();

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

    public static void receivePastSubjectsData() throws Exception {

        if (actionURL == null) throw new Exception("Authenticate is required before any action.");

        // Subjects data request
        Document document = Jsoup.connect(RequestUtils.REQUEST_BASE_URL + actionURL)
                .data(RequestUtils.Params.ROUTINE, RequestUtils.Values.ROUTINE_SUBJECTS_PAST)
                .timeout(RequestUtils.REQUEST_TIMEOUT)
                .get();

        Elements subjectsTable = document.select("table").get(6).select("tr");

        subjectsTable.remove(0); // Removing header

        // Using transactions to speed up the process
        ActiveAndroid.beginTransaction();

        for (Element subjectRow : subjectsTable) {

            Elements subjectColumns = subjectRow.select(".tab_texto");

            String code = subjectColumns.get(1).text();
            String name = UnicapUtils.replaceExceptions(WordUtils.capitalizeFully(subjectColumns.get(2).text()));
            String paidIn = subjectColumns.get(0).text();
            Float average = Float.parseFloat(subjectColumns.get(3).text());

            SubjectStatus.Situation situation;
            if (subjectColumns.get(4).text().equals("AP")) situation = SubjectStatus.Situation.APPROVED;
            else if (subjectColumns.get(4).text().equals("RM")) situation = SubjectStatus.Situation.REPROVED;
            else situation = SubjectStatus.Situation.UNKNOWN;

            UnicapDataManager.persistPastSubject(code, name, paidIn, average, situation);

        }

        ActiveAndroid.setTransactionSuccessful();
        ActiveAndroid.endTransaction();

    }

    public static void receiveActualSubjectsData() throws Exception {

        if (actionURL == null) throw new Exception("Authenticate is required before any action.");

        // Subjects data request
        Document document = Jsoup.connect(RequestUtils.REQUEST_BASE_URL + actionURL)
                .data(RequestUtils.Params.ROUTINE, RequestUtils.Values.ROUTINE_SUBJECTS_ACTUAL)
                .timeout(RequestUtils.REQUEST_TIMEOUT)
                .get();

        Elements subjectsTable = document.select("table").get(5).select("tr");

        subjectsTable.remove(0); // Removing header
        subjectsTable.remove(subjectsTable.size()-1); // Removing 'sum' row

        // Using transactions to speed up the process
        ActiveAndroid.beginTransaction();

        for (Element subjectRow : subjectsTable) {

            Elements subjectColumns = subjectRow.select(".tab_texto");

            String code = subjectColumns.get(0).text();
            String paidIn = document.select("table").get(4).select("td").get(1).text();
            String name = UnicapUtils.replaceExceptions(WordUtils.capitalizeFully(subjectColumns.get(1).text()));
            Integer workload = Integer.parseInt(subjectColumns.get(5).text());
            Integer credits = Integer.parseInt(subjectColumns.get(6).text());
            Integer period = Integer.parseInt(subjectColumns.get(7).text());
            SubjectStatus.Situation situation = SubjectStatus.Situation.ACTUAL;
            String team = subjectColumns.get(2).text();
            String room = subjectColumns.get(3).text();
            String schedule = subjectColumns.get(4).text();

            UnicapDataManager.persistActualSubject(code, paidIn, name, workload, credits, period, situation, team, room, schedule);
        }

        ActiveAndroid.setTransactionSuccessful();
        ActiveAndroid.endTransaction();

    }

    //TODO: port to use in UnicapSync
    public static void receivePendingSubjectsData() throws Exception {

        if (actionURL == null) throw new Exception("Authenticate is required before any action.");

        // Subjects data request
        Document document = Jsoup.connect(RequestUtils.REQUEST_BASE_URL + actionURL)
                .data(RequestUtils.Params.ROUTINE, RequestUtils.Values.ROUTINE_SUBJECTS_PENDING)
                .timeout(RequestUtils.REQUEST_TIMEOUT)
                .get();

        Elements subjectsTable = document.select("table").get(6).select("tr");

        subjectsTable.remove(0); // Removing header

        //TODO: refactor to support multiple accounts
        Student student = new Select().from(Student.class).executeSingle();

        // Using transactions to speed up the process
        ActiveAndroid.beginTransaction();

        for (Element subjectRow : subjectsTable) {

            Elements subjectColumns = subjectRow.select(".tab_texto");

            String code = subjectColumns.get(2).text();

            Subject subject = new Select().from(Subject.class).where("Code = ?", code).executeSingle();

            if(subject == null) {
                subject = new Subject();
                subject.code = code;
            }

            subject.student = student;
            subject.period = Integer.parseInt(subjectColumns.get(0).text());
            subject.name = UnicapUtils.replaceExceptions(WordUtils.capitalizeFully(subjectColumns.get(4).text()));
            subject.credits = Integer.parseInt(subjectColumns.get(5).text());
            subject.workload = Integer.parseInt(subjectColumns.get(6).text());

            subject.save();

            SubjectStatus subjectStatus = new SubjectStatus();

            subjectStatus.subject = subject;
            subjectStatus.situation = SubjectStatus.Situation.PENDING;

            subjectStatus.save();

        }

        ActiveAndroid.setTransactionSuccessful();
        ActiveAndroid.endTransaction();

    }

    public static void receiveSubjectsCalendarData() throws Exception {

        if (actionURL == null) throw new Exception("Authenticate is required before any action.");

        // Subjects data request
        Document document = Jsoup.connect(RequestUtils.REQUEST_BASE_URL + actionURL)
                .data(RequestUtils.Params.ROUTINE, RequestUtils.Values.ROUTINE_SUBJECTS_CALENDAR)
                .timeout(RequestUtils.REQUEST_TIMEOUT)
                .get();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Elements subjectsTable = document.select("table").get(5).select("tr");

        subjectsTable.remove(0); // Removing header

        // Using transactions to speed up the process
        ActiveAndroid.beginTransaction();

        for (Element subjectRow : subjectsTable) {

            Elements subjectColumns = subjectRow.select(".tab_texto");

            String code = subjectColumns.get(0).text();

            Date first_degree_date1 = simpleDateFormat.parse(subjectColumns.get(3).text());
            Date first_degree_date2 = simpleDateFormat.parse(subjectColumns.get(4).text());
            UnicapDataManager.persistSubjectCalendar(code, SubjectTest.Degree.FIRST_DEGREE, first_degree_date1, first_degree_date2);

            Date second_degree_date1 = simpleDateFormat.parse(subjectColumns.get(5).text());
            UnicapDataManager.persistSubjectCalendar(code, SubjectTest.Degree.SECOND_DEGREE, second_degree_date1, null); // SECOND_DEGREE doesn't have date2

            Date final_degree_date1 = simpleDateFormat.parse(subjectColumns.get(3).text());
            Date final_degree_date2 = simpleDateFormat.parse(subjectColumns.get(4).text());
            UnicapDataManager.persistSubjectCalendar(code, SubjectTest.Degree.FINAL_DEGREE, final_degree_date1, final_degree_date2);

        }

        ActiveAndroid.setTransactionSuccessful();
        ActiveAndroid.endTransaction();

    }

    public static void receiveSubjectsGradesData() throws Exception {

        if (actionURL == null) throw new Exception("Authenticate is required before any action.");

        // Subjects data request
        Document document = Jsoup.connect(RequestUtils.REQUEST_BASE_URL + actionURL)
                .data(RequestUtils.Params.ROUTINE, RequestUtils.Values.ROUTINE_SUBJECTS_TESTS)
                .timeout(RequestUtils.REQUEST_TIMEOUT)
                .get();

        Elements subjectsTable = document.select("table").get(7).select("tr");

        subjectsTable.remove(0); // Removing header

        // Using transactions to speed up the process
        ActiveAndroid.beginTransaction();

        for (Element subjectRow : subjectsTable) {

            Elements subjectColumns = subjectRow.select(".tab_texto");

            String code = subjectColumns.get(0).text();

            SubjectTest.Degree first_degree = SubjectTest.Degree.FIRST_DEGREE;
            String first_gradeText = subjectColumns.get(2).text();
            if(!first_gradeText.equals("--"))
                UnicapDataManager.persistSubjectGrade(code, first_degree, Float.parseFloat(first_gradeText));

            SubjectTest.Degree second_degree = SubjectTest.Degree.SECOND_DEGREE;
            String second_gradeText = subjectColumns.get(3).text();
            if(!second_gradeText.equals("--"))
                UnicapDataManager.persistSubjectGrade(code, second_degree, Float.parseFloat(second_gradeText));

            SubjectTest.Degree final_degree = SubjectTest.Degree.FINAL_DEGREE;
            String final_gradeText = subjectColumns.get(5).text();
            if(!final_gradeText.equals("--"))
                UnicapDataManager.persistSubjectGrade(code, final_degree, Float.parseFloat(final_gradeText));

        }

        ActiveAndroid.setTransactionSuccessful();
        ActiveAndroid.endTransaction();

    }

}
