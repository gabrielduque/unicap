package com.thm.unicap.app.calendar;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuInflater;

import com.devspark.progressfragment.ProgressFragment;
import com.nhaarman.listviewanimations.swinginadapters.AnimationAdapter;
import com.nhaarman.listviewanimations.swinginadapters.prepared.SwingBottomInAnimationAdapter;
import com.thm.unicap.app.MainActivity;
import com.thm.unicap.app.R;
import com.thm.unicap.app.UnicapApplication;
import com.thm.unicap.app.menu.NavigationDrawerFragment;
import com.thm.unicap.app.model.Student;
import com.thm.unicap.app.model.SubjectTest;
import com.thm.unicap.app.util.DatabaseDependentFragment;
import com.thm.unicap.app.util.DatabaseListener;
import com.thm.unicap.app.util.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.prototypes.CardSection;
import it.gmariotti.cardslib.library.prototypes.SectionedCardAdapter;
import it.gmariotti.cardslib.library.view.CardListView;

public class CalendarFragment extends ProgressFragment implements DatabaseListener, DatabaseDependentFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        NavigationDrawerFragment navigationDrawerFragment = ((MainActivity) getActivity()).getNavigationDrawerFragment();

        if(navigationDrawerFragment != null && !navigationDrawerFragment.isDrawerOpen()) {
            inflater.inflate(R.menu.fragment_calendar, menu);
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

        UnicapApplication.addDatabaseListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        UnicapApplication.removeDatabaseListener(this);
    }

    @Override
    public void initDatabaseDependentViews() {
        Student student = UnicapApplication.getCurrentStudent();

        List<SubjectTest> subjectTests = student.getSubjectTestsOrdered();

        ArrayList<Card> cards = new ArrayList<Card>();
        ArrayList<CardSection> cardSections = new ArrayList<CardSection>();

        boolean found_first_position = false;
        boolean found_second_position = false;
        boolean found_final_position = false;

        for (int i = 0; i < subjectTests.size(); i++) {

            if(!found_first_position && subjectTests.get(i).degree == SubjectTest.Degree.FIRST_DEGREE) {
                cardSections.add(new CardSection(i, getString(R.string.first_degree)));
                found_first_position = true;
            }

            if(!found_second_position && subjectTests.get(i).degree == SubjectTest.Degree.SECOND_DEGREE) {
                cardSections.add(new CardSection(i, getString(R.string.second_degree)));
                found_second_position = true;
            }

            if(!found_final_position && subjectTests.get(i).degree == SubjectTest.Degree.FINAL_DEGREE) {
                cardSections.add(new CardSection(i, getString(R.string.final_degree)));
                found_final_position = true;
            }

            CalendarListItemCard calendarListItemCard = new CalendarListItemCard(getActivity(), subjectTests.get(i));
            cards.add(calendarListItemCard);
        }

        CardListView cardListView = (CardListView) getContentView().findViewById(R.id.calendar_list);

        CardArrayAdapter cardArrayAdapter = new CardArrayAdapter(getActivity(), cards);

        SectionedCardAdapter sectionedCardAdapter = new SectionedCardAdapter(getActivity(), R.layout.card_section, cardArrayAdapter);

        CardSection[] sections = new CardSection[cardSections.size()];
        sectionedCardAdapter.setCardSections(cardSections.toArray(sections));

        AnimationAdapter animCardArrayAdapter = new SwingBottomInAnimationAdapter(sectionedCardAdapter);
        animCardArrayAdapter.setAbsListView(cardListView);
        cardListView.setExternalAdapter(animCardArrayAdapter, cardArrayAdapter);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(NavigationDrawerFragment.SESSION_CALENDAR);
    }

    @Override
    public void databaseSyncing() {

    }

    @Override
    public void databaseUpdated() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setContentView(R.layout.fragment_calendar);
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
