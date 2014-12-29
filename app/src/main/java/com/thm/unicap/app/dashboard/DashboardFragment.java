package com.thm.unicap.app.dashboard;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.devspark.progressfragment.ProgressFragment;
import com.thm.unicap.app.R;
import com.thm.unicap.app.UnicapApplication;
import com.thm.unicap.app.lessons.WeekdayLessonsCard;
import com.thm.unicap.app.model.Student;
import com.thm.unicap.app.util.DatabaseDependentFragment;
import com.thm.unicap.app.util.DatabaseListener;
import com.thm.unicap.app.util.NetworkUtils;
import com.thm.unicap.app.util.UnicapUtils;

public class DashboardFragment extends ProgressFragment implements DatabaseListener, DatabaseDependentFragment {

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

    @Override
    public void onResume() {
        super.onResume();
        UnicapApplication.addDatabaseListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        UnicapApplication.removeDatabaseListener(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setHasOptionsMenu(true);

        if(UnicapApplication.hasStudentData()) { // Show offline data
            databaseUpdated();
        } else if (!NetworkUtils.isNetworkConnected(getActivity())) { // Show layout for offline error
            setContentView(R.layout.content_offline);
            setContentShown(true);
        }
    }

    @Override
    public void databaseSyncing() {

    }

    @Override
    public void databaseUpdated() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setContentView(R.layout.fragment_dashboard);
                initDatabaseDependentViews();
                setContentShown(true);
            }
        });
    }

    @Override
    public void databaseUnreachable(String message) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setContentView(R.layout.content_unreachable);
                setContentShown(true);
            }
        });
    }
}