package com.thm.unicap.app.connection;

import android.util.Log;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.thm.unicap.app.UnicapApplication;
import com.thm.unicap.app.model.Student;
import com.thm.unicap.app.model.Subject;
import com.thm.unicap.app.model.SubjectStatus;
import com.thm.unicap.app.model.SubjectTest;
import com.thm.unicap.app.util.UnicapUtils;

import java.util.Date;

public class UnicapDataManager {

    public static void cleanDatabase() {
        new Delete().from(Student.class).execute();
    }

    public static void cleanUserData(String registration) {

        Student student = new Select().from(Student.class).where("Registration = ?", registration).executeSingle();

        if(student != null)
            student.delete(); // Models configured with cascade delete
    }

    public static void initStudent(String registration, String password) {
        Student student = new Select().from(Student.class).where("Registration = ?", registration).executeSingle();

        if(student == null) {
            student = new Student();
            student.registration = registration;
            student.password = password;
            student.save();
        }

    }

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

        UnicapApplication.setCurrentStudent(student);

    }

    public static void persistPastSubject(String code, String name, String paidIn, Float average, SubjectStatus.Situation situation) {

        Subject subject = new Select().from(Subject.class)
                .where("Code = ?", code)
                .where("Student = ?", UnicapApplication.getCurrentStudent().getId())
                .executeSingle();

        if(subject == null) {
            subject = new Subject();
            subject.code = code;
            subject.student = UnicapApplication.getCurrentStudent();
        }

        subject.name = name;

        subject.save();

        SubjectStatus subjectStatus = new Select().from(SubjectStatus.class)
                .where("Subject = ?", subject.getId())
                .where("PaidIn = ?", paidIn)
                .executeSingle();

        if(subjectStatus == null) {
            subjectStatus = new SubjectStatus();
            subjectStatus.subject = subject;
            subjectStatus.paidIn = paidIn;
        }

        subjectStatus.average = average;
        subjectStatus.situation = situation;

        subjectStatus.save();

    }

    public static void persistActualSubject(String code, String paidIn, String name, Integer workload, Integer credits, Integer period, String team, String room, String schedule) {

        Subject subject = new Select().from(Subject.class)
                .where("Code = ?", code)
                .where("Student = ?", UnicapApplication.getCurrentStudent().getId())
                .executeSingle();

        if(subject == null) {
            subject = new Subject();
            subject.code = code;
            subject.student = UnicapApplication.getCurrentStudent();
        }

        subject.name = name;
        subject.workload = workload;
        subject.credits = credits;
        subject.period = period;

        subject.save();

        SubjectStatus subjectStatus = new Select().from(SubjectStatus.class)
                .where("Subject = ?", subject.getId())
                .where("PaidIn = ?", paidIn)
                .executeSingle();

        if(subjectStatus == null) {
            subjectStatus = new SubjectStatus();
            subjectStatus.subject = subject;
            subjectStatus.paidIn = paidIn;
        } else {

            // Merging room from different rows, but same subject
            if (!subjectStatus.room.contains(room))
                room = subjectStatus.room + "-" + room;

            // Merging schedule from different rows, but same subject
            if (!subjectStatus.schedule.contains(schedule))
                schedule = subjectStatus.schedule + " " + schedule;
        }

        subjectStatus.situation = SubjectStatus.Situation.ACTUAL;
        subjectStatus.team = team;
        subjectStatus.room = room;
        subjectStatus.schedule = schedule;

        subjectStatus.save();
    }

    public static void persistPendingSubject(String code, int period, String name, int credits, int workload) {

        Subject subject = new Select().from(Subject.class)
                .where("Code = ?", code)
                .where("Student = ?", UnicapApplication.getCurrentStudent().getId())
                .executeSingle();

        if(subject == null) {
            subject = new Subject();
            subject.code = code;
            subject.student = UnicapApplication.getCurrentStudent();
        }

        subject.period = period;
        subject.name = name;
        subject.credits = credits;
        subject.workload = workload;

        subject.save();

        SubjectStatus subjectStatus = new Select().from(SubjectStatus.class)
                .where("Subject = ?", subject.getId())
                .where("Situation = ?", SubjectStatus.Situation.PENDING)
                .executeSingle();

        if(subjectStatus == null) {
            subjectStatus = new SubjectStatus();
            subjectStatus.subject = subject;
            subjectStatus.situation = SubjectStatus.Situation.PENDING;
        }

        subjectStatus.subject = subject;

        subjectStatus.save();
    }

    public static void persistSubjectCalendar(String code, SubjectTest.Degree degree, Date date1, Date date2) {

        Subject subject = new Select().from(Subject.class)
                .where("Code = ?", code)
                .where("Student = ?", UnicapApplication.getCurrentStudent().getId())
                .executeSingle();

        if(subject != null) {
            SubjectTest subjectTest = subject.getTestByDegree(degree);

            if(subjectTest == null) {
                subjectTest = new SubjectTest();
                subjectTest.subject = subject;
                subjectTest.degree = degree;
                subjectTest.first_time = true;
            }
            subjectTest.date1 = date1;
            subjectTest.date2 = date2;
            subjectTest.save();
        }

    }

    public static void persistSubjectGrade(String code, SubjectTest.Degree degree, Float grade) {

        Subject subject = new Select().from(Subject.class)
                .where("Code = ?", code)
                .where("Student = ?", UnicapApplication.getCurrentStudent().getId())
                .executeSingle();

        if(subject != null) {
            SubjectTest subjectTest = subject.getTestByDegree(degree);

            if(subjectTest != null) {
                if(grade != null && !grade.equals(subjectTest.grade) && !subjectTest.first_time) {
                    subjectTest.notify = true;
                }
                subjectTest.grade = grade;
                subjectTest.first_time = false;
                subjectTest.save();
            }

        }

    }

    public static void persistSubjectAverage(String code, Float grade) {

        Subject subject = new Select().from(Subject.class)
                .where("Code = ?", code)
                .where("Student = ?", UnicapApplication.getCurrentStudent().getId())
                .executeSingle();

        if(subject != null) {
            SubjectStatus actualSubjectStatus = subject.getActualSubjectStatus();

            if (actualSubjectStatus != null) {
                actualSubjectStatus.average = grade;
                actualSubjectStatus.save();
            }
        }

    }

    public static void persistSubjectFinalAverage(String code, Float grade) {

        Subject subject = new Select().from(Subject.class)
                .where("Code = ?", code)
                .where("Student = ?", UnicapApplication.getCurrentStudent().getId())
                .executeSingle();

        if(subject != null) {
            SubjectStatus actualSubjectStatus = subject.getActualSubjectStatus();

            if (actualSubjectStatus != null) {
                actualSubjectStatus.final_average = grade;
                actualSubjectStatus.save();
            }
        }

    }

    public static void setSubjectFlowSituation(String code, SubjectStatus.FlowSituation flowSituation) {
        Subject subject = new Select().from(Subject.class)
                .where("Code = ?", code)
                .where("Student = ?", UnicapApplication.getCurrentStudent().getId())
                .executeSingle();

        if(subject != null) {
            SubjectStatus actualSubjectStatus = subject.getActualSubjectStatus();

            if (actualSubjectStatus != null) {
                actualSubjectStatus.flowSituation = flowSituation;
                actualSubjectStatus.save();
            }
        }
    }
}
