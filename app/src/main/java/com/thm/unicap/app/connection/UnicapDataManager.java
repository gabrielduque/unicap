package com.thm.unicap.app.connection;

import com.activeandroid.query.Select;
import com.thm.unicap.app.model.Student;

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

    }
}
