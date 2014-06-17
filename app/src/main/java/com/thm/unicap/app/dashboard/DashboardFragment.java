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

import it.gmariotti.cardslib.library.view.CardView;

public class DashboardFragment extends Fragment {

    private SituationGraphCard mSituationGraphCard;

    public DashboardFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);

        CardView card_status_graph = (CardView) rootView.findViewById(R.id.card_status_graph);
        mSituationGraphCard = new SituationGraphCard(getActivity());
        card_status_graph.setCard(mSituationGraphCard);

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(NavigationDrawerFragment.SESSION_DASHBOARD);
    }
}