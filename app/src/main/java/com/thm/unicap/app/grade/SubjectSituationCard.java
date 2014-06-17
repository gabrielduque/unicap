package com.thm.unicap.app.grade;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.thm.unicap.app.R;
import com.thm.unicap.app.model.Subject;
import com.thm.unicap.app.model.SubjectStatus;
import com.thm.unicap.app.model.SubjectTest;

import it.gmariotti.cardslib.library.internal.Card;

public class SubjectSituationCard extends Card {

    private Subject mSubject;

    public SubjectSituationCard(Context context, Subject subject) {
        super(context, R.layout.card_subject_situation);
        mSubject = subject;
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {
        TextView card_subject_grades_situation = (TextView) parent.findViewById(R.id.card_subject_grades_situation);

        ViewGroup card_subject_grades_situation_container = (ViewGroup) parent.findViewById(R.id.card_subject_grades_situation_container);

        SubjectStatus.FlowSituation flowSituation = mSubject.getActualSubjectStatus().getFlowSituation();

        switch (flowSituation) {
            case APPROVED:
                card_subject_grades_situation.setText(mContext.getString(R.string.approved));
                card_subject_grades_situation.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.ic_action_accept_light), null, null, null);
                card_subject_grades_situation_container.setBackgroundColor(mContext.getResources().getColor(android.R.color.holo_green_light));
                break;
            case REPROVED:
                card_subject_grades_situation.setText(mContext.getString(R.string.repproved));
                card_subject_grades_situation.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.ic_action_cancel_light), null, null, null);
                card_subject_grades_situation_container.setBackgroundColor(mContext.getResources().getColor(android.R.color.holo_red_light));
                break;
            case WAITING:
                card_subject_grades_situation.setText(mContext.getString(R.string.waiting));
                card_subject_grades_situation.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.ic_action_time_light), null, null, null);
                card_subject_grades_situation_container.setBackgroundColor(mContext.getResources().getColor(android.R.color.darker_gray));
                break;
            case WAITING_FINAL:
                card_subject_grades_situation.setText(mContext.getString(R.string.waiting_final));
                card_subject_grades_situation.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.ic_action_time_light), null, null, null);
                card_subject_grades_situation_container.setBackgroundColor(mContext.getResources().getColor(android.R.color.darker_gray));
                break;
        }

    }

}
