package com.thm.unicap.app.dashboard;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thm.unicap.app.R;
import com.thm.unicap.app.MainActivity;
import com.thm.unicap.app.lessons.LessonsCard;
import com.thm.unicap.app.menu.NavigationDrawerFragment;
import com.thm.unicap.app.util.UnicapUtils;

import it.gmariotti.cardslib.library.view.CardView;

public class DashboardFragment extends Fragment {

    private LessonsCard mLessonsCard;

    public DashboardFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);

        CardView card_today_lessons = (CardView) rootView.findViewById(R.id.card_today_lessons);
        mLessonsCard = new LessonsCard(getActivity(), UnicapUtils.getCurrentScheduleWeekDay());
        mLessonsCard.init();
        card_today_lessons.setCard(mLessonsCard);

        CardView card_status_graph = (CardView) rootView.findViewById(R.id.card_status_graph);
        SituationGraphCard situationGraphCard = new SituationGraphCard(getActivity());
        card_status_graph.setCard(situationGraphCard);

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(NavigationDrawerFragment.SESSION_DASHBOARD);
    }
}