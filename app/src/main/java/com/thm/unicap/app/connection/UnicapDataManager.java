package com.thm.unicap.app.connection;

import com.activeandroid.query.Select;
import com.thm.unicap.app.UnicapApplication;
import com.thm.unicap.app.model.Student;
import com.thm.unicap.app.model.Subject;
import com.thm.unicap.app.model.SubjectStatus;
import com.thm.unicap.app.model.SubjectTest;
import com.thm.unicap.app.util.UnicapUtils;
import com.thm.unicap.app.util.WordUtils;

import java.util.Date;

public class UnicapDataManager {

    public static void persistPersonalData(String registration, String name, String course, String shift, String gender, String birthday, String email) {

        Student student = new Select().from(Student.class).where("Registration = ?", registration).executeSingle();

        if(student == null) {
            student = new Student();
            student.registration = registration;
        }

        student.name = name;
        student.course = course;
        student.shift = shift;
        student.gender = gender;
        student.birthday = birthday;
        student.email = email;

        student.save();

        UnicapApplication.setStudent(student);

    }

    public static void persistPastSubject(String code, String name, String paidIn, Float average, SubjectStatus.Situation situation) {

        Subject subject = new Select().from(Subject.class).where("Code = ?", code).executeSingle();

        if(subject == null) {
            subject = new Subject();
            subject.code = code;
        }

        subject.student = UnicapApplication.getStudent();
        subject.name = name;

        subject.save();

        SubjectStatus subjectStatus = new SubjectStatus();

        subjectStatus.subject = subject;
        subjectStatus.paidIn = paidIn;
        subjectStatus.average = average;
        subjectStatus.situation = situation;

        subjectStatus.save();

    }

    public static void persistActualSubject(String code, String paidIn, String name, Integer workload, Integer credits, Integer period, SubjectStatus.Situation situation, String team, String room, String schedule) {

        Subject subject = new Select().from(Subject.class).where("Code = ?", code).executeSingle();

        if(subject == null) {
            subject = new Subject();
            subject.code = code;
        }

        subject.student = UnicapApplication.getStudent();
        subject.name = name;
        subject.workload = workload;
        subject.credits = credits;
        subject.period = period;

        subject.save();

        SubjectStatus subjectStatus = new SubjectStatus();

        subjectStatus.subject = subject;
        subjectStatus.situation = SubjectStatus.Situation.ACTUAL;
        subjectStatus.team = team;
        subjectStatus.room = room;
        subjectStatus.schedule = schedule;
        subjectStatus.paidIn = paidIn;

        subjectStatus.save();
    }

    public static void persistSubjectCalendar(String code, SubjectTest.Degree degree, Date date1, Date date2) {

        Subject subject = new Select().from(Subject.class).where("Code = ?", code).executeSingle();

        if(subject != null) {
            SubjectTest subjectTest = new SubjectTest();
            subjectTest.subject = subject;
            subjectTest.degree = degree;
            subjectTest.date1 = date1;
            subjectTest.date2 = date2;
            subjectTest.save();
        }

    }

    public static void persistSubjectGrade(String code, SubjectTest.Degree degree, Float grade) {

        Subject subject = new Select().from(Subject.class).where("Code = ?", code).executeSingle();

        if(subject != null) {
            SubjectTest subjectTest = subject.getTestByDegree(degree);

            if(subjectTest != null) {
                subjectTest.grade = grade;
                subjectTest.save();
            }

        }

    }
}
