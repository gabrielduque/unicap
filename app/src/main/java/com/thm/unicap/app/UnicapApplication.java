package com.thm.unicap.app;

import com.activeandroid.query.Select;
import com.thm.unicap.app.model.Student;

public class UnicapApplication extends com.activeandroid.app.Application {
    public static final String TAG = "Unicap";

    public static final int SESSION_DASHBOARD = 1;

    private static Student student;

    @Override
    public void onCreate() {
        super.onCreate();

        student = new Select().from(Student.class).executeSingle();
    }

    public static Student getStudent() {
        return student;
    }

    public static void setStudent(Student student) {
        UnicapApplication.student = student;
    }

    public static boolean isLogged() {
        return student != null;
    }
}
