package com.thm.unicap.app.connection;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Delete;
import com.thm.unicap.app.model.Student;
import com.thm.unicap.app.model.Subject;
import com.thm.unicap.app.model.SubjectStatus;
import com.thm.unicap.app.util.UnicapUtils;
import com.thm.unicap.app.util.WordUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class UnicapConnector {

    private static String loginUrl;
    private static String actionUrl;

    public static void cleanDatabase() {
        new Delete().from(Student.class).execute();
        new Delete().from(Subject.class).execute();
        new Delete().from(SubjectStatus.class).execute();
    }

    public static void prepareLoginRequest() throws IOException {
        // Request to get temporary session to be used on login request
        Document document = Jsoup.connect(RequestUtils.REQUEST_BASE_URL)
                .timeout(RequestUtils.REQUEST_TIMEOUT)
                .get();

        loginUrl = document.select("form").first().attr("action");
    }

    public static void loginRequest(String registration, String password) throws Exception {

        if (loginUrl == null) throw new Exception("Prepare login before trying to call this.");

        // Request to get permanent session to all future requests
        Document document = Jsoup.connect(RequestUtils.REQUEST_BASE_URL + loginUrl)
                .timeout(RequestUtils.REQUEST_TIMEOUT)
                .data(RequestUtils.Params.ROUTINE, RequestUtils.Values.ROUTINE_LOGIN)
                .data(RequestUtils.Params.REGISTRATION, registration.substring(0, 9))
                .data(RequestUtils.Params.DIGIT, registration.substring(10))
                .data(RequestUtils.Params.PASSWORD, password)
                .get();

        actionUrl = document.select("form").first().attr("action");
    }

    public static void receivePersonalData() throws Exception {

        if (actionUrl == null) throw new Exception("Authenticate is required before any action.");

        // Personal data request
        Document personal = Jsoup.connect(RequestUtils.REQUEST_BASE_URL + actionUrl)
                .data(RequestUtils.Params.ROUTINE, RequestUtils.Values.ROUTINE_PERSONAL)
                .get();

        Student student = new Student();
        student.registration = personal.select(".tab_texto").get(0).text();
        student.name = WordUtils.capitalizeFully(personal.select(".tab_texto").get(1).text(), null);
        student.course = UnicapUtils.replaceExceptions(WordUtils.capitalizeFully(personal.select(".tab_texto").get(2).text()).replace("Curso De ", ""));
        student.shift = personal.select(".tab_texto").get(4).text();
        student.gender = WordUtils.capitalizeFully(personal.select(".tab_texto").get(5).text(), null);
        student.birthday = WordUtils.capitalizeFully(personal.select(".tab_texto").get(6).text(), null);
        student.email = personal.select(".tab_texto").get(20).text();

        student.save();

    }

    public static void receiveSubjectsActualData() throws Exception {

        if (actionUrl == null) throw new Exception("Authenticate is required before any action.");

        // Subjects data request
        Document personal = Jsoup.connect(RequestUtils.REQUEST_BASE_URL + actionUrl)
                .data(RequestUtils.Params.ROUTINE, RequestUtils.Values.ROUTINE_SUBJECTS_ACTUAL)
                .get();

        Elements subjectsTable = personal.select("table").get(5).select("tr");

        subjectsTable.remove(0); // Removing header
        subjectsTable.remove(subjectsTable.size()-1); // Removing 'sum' row

        ActiveAndroid.beginTransaction();

        for (Element subjectRow : subjectsTable) {

            Elements subjectColumns = subjectRow.select(".tab_texto");

            Subject subject = new Subject();

            subject.code = subjectColumns.get(0).text();
            subject.name = UnicapUtils.replaceExceptions(WordUtils.capitalizeFully(subjectColumns.get(1).text()));
            subject.workload = subjectColumns.get(5).text();
            subject.credits = Integer.parseInt(subjectColumns.get(6).text());
            subject.period = Integer.parseInt(subjectColumns.get(7).text());

            subject.save();

            SubjectStatus subjectStatus = new SubjectStatus();

            subjectStatus.subject = subject;
            subjectStatus.situation = SubjectStatus.Situation.ACTUAL;
            subjectStatus.team = subjectColumns.get(2).text();
            subjectStatus.room = subjectColumns.get(3).text();
            subjectStatus.schedule = subjectColumns.get(4).text();

            subjectStatus.save();

        }

        ActiveAndroid.setTransactionSuccessful();
        ActiveAndroid.endTransaction();

    }

}
