package com.thm.unicap.app.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.List;

@Table(name = "Subject")
public class Subject extends Model {

    @Column(name = "Student", onDelete = Column.ForeignKeyAction.CASCADE)
    public Student student;

    @Column(name = "Code", unique = true)
    public String code;

    @Column(name = "Name")
    public String name;

    @Column(name = "Workload")
    public Integer workload;

    @Column(name = "Credits")
    public Integer credits;

    @Column(name = "Period")
    public Integer period;

    public List<SubjectStatus> getSubjectStatus() {
        return getMany(SubjectStatus.class, "Subject");
    }

    public List<SubjectTest> getSubjectTests() {
        return getMany(SubjectTest.class, "Subject");
    }

    public SubjectTest getTestByDegree(SubjectTest.Degree degree) {
        List<SubjectTest> subjectTests = getMany(SubjectTest.class, "Subject");

        for (SubjectTest subjectTest : subjectTests) {
            if (subjectTest.degree == degree) return subjectTest;
        }

        return null;
    }
}
