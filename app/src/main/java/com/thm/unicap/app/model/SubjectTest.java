package com.thm.unicap.app.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.Date;

@Table(name = "SubjectTest")
public class SubjectTest extends Model {

    public enum Degree {
        FIRST_DEGREE, SECOND_DEGREE, FINAL_DEGREE
    }

    @Column(name = "Subject")
    public Subject subject;

    @Column(name = "Degree")
    public Degree degree;

    @Column(name = "Date1")
    public Date date1;

    @Column(name = "Date2")
    public Date date2;

    @Column(name = "Grade")
    public Float grade;

}
