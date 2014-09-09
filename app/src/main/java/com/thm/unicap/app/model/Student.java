package com.thm.unicap.app.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.From;
import com.activeandroid.query.Select;
import com.thm.unicap.app.util.HashUtils;
import com.thm.unicap.app.util.UnicapUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Table(name = "Student")
public class Student extends Model {

    @Column(name = "Registration", unique = true)
    public String registration;

    @Column(name = "Name")
    public String name;

    @Column(name = "Course")
    public String course;

    @Column(name = "Shift")
    public String shift;

    @Column(name = "Gender")
    public String gender;

    @Column(name = "Birthday")
    public String birthday;

    @Column(name = "Email")
    public String email;

    @Column(name = "Password")
    public String password;

    public List<Subject> getSubjects() {
        return getMany(Subject.class, "Student");
    }

    public String getGravatarURL(Integer pictureSize) {

        String emailAddress = email != null ? email : "";

        String md5Hash = HashUtils.MD5(emailAddress);

        if(md5Hash == null) md5Hash = "";

        return String.format("http://www.gravatar.com/avatar/%s?s=%s", md5Hash, String.valueOf(pictureSize));
    }

    public List<Subject> getActualSubjects() {

        return new Select("Subject.*")
                .from(Subject.class)
                .innerJoin(SubjectStatus.class)
                .on("Subject.Id = SubjectStatus.Subject")
                .where("Subject.Student = ?", getId())
                .orderBy("Subject.Period, SubjectStatus.PaidIn, Subject.Name")
                .where("SubjectStatus.Situation = ?", SubjectStatus.Situation.ACTUAL)
                .execute();
    }

    public List<SubjectTest> getSubjectTestsOrdered() {

        return new Select("SubjectTest.*")
                .from(SubjectTest.class)
                .innerJoin(Subject.class)
                .on("Subject.Id = SubjectTest.Subject")
                .innerJoin(SubjectStatus.class)
                .on("SubjectStatus.Subject = SubjectTest.Subject")
                .where("Subject.Student = ?", getId())
                .orderBy("SubjectTest.degree, SubjectTest.Date1, SubjectTest.Date2, Subject.Period, Subject.Name")
                .where("SubjectStatus.Situation = ?", SubjectStatus.Situation.ACTUAL)
                .execute();
    }

    public List<SubjectTest> getNewSubjectTests() {

        return new Select("SubjectTest.*")
                .from(SubjectTest.class)
                .innerJoin(Subject.class)
                .on("Subject.Id = SubjectTest.Subject")
                .innerJoin(SubjectStatus.class)
                .on("Subject.Id = SubjectStatus.Subject")
                .where("Subject.Student = ?", getId())
                .where("SubjectTest.Notify = ?", true)
                .orderBy("SubjectTest.Date1, SubjectTest.Date2, Subject.Period, Subject.Name")
                .where("SubjectStatus.Situation = ?", SubjectStatus.Situation.ACTUAL)
                .execute();
    }

    //TODO: Completely refactor this :x
    public List<Subject> getSubjectsFromWeekDay(SubjectStatus.ScheduleWeekDay scheduleWeekDay) {
        List<Subject> actualSubjects = getActualSubjects();

        HashMap<SubjectStatus.ScheduleHour, Subject> todaySubjects = new HashMap<SubjectStatus.ScheduleHour, Subject>();

        for (Subject subject : actualSubjects) {

            HashMap<SubjectStatus.ScheduleWeekDay, SubjectStatus.ScheduleHour> fullSchedule = subject.getActualSubjectStatus().getFullSchedule();

            if(!fullSchedule.containsKey(scheduleWeekDay))
                continue;

            todaySubjects.put(fullSchedule.get(scheduleWeekDay), subject);
        }

        List<SubjectStatus.ScheduleHour> orderedScheduleHours = new ArrayList<SubjectStatus.ScheduleHour>(todaySubjects.keySet());
        Collections.sort(orderedScheduleHours);

//        Comparator<SubjectStatus.ScheduleHour> comparator = new Comparator<SubjectStatus.ScheduleHour>() {
//            @Override
//            public int compare(SubjectStatus.ScheduleHour lhs, SubjectStatus.ScheduleHour rhs) {
//                return lhs.compareTo(rhs);
//            }
//        }

        List<Subject> sortedResultList = new ArrayList<Subject>();
        for (SubjectStatus.ScheduleHour scheduleHour : orderedScheduleHours) {
            sortedResultList.add(todaySubjects.get(scheduleHour));
        }

        return sortedResultList;
    }
}
