package com.thm.unicap.app.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.Date;

@Table(name = "SubjectTest")
public class SubjectTest extends Model {

    public static final Float MIN_AVERAGE = 5.0f;

    public enum Degree {
        FIRST_DEGREE, SECOND_DEGREE, FINAL_DEGREE
    }

    @Column(name = "Subject", onDelete = Column.ForeignKeyAction.CASCADE)
    public Subject subject;

    @Column(name = "Degree")
    public Degree degree;

    @Column(name = "Date1")
    public Date date1;

    @Column(name = "Date2")
    public Date date2;

    @Column(name = "Grade")
    public Float grade;

    @Column(name = "Notify")
    public boolean notify;

   public static Float calculateGrade(Float firstDegree, Float secondDegree, Float finalDegree) {
       if(firstDegree != null && secondDegree == null && finalDegree != null) {
           //Final como segundo GQ
           return (firstDegree * 2 + finalDegree * 3) / 5;
       } else if(firstDegree != null && secondDegree != null && finalDegree == null) {
           //Normal
           return (firstDegree * 2 + secondDegree * 3) / 5;
       } else if(firstDegree != null && secondDegree != null && finalDegree != null) {
           //Final
           return ((firstDegree * 2 + secondDegree * 3) / 5 + finalDegree) / 2;
       } else {
           return null;
       }
   }

}
