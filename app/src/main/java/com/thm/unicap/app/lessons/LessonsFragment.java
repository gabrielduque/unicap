package com.thm.unicap.app.lessons;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thm.unicap.app.MainActivity;
import com.thm.unicap.app.R;
import com.thm.unicap.app.UnicapApplication;
import com.thm.unicap.app.menu.NavigationDrawerFragment;
import com.thm.unicap.app.model.SubjectStatus;
import com.thm.unicap.app.util.StudentListener;

import it.gmariotti.cardslib.library.view.CardView;

public class LessonsFragment extends Fragment implements StudentListener {

    private View mRootView;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_lessons, container, false);
        if(UnicapApplication.isLogged()) init();
        return mRootView;
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

        card_lessons = (CardView) mRootView.findViewById(R.id.card_lessons_mon);
        lessonsCard = new LessonsCard(getActivity(), SubjectStatus.ScheduleWeekDay.Mon);
        lessonsCard.init();
        card_lessons.setCard(lessonsCard);
        card_lessons.setVisibility(View.VISIBLE);

        card_lessons = (CardView) mRootView.findViewById(R.id.card_lessons_tue);
        lessonsCard = new LessonsCard(getActivity(), SubjectStatus.ScheduleWeekDay.Tue);
        lessonsCard.init();
        card_lessons.setCard(lessonsCard);
        card_lessons.setVisibility(View.VISIBLE);

        card_lessons = (CardView) mRootView.findViewById(R.id.card_lessons_wed);
        lessonsCard = new LessonsCard(getActivity(), SubjectStatus.ScheduleWeekDay.Wed);
        lessonsCard.init();
        card_lessons.setCard(lessonsCard);
        card_lessons.setVisibility(View.VISIBLE);

        card_lessons = (CardView) mRootView.findViewById(R.id.card_lessons_thu);
        lessonsCard = new LessonsCard(getActivity(), SubjectStatus.ScheduleWeekDay.Thu);
        lessonsCard.init();
        card_lessons.setCard(lessonsCard);
        card_lessons.setVisibility(View.VISIBLE);

        card_lessons = (CardView) mRootView.findViewById(R.id.card_lessons_fri);
        lessonsCard = new LessonsCard(getActivity(), SubjectStatus.ScheduleWeekDay.Fri);
        lessonsCard.init();
        card_lessons.setCard(lessonsCard);
        card_lessons.setVisibility(View.VISIBLE);

        card_lessons = (CardView) mRootView.findViewById(R.id.card_lessons_sat);
        lessonsCard = new LessonsCard(getActivity(), SubjectStatus.ScheduleWeekDay.Sat);
        lessonsCard.init();
        card_lessons.setCard(lessonsCard);
        card_lessons.setVisibility(View.VISIBLE);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(NavigationDrawerFragment.SESSION_LESSONS);
    }

    @Override
    public void studentChanged() {
        init();
    }
}