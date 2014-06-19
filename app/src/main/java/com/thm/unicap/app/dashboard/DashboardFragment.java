package com.thm.unicap.app.dashboard;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thm.unicap.app.R;
import com.thm.unicap.app.MainActivity;
import com.thm.unicap.app.menu.NavigationDrawerFragment;

import java.util.Timer;
import java.util.TimerTask;

import it.gmariotti.cardslib.library.view.CardView;

public class DashboardFragment extends Fragment {

    private TodayLessonsCard mTodayLessonsCard;

    public DashboardFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);

        CardView card_today_lessons = (CardView) rootView.findViewById(R.id.card_today_lessons);
        mTodayLessonsCard = new TodayLessonsCard(getActivity());
        card_today_lessons.setCard(mTodayLessonsCard);

        CardView card_status_graph = (CardView) rootView.findViewById(R.id.card_status_graph);
        SituationGraphCard situationGraphCard = new SituationGraphCard(getActivity());
        card_status_graph.setCard(situationGraphCard);

        return rootView;
    }

//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        Timer t = new Timer();
//        t.scheduleAtFixedRate(new TimerTask() {
//            @Override
//            public void run() {
//                mTodayLessonsCard.initTodayLessons();
//            }
//        }, 0, 10000);
//    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(NavigationDrawerFragment.SESSION_DASHBOARD);
    }
}