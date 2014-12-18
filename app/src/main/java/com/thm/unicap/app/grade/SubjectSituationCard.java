package com.thm.unicap.app.grade;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.thm.unicap.app.R;
import com.thm.unicap.app.model.Subject;
import com.thm.unicap.app.model.SubjectStatus;
import com.thm.unicap.app.model.SubjectTest;

import it.gmariotti.cardslib.library.internal.Card;

public class SubjectSituationCard extends CardView {

    private Subject mSubject;

    public SubjectSituationCard(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        View.inflate(getContext(), R.layout.card_subject_situation, this);
    }

    private void initData() {
        TextView card_subject_grades_situation = (TextView) findViewById(R.id.card_subject_grades_situation);

        ViewGroup card_subject_grades_situation_container = (ViewGroup) findViewById(R.id.card_subject_grades_situation_container);

        SubjectStatus.FlowSituation flowSituation = mSubject.getActualSubjectStatus().getFlowSituation();

        switch (flowSituation) {
            case APPROVED:
                card_subject_grades_situation.setText(getContext().getString(R.string.approved));
                card_subject_grades_situation.setCompoundDrawablesWithIntrinsicBounds(getContext().getResources().getDrawable(R.drawable.ic_action_accept_light), null, null, null);
                card_subject_grades_situation_container.setBackgroundColor(getContext().getResources().getColor(android.R.color.holo_green_light));
                break;
            case REPROVED:
                card_subject_grades_situation.setText(getContext().getString(R.string.repproved));
                card_subject_grades_situation.setCompoundDrawablesWithIntrinsicBounds(getContext().getResources().getDrawable(R.drawable.ic_action_cancel_light), null, null, null);
                card_subject_grades_situation_container.setBackgroundColor(getContext().getResources().getColor(android.R.color.holo_red_light));
                break;
            case WAITING:
                card_subject_grades_situation.setText(getContext().getString(R.string.waiting));
                card_subject_grades_situation.setCompoundDrawablesWithIntrinsicBounds(getContext().getResources().getDrawable(R.drawable.ic_action_time_light), null, null, null);
                card_subject_grades_situation_container.setBackgroundColor(getContext().getResources().getColor(android.R.color.darker_gray));
                break;
            case WAITING_FINAL:
                card_subject_grades_situation.setText(getContext().getString(R.string.waiting_final));
                card_subject_grades_situation.setCompoundDrawablesWithIntrinsicBounds(getContext().getResources().getDrawable(R.drawable.ic_action_time_light), null, null, null);
                card_subject_grades_situation_container.setBackgroundColor(getContext().getResources().getColor(android.R.color.darker_gray));
                break;
        }

    }

    public void setSubject(Subject subject) {
        this.mSubject = subject;
        initData();
    }
}
