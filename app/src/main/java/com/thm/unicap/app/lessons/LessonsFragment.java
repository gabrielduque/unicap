package com.thm.unicap.app.lessons;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import com.devspark.progressfragment.ProgressFragment;
import com.thm.unicap.app.MainActivity;
import com.thm.unicap.app.R;
import com.thm.unicap.app.UnicapApplication;
import com.thm.unicap.app.menu.NavigationDrawerFragment;
import com.thm.unicap.app.model.SubjectStatus;
import com.thm.unicap.app.util.DatabaseDependentFragment;
import com.thm.unicap.app.util.DatabaseListener;
import com.thm.unicap.app.util.NetworkUtils;

import it.gmariotti.cardslib.library.view.CardView;

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

        UnicapApplication.addDatabaseListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        UnicapApplication.removeDatabaseListener(this);
    }

    @Override
    public void initDatabaseDependentViews() {
        CardView card_lessons;
        LessonsCard lessonsCard;

        // Card: Mon

        card_lessons = (CardView) getContentView().findViewById(R.id.card_lessons_mon);
        lessonsCard = new LessonsCard(getActivity(), SubjectStatus.ScheduleWeekDay.Mon);
        lessonsCard.init();

        if(card_lessons.getCard() != null)
            card_lessons.replaceCard(lessonsCard);
        else
            card_lessons.setCard(lessonsCard);

        card_lessons.setVisibility(View.VISIBLE);

        // Card: Tue

        card_lessons = (CardView) getContentView().findViewById(R.id.card_lessons_tue);
        lessonsCard = new LessonsCard(getActivity(), SubjectStatus.ScheduleWeekDay.Tue);
        lessonsCard.init();

        if(card_lessons.getCard() != null)
            card_lessons.replaceCard(lessonsCard);
        else
            card_lessons.setCard(lessonsCard);

        card_lessons.setVisibility(View.VISIBLE);

        // Card: Wed

        card_lessons = (CardView) getContentView().findViewById(R.id.card_lessons_wed);
        lessonsCard = new LessonsCard(getActivity(), SubjectStatus.ScheduleWeekDay.Wed);
        lessonsCard.init();

        if(card_lessons.getCard() != null)
            card_lessons.replaceCard(lessonsCard);
        else
            card_lessons.setCard(lessonsCard);

        card_lessons.setVisibility(View.VISIBLE);

        // Card: Thu

        card_lessons = (CardView) getContentView().findViewById(R.id.card_lessons_thu);
        lessonsCard = new LessonsCard(getActivity(), SubjectStatus.ScheduleWeekDay.Thu);
        lessonsCard.init();

        if(card_lessons.getCard() != null)
            card_lessons.replaceCard(lessonsCard);
        else
            card_lessons.setCard(lessonsCard);

        card_lessons.setVisibility(View.VISIBLE);

        // Card: Fri

        card_lessons = (CardView) getContentView().findViewById(R.id.card_lessons_fri);
        lessonsCard = new LessonsCard(getActivity(), SubjectStatus.ScheduleWeekDay.Fri);
        lessonsCard.init();

        if(card_lessons.getCard() != null)
            card_lessons.replaceCard(lessonsCard);
        else
            card_lessons.setCard(lessonsCard);

        card_lessons.setVisibility(View.VISIBLE);

        // Card: Sat

        card_lessons = (CardView) getContentView().findViewById(R.id.card_lessons_sat);
        lessonsCard = new LessonsCard(getActivity(), SubjectStatus.ScheduleWeekDay.Sat);
        lessonsCard.init();

        if(card_lessons.getCard() != null)
            card_lessons.replaceCard(lessonsCard);
        else
            card_lessons.setCard(lessonsCard);

        card_lessons.setVisibility(View.VISIBLE);

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