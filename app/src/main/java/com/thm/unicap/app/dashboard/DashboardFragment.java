package com.thm.unicap.app.dashboard;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thm.unicap.app.R;
import com.thm.unicap.app.MainActivity;
import com.thm.unicap.app.UnicapApplication;
import com.thm.unicap.app.lessons.LessonsCard;
import com.thm.unicap.app.menu.NavigationDrawerFragment;
import com.thm.unicap.app.util.DatabaseListener;
import com.thm.unicap.app.util.UnicapUtils;

import it.gmariotti.cardslib.library.view.CardView;

public class DashboardFragment extends Fragment implements DatabaseListener {

    private View mRootView;

    private void init() {
        CardView card_today_lessons = (CardView) mRootView.findViewById(R.id.card_today_lessons);
        CardView card_status_graph = (CardView) mRootView.findViewById(R.id.card_status_graph);

        SituationGraphCard situationGraphCard = new SituationGraphCard(getActivity());
        LessonsCard lessonsCard = new LessonsCard(getActivity(), UnicapUtils.getCurrentScheduleWeekDay());
        lessonsCard.init();

        if(card_today_lessons.getCard() != null)
            card_today_lessons.replaceCard(lessonsCard);
        else
            card_today_lessons.setCard(lessonsCard);

        if (card_status_graph.getCard() != null)
            card_status_graph.replaceCard(situationGraphCard);
        else
            card_status_graph.setCard(situationGraphCard);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        NavigationDrawerFragment navigationDrawerFragment = ((MainActivity) getActivity()).getNavigationDrawerFragment();

        if(navigationDrawerFragment != null && !navigationDrawerFragment.isDrawerOpen()) {
            inflater.inflate(R.menu.fragment_dashboard, menu);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_dashboard, container, false);
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

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(NavigationDrawerFragment.SESSION_DASHBOARD);
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