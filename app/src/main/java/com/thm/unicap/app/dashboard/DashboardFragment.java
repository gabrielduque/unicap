package com.thm.unicap.app.dashboard;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import com.devspark.progressfragment.ProgressFragment;
import com.thm.unicap.app.R;
import com.thm.unicap.app.MainActivity;
import com.thm.unicap.app.UnicapApplication;
import com.thm.unicap.app.lessons.LessonsCard;
import com.thm.unicap.app.menu.NavigationDrawerFragment;
import com.thm.unicap.app.util.DatabaseDependentFragment;
import com.thm.unicap.app.util.DatabaseListener;
import com.thm.unicap.app.util.NetworkUtils;
import com.thm.unicap.app.util.UnicapUtils;

import it.gmariotti.cardslib.library.view.CardView;

public class DashboardFragment extends ProgressFragment implements DatabaseListener, DatabaseDependentFragment {

    @Override
    public void initDatabaseDependentViews() {
        CardView card_today_lessons = (CardView) getContentView().findViewById(R.id.card_today_lessons);
        CardView card_status_graph = (CardView) getContentView().findViewById(R.id.card_status_graph);

        CoefficientCard coefficientCard = (CoefficientCard) getContentView().findViewById(R.id.card_course_coefficient);

        //Vê se era isso que você estava falando, não tenho certeza
        // se quando não existe coeficiente no banco o valor dele seja null ou zero
        if ( UnicapApplication.getCurrentStudent().lastCoefficient == null ||
                UnicapApplication.getCurrentStudent().courseCoefficient == null ) {
            coefficientCard.setVisibility(View.GONE);
        }
        else {
            coefficientCard.setCoefficient();
        }

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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        NavigationDrawerFragment navigationDrawerFragment = ((MainActivity) getActivity()).getNavigationDrawerFragment();

        if(navigationDrawerFragment != null && !navigationDrawerFragment.isDrawerOpen()) {
            inflater.inflate(R.menu.fragment_dashboard, menu);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(NavigationDrawerFragment.SESSION_DASHBOARD);
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