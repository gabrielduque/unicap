package com.thm.unicap.app.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.thm.unicap.app.util.HashUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

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

    @Column(name = "CourseCoefficient")
    public Float courseCoefficient;

    @Column(name = "LastCoefficient")
    public Float lastCoefficient;

    public List<Subject> getSubjects() {
        return getMany(Subject.class, "Student");
    }

    public String getGravatarURL(Integer pictureSize) {

        String emailAddress = email != null ? email : "";

        String md5Hash = HashUtils.MD5(emailAddress);

        if(md5Hash == null) md5Hash = "";

        return String.format("http://www.gravatar.com/avatar/%s?s=%s&d=404", md5Hash, String.valueOf(pictureSize));
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
                .orderBy("CASE SubjectTest.degree " +
                        "WHEN 'FIRST_DEGREE' THEN 0 " +
                        "WHEN 'SECOND_DEGREE' THEN 1 " +
                        "WHEN 'FINAL_DEGREE' THEN 2 " +
                        "END," +
                        "SubjectTest.Date1, " +
                        "SubjectTest.Date2, " +
                        "Subject.Period, " +
                        "Subject.Name")
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
    public List<Subject> getSubjectsFromWeekDay(SubjectStatus.ScheduleWeekday scheduleWeekday) {
        List<Subject> actualSubjects = getActualSubjects();

        HashMap<Character, Subject> todaySubjects = new HashMap<Character, Subject>();

        for (Subject subject : actualSubjects) {

            HashMap<SubjectStatus.ScheduleWeekday, char[]> fullSchedule = subject.getActualSubjectStatus().getFullSchedule();

            if(!fullSchedule.containsKey(scheduleWeekday))
                continue;

            char[] todaySubjectHour = fullSchedule.get(scheduleWeekday);

            if(todaySubjectHour.length > 0)
                todaySubjects.put(todaySubjectHour[0], subject);
        }

        List<Character> orderedScheduleBeginHours = new ArrayList<Character>(todaySubjects.keySet());
        Collections.sort(orderedScheduleBeginHours);

//        Comparator<SubjectStatus.ScheduleHour> comparator = new Comparator<SubjectStatus.ScheduleHour>() {
//            @Override
//            public int compare(SubjectStatus.ScheduleHour lhs, SubjectStatus.ScheduleHour rhs) {
//                return lhs.compareTo(rhs);
//            }
//        }

        List<Subject> sortedResultList = new ArrayList<Subject>();
        for (Character scheduleBeginHour : orderedScheduleBeginHours) {
            sortedResultList.add(todaySubjects.get(scheduleBeginHour));
        }

        return sortedResultList;
    }

}
