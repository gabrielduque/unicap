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
import com.thm.unicap.app.util.DatabaseListener;

import it.gmariotti.cardslib.library.view.CardView;

public class LessonsFragment extends ProgressFragment implements DatabaseListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

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
        setContentView(R.layout.fragment_lessons);

        if(UnicapApplication.hasStudentData())
            init();
        else
            setContentShown(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        UnicapApplication.addStudentListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        UnicapApplication.removeStudentListener(this);
    }

    private void init() {
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

        setContentShown(true);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(NavigationDrawerFragment.SESSION_LESSONS);
    }

    @Override
    public void databaseChanged() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                init();
            }
        });
    }
}