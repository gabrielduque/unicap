package com.thm.unicap.app.connection;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
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

public class UnicapSync {

    private static String actionURL;

    //TODO: port to use in UnicapSync
    public static void cleanDatabase() {
        new Delete().from(SubjectTest.class).execute();
        new Delete().from(SubjectStatus.class).execute();
        new Delete().from(Subject.class).execute();
        new Delete().from(Student.class).execute();
    }

    public static void loginRequest(String registration, String password) throws Exception {

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

    //TODO: port to use in UnicapSync
    public static void receiveSubjectsPastData() throws Exception {

        if (actionURL == null) throw new Exception("Authenticate is required before any action.");

        // Subjects data request
        Document document = Jsoup.connect(RequestUtils.REQUEST_BASE_URL + actionURL)
                .data(RequestUtils.Params.ROUTINE, RequestUtils.Values.ROUTINE_SUBJECTS_PAST)
                .timeout(RequestUtils.REQUEST_TIMEOUT)
                .get();

        Elements subjectsTable = document.select("table").get(6).select("tr");

        subjectsTable.remove(0); // Removing header

        //TODO: refactor to support multipls accounts
        Student student = new Select().from(Student.class).executeSingle();

        // Using transactions to speed up the process
        ActiveAndroid.beginTransaction();

        for (Element subjectRow : subjectsTable) {

            Elements subjectColumns = subjectRow.select(".tab_texto");

            String code = subjectColumns.get(1).text();

            Subject subject = new Select().from(Subject.class).where("Code = ?", code).executeSingle();

            if(subject == null) {
                subject = new Subject();
                subject.code = code;
            }

            subject.student = student;
            subject.name = UnicapUtils.replaceExceptions(WordUtils.capitalizeFully(subjectColumns.get(2).text()));

            subject.save();

            SubjectStatus subjectStatus = new SubjectStatus();

            subjectStatus.subject = subject;
            subjectStatus.paidIn = subjectColumns.get(0).text();
            subjectStatus.average = Float.parseFloat(subjectColumns.get(3).text());

            String situation = subjectColumns.get(4).text();

            if (situation.equals("AP")) subjectStatus.situation = SubjectStatus.Situation.APPROVED;
            else if (situation.equals("RM")) subjectStatus.situation = SubjectStatus.Situation.REPROVED;
            else subjectStatus.situation = SubjectStatus.Situation.UNKNOWN;

            subjectStatus.save();

        }

        ActiveAndroid.setTransactionSuccessful();
        ActiveAndroid.endTransaction();

    }

    //TODO: port to use in UnicapSync
    public static void receiveSubjectsActualData() throws Exception {

        if (actionURL == null) throw new Exception("Authenticate is required before any action.");

        // Subjects data request
        Document document = Jsoup.connect(RequestUtils.REQUEST_BASE_URL + actionURL)
                .data(RequestUtils.Params.ROUTINE, RequestUtils.Values.ROUTINE_SUBJECTS_ACTUAL)
                .timeout(RequestUtils.REQUEST_TIMEOUT)
                .get();

        String paidIn = document.select("table").get(4).select("td").get(1).text();
        Elements subjectsTable = document.select("table").get(5).select("tr");

        subjectsTable.remove(0); // Removing header
        subjectsTable.remove(subjectsTable.size()-1); // Removing 'sum' row

        //TODO: refactor to support multipls accounts
        Student student = new Select().from(Student.class).executeSingle();

        // Using transactions to speed up the process
        ActiveAndroid.beginTransaction();

        for (Element subjectRow : subjectsTable) {

            Elements subjectColumns = subjectRow.select(".tab_texto");

            String code = subjectColumns.get(0).text();

            Subject subject = new Select().from(Subject.class).where("Code = ?", code).executeSingle();

            if(subject == null) {
                subject = new Subject();
                subject.code = code;
            }

            subject.student = student;
            subject.name = UnicapUtils.replaceExceptions(WordUtils.capitalizeFully(subjectColumns.get(1).text()));
            subject.workload = Integer.parseInt(subjectColumns.get(5).text());
            subject.credits = Integer.parseInt(subjectColumns.get(6).text());
            subject.period = Integer.parseInt(subjectColumns.get(7).text());

            subject.save();

            SubjectStatus subjectStatus = new SubjectStatus();

            subjectStatus.subject = subject;
            subjectStatus.situation = SubjectStatus.Situation.ACTUAL;
            subjectStatus.team = subjectColumns.get(2).text();
            subjectStatus.room = subjectColumns.get(3).text();
            subjectStatus.schedule = subjectColumns.get(4).text();
            subjectStatus.paidIn = paidIn;

            subjectStatus.save();

        }

        ActiveAndroid.setTransactionSuccessful();
        ActiveAndroid.endTransaction();

    }

    //TODO: port to use in UnicapSync
    public static void receiveSubjectsPendingData() throws Exception {

        if (actionURL == null) throw new Exception("Authenticate is required before any action.");

        // Subjects data request
        Document document = Jsoup.connect(RequestUtils.REQUEST_BASE_URL + actionURL)
                .data(RequestUtils.Params.ROUTINE, RequestUtils.Values.ROUTINE_SUBJECTS_PENDING)
                .timeout(RequestUtils.REQUEST_TIMEOUT)
                .get();

        Elements subjectsTable = document.select("table").get(6).select("tr");

        subjectsTable.remove(0); // Removing header

        //TODO: refactor to support multipls accounts
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

    //TODO: port to use in UnicapSync
    public static void receiveSubjectsCalendarData() throws Exception {

        if (actionURL == null) throw new Exception("Authenticate is required before any action.");

        // Subjects data request
        Document document = Jsoup.connect(RequestUtils.REQUEST_BASE_URL + actionURL)
                .data(RequestUtils.Params.ROUTINE, RequestUtils.Values.ROUTINE_SUBJECTS_CALENDAR)
                .timeout(RequestUtils.REQUEST_TIMEOUT)
                .get();

        Elements subjectsTable = document.select("table").get(5).select("tr");

        subjectsTable.remove(0); // Removing header

        // Using transactions to speed up the process
        ActiveAndroid.beginTransaction();

        for (Element subjectRow : subjectsTable) {

            Elements subjectColumns = subjectRow.select(".tab_texto");

            String code = subjectColumns.get(0).text();

            Subject subject = new Select().from(Subject.class).where("Code = ?", code).executeSingle();

            if(subject != null) {

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

                SubjectTest firstDegree = new SubjectTest();
                firstDegree.subject = subject;
                firstDegree.degree = SubjectTest.Degree.FIRST_DEGREE;
                firstDegree.date1 = simpleDateFormat.parse(subjectColumns.get(3).text());
                firstDegree.date2 = simpleDateFormat.parse(subjectColumns.get(4).text());
                firstDegree.save();

                SubjectTest secondDegree = new SubjectTest();
                secondDegree.subject = subject;
                secondDegree.degree = SubjectTest.Degree.SECOND_DEGREE;
                secondDegree.date1 = simpleDateFormat.parse(subjectColumns.get(5).text());
                secondDegree.save();

                SubjectTest finalDegree = new SubjectTest();
                finalDegree.subject = subject;
                finalDegree.degree = SubjectTest.Degree.FINAL_DEGREE;
                finalDegree.date1 = simpleDateFormat.parse(subjectColumns.get(6).text());
                finalDegree.date2 = simpleDateFormat.parse(subjectColumns.get(7).text());
                finalDegree.save();

            }

        }

        ActiveAndroid.setTransactionSuccessful();
        ActiveAndroid.endTransaction();

    }

    //TODO: port to use in UnicapSync
    public static void receiveSubjectsTestsData() throws Exception {

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

            Subject subject = new Select().from(Subject.class).where("Code = ?", code).executeSingle();

            if(subject != null) {

                SubjectTest subjectTest;
                String gradeText;

                subjectTest = subject.getTestByDegree(SubjectTest.Degree.FIRST_DEGREE);
                gradeText = subjectColumns.get(2).text();

                if(subjectTest != null && !gradeText.equals("--")) {
                    subjectTest.grade = Float.parseFloat(gradeText);
                    subjectTest.save();
                }

                subjectTest = subject.getTestByDegree(SubjectTest.Degree.SECOND_DEGREE);
                gradeText = subjectColumns.get(3).text();

                if(subjectTest != null && !gradeText.equals("--")) {
                    subjectTest.grade = Float.parseFloat(gradeText);
                    subjectTest.save();
                }

                subjectTest = subject.getTestByDegree(SubjectTest.Degree.FINAL_DEGREE);
                gradeText = subjectColumns.get(5).text();

                if(subjectTest != null && !gradeText.equals("--")) {
                    subjectTest.grade = Float.parseFloat(gradeText);
                    subjectTest.save();
                }

            }

        }

        ActiveAndroid.setTransactionSuccessful();
        ActiveAndroid.endTransaction();

    }

}
