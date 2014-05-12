package com.thm.unicap.app.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.List;

@Table(name = "Subjects")
public class Subject extends Model {

    @Column(name = "Student")
    public Student student;

    @Column(name = "Code", unique = true)
    public String code;

    @Column(name = "Name")
    public String name;

    @Column(name = "Workload")
    public String workload;

    @Column(name = "Credits")
    public Integer credits;

    @Column(name = "Period")
    public Integer period;

    public List<SubjectStatus> getSubjectStatus() {
        return getMany(SubjectStatus.class, "Subject");
    }
}
