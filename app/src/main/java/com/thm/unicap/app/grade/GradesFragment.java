package com.thm.unicap.app.grade;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.activeandroid.query.Select;
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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.view.CardListView;

public class GradesFragment extends Fragment implements Card.OnCardClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_grades, container, false);

        Student student = UnicapApplication.getStudent();

        List<Subject> subjects = student.getActualSubjects();

        ArrayList<Card> cardArrayList = new ArrayList<Card>();

        for (Subject subject : subjects) {
            SubjectGradesListItemCard subjectGradesListItemCard = new SubjectGradesListItemCard(getActivity(), subject);
            subjectGradesListItemCard.setClickable(true);
            subjectGradesListItemCard.setOnClickListener(this);
            cardArrayList.add(subjectGradesListItemCard);
        }

        CardListView cardListView = (CardListView) rootView.findViewById(R.id.subjects_list);

        CardArrayAdapter cardArrayAdapter = new CardArrayAdapter(getActivity(), cardArrayList);

        cardListView.setAdapter(cardArrayAdapter);

        return rootView;
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
}
