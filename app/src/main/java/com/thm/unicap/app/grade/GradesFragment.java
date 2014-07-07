package com.thm.unicap.app.grade;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.activeandroid.query.Select;
import com.nhaarman.listviewanimations.swinginadapters.AnimationAdapter;
import com.nhaarman.listviewanimations.swinginadapters.prepared.SwingBottomInAnimationAdapter;
import com.thm.unicap.app.MainActivity;
import com.thm.unicap.app.R;
import com.thm.unicap.app.UnicapApplication;
import com.thm.unicap.app.menu.NavigationDrawerFragment;
import com.thm.unicap.app.model.Student;
import com.thm.unicap.app.model.Subject;
import com.thm.unicap.app.model.SubjectStatus;
import com.thm.unicap.app.subject.SubjectActivity;
import com.thm.unicap.app.subject.SubjectListItemCard;
import com.thm.unicap.app.subject.SubjectsPagerAdapter;
import com.thm.unicap.app.util.StudentListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.view.CardListView;

public class GradesFragment extends Fragment implements Card.OnCardClickListener, StudentListener {

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
            inflater.inflate(R.menu.fragment_grades, menu);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_grades, container, false);
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
        Student student = UnicapApplication.getStudent();

        List<Subject> subjects = student.getActualSubjects();

        ArrayList<Card> cardArrayList = new ArrayList<Card>();

        for (Subject subject : subjects) {
            SubjectGradesListItemCard subjectGradesListItemCard = new SubjectGradesListItemCard(getActivity(), subject);
            subjectGradesListItemCard.setClickable(true);
            subjectGradesListItemCard.setOnClickListener(this);
            cardArrayList.add(subjectGradesListItemCard);
        }

        CardListView cardListView = (CardListView) mRootView.findViewById(R.id.subjects_list);

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
    public void studentChanged() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                init();
            }
        });
    }
}
