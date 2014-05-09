package com.thm.unicap.app.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/*
Student
Subject
Class
Test
BankSlip
 */

@Table(name = "Students")
public class Student extends Model {

    @Column(name = "Registration")
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

}
