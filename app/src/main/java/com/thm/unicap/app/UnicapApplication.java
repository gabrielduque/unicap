package com.thm.unicap.app;

import com.thm.unicap.app.model.Student;
import com.thm.unicap.app.util.StudentListener;

import java.util.ArrayList;

public class UnicapApplication extends com.activeandroid.app.Application {
    public static final String TAG = "Unicap";

    private static Student student = null;
    private static ArrayList<StudentListener> mStudentListeners;

    @Override
    public void onCreate() {
        super.onCreate();
        mStudentListeners = new ArrayList<StudentListener>();
    }

    public static Student getStudent() {
        return student;
    }

    public static void setStudent(Student student) {

        UnicapApplication.student = student;

        if(student != null)
            for (StudentListener listener : mStudentListeners)
                listener.studentChanged();
    }

    public static boolean isLogged() {
        return student != null;
    }

    public static void addStudentListener(StudentListener listener) {
        if(!mStudentListeners.contains(listener))
            mStudentListeners.add(listener);
    }

    public static void removeStudentListener(StudentListener listener) {
        mStudentListeners.remove(listener);
    }
}
