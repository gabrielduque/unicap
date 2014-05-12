package com.thm.unicap.app.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.List;

@Table(name = "Students")
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

    public List<Subject> getSubjects() {
        return getMany(Subject.class, "Student");
    }
}
