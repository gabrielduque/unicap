package com.thm.unicap.app.grade;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import com.devspark.progressfragment.ProgressFragment;
import com.nhaarman.listviewanimations.swinginadapters.AnimationAdapter;
import com.nhaarman.listviewanimations.swinginadapters.prepared.SwingBottomInAnimationAdapter;
import com.thm.unicap.app.MainActivity;
import com.thm.unicap.app.R;
import com.thm.unicap.app.UnicapApplication;
import com.thm.unicap.app.menu.NavigationDrawerFragment;
import com.thm.unicap.app.model.Student;
import com.thm.unicap.app.model.Subject;
import com.thm.unicap.app.util.DatabaseDependentFragment;
import com.thm.unicap.app.util.DatabaseListener;
import com.thm.unicap.app.util.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.view.CardListView;

public class GradesFragment extends ProgressFragment implements Card.OnCardClickListener, DatabaseListener, DatabaseDependentFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        NavigationDrawerFragment navigationDrawerFragment = ((MainActivity) getActivity()).getNavigationDrawerFragment();

        if(navigationDrawerFragment != null && !navigationDrawerFragment.isDrawerOpen()) {
            inflater.inflate(R.menu.fragment_grades, menu);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

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
        UnicapApplication.addStudentListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        UnicapApplication.removeStudentListener(this);
    }

    @Override
    public void initDatabaseDependentViews() {
        Student student = UnicapApplication.getCurrentStudent();

        List<Subject> subjects = student.getActualSubjects();

        ArrayList<Card> cardArrayList = new ArrayList<Card>();

        for (Subject subject : subjects) {
            SubjectGradesListItemCard subjectGradesListItemCard = new SubjectGradesListItemCard(getActivity(), subject);
            subjectGradesListItemCard.setClickable(true);
            subjectGradesListItemCard.setOnClickListener(this);
            cardArrayList.add(subjectGradesListItemCard);
        }

        CardListView cardListView = (CardListView) getContentView().findViewById(R.id.subjects_list);

        CardArrayAdapter cardArrayAdapter = new CardArrayAdapter(getActivity(), cardArrayList);

        AnimationAdapter animCardArrayAdapter = new SwingBottomInAnimationAdapter(cardArrayAdapter);
        animCardArrayAdapter.setAbsListView(cardListView);
        cardListView.setExternalAdapter(animCardArrayAdapter, cardArrayAdapter);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(NavigationDrawerFragment.SESSION_GRADES);
    }

    @Override
    public void onClick(Card card, View view) {
        Intent intent = new Intent(getActivity(), GradesActivity.class);
        intent.putExtra("subject_id", ((SubjectGradesListItemCard)card).getSubject().getId());
        getActivity().startActivity(intent);
    }

    @Override
    public void databaseUpdated() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setContentView(R.layout.fragment_grades);
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
