package com.thm.unicap.app.lessons;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuInflater;

import com.devspark.progressfragment.ProgressFragment;
import com.thm.unicap.app.MainActivity;
import com.thm.unicap.app.R;
import com.thm.unicap.app.UnicapApplication;
import com.thm.unicap.app.menu.NavigationDrawerFragment;
import com.thm.unicap.app.model.SubjectStatus;
import com.thm.unicap.app.util.DatabaseDependentFragment;
import com.thm.unicap.app.util.DatabaseListener;
import com.thm.unicap.app.util.NetworkUtils;

public class LessonsFragment extends ProgressFragment implements DatabaseListener, DatabaseDependentFragment {

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        NavigationDrawerFragment navigationDrawerFragment = ((MainActivity) getActivity()).getNavigationDrawerFragment();

        if(navigationDrawerFragment != null && !navigationDrawerFragment.isDrawerOpen()) {
            inflater.inflate(R.menu.fragment_lessons, menu);
        }
        super.onCreateOptionsMenu(menu, inflater);
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
    public void initDatabaseDependentViews() {

        WeekdayLessonsCard weekdayLessonsCard;

        // Card: Mon

        weekdayLessonsCard = (WeekdayLessonsCard) getContentView().findViewById(R.id.card_lessons_mon);
        weekdayLessonsCard.setStudent(UnicapApplication.getCurrentStudent());
        weekdayLessonsCard.setScheduleWeekDay(SubjectStatus.ScheduleWeekday.Mon);

        // Card: Tue

        weekdayLessonsCard = (WeekdayLessonsCard) getContentView().findViewById(R.id.card_lessons_tue);
        weekdayLessonsCard.setStudent(UnicapApplication.getCurrentStudent());
        weekdayLessonsCard.setScheduleWeekDay(SubjectStatus.ScheduleWeekday.Tue);

        // Card: Wed

        weekdayLessonsCard = (WeekdayLessonsCard) getContentView().findViewById(R.id.card_lessons_wed);
        weekdayLessonsCard.setStudent(UnicapApplication.getCurrentStudent());
        weekdayLessonsCard.setScheduleWeekDay(SubjectStatus.ScheduleWeekday.Wed);

        // Card: Thu

        weekdayLessonsCard = (WeekdayLessonsCard) getContentView().findViewById(R.id.card_lessons_thu);
        weekdayLessonsCard.setStudent(UnicapApplication.getCurrentStudent());
        weekdayLessonsCard.setScheduleWeekDay(SubjectStatus.ScheduleWeekday.Thu);

        // Card: Fri

        weekdayLessonsCard = (WeekdayLessonsCard) getContentView().findViewById(R.id.card_lessons_fri);
        weekdayLessonsCard.setStudent(UnicapApplication.getCurrentStudent());
        weekdayLessonsCard.setScheduleWeekDay(SubjectStatus.ScheduleWeekday.Fri);

        // Card: Sat

        weekdayLessonsCard = (WeekdayLessonsCard) getContentView().findViewById(R.id.card_lessons_sat);
        weekdayLessonsCard.setStudent(UnicapApplication.getCurrentStudent());
        weekdayLessonsCard.setScheduleWeekDay(SubjectStatus.ScheduleWeekday.Sat);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(NavigationDrawerFragment.SESSION_LESSONS);
    }

    @Override
    public void databaseSyncing() {

    }

    @Override
    public void databaseUpdated() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setContentView(R.layout.fragment_lessons);
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