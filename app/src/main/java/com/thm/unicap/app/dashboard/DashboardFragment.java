package com.thm.unicap.app.dashboard;

import android.view.View;

import com.thm.unicap.app.R;
import com.thm.unicap.app.UnicapApplication;
import com.thm.unicap.app.lessons.WeekdayLessonsCard;
import com.thm.unicap.app.model.Student;
import com.thm.unicap.app.database.DatabaseDependentFragment;
import com.thm.unicap.app.util.UnicapUtils;

public class DashboardFragment extends DatabaseDependentFragment {

    public DashboardFragment() {
        super(R.layout.fragment_dashboard);
    }

    @Override
    public void initDatabaseDependentViews() {

        Student currentStudent = UnicapApplication.getCurrentStudent();

        WeekdayLessonsCard card_today_lessons = (WeekdayLessonsCard) getContentView().findViewById(R.id.card_today_lessons);
        StudentCoefficientCard card_course_coefficient = (StudentCoefficientCard) getContentView().findViewById(R.id.card_course_coefficient);
        StudentSituationGraphCard card_status_graph = (StudentSituationGraphCard) getContentView().findViewById(R.id.card_status_graph);

        card_today_lessons.setStudent(UnicapApplication.getCurrentStudent());
        card_today_lessons.setScheduleWeekDay(UnicapUtils.getCurrentScheduleWeekDay());

        if (currentStudent.lastCoefficient != null && currentStudent.courseCoefficient != null) {
            card_course_coefficient.setStudent(currentStudent);
            card_course_coefficient.setVisibility(View.VISIBLE);
        }

        card_status_graph.setStudent(UnicapApplication.getCurrentStudent());
    }

}