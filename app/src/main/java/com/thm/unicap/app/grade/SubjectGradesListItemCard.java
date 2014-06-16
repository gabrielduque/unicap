package com.thm.unicap.app.grade;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.thm.unicap.app.R;
import com.thm.unicap.app.model.Subject;
import com.thm.unicap.app.model.SubjectStatus;

import it.gmariotti.cardslib.library.internal.Card;

public class SubjectGradesListItemCard extends Card {

    private Subject mSubject;

    public SubjectGradesListItemCard(Context context, Subject subject) {
        this(context, R.layout.card_subject_grades_list_item, subject);
    }

    public SubjectGradesListItemCard(Context context, int innerLayout, Subject subject) {
        super(context, innerLayout);
        mSubject = subject;
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {
        TextView subject_name_abbreviation = (TextView) parent.findViewById(R.id.subject_name_abbreviation);
        TextView subject_code = (TextView) parent.findViewById(R.id.subject_code);
        TextView subject_name = (TextView) parent.findViewById(R.id.subject_name);
        TextView subject_situation = (TextView) parent.findViewById(R.id.subject_situation);

        subject_name_abbreviation.setBackgroundResource(mSubject.getColorResource());
        subject_name_abbreviation.setText(mSubject.getNameAbbreviation());
        subject_code.setText(mSubject.code);
        subject_name.setText(mSubject.name);

        SubjectStatus.FlowSituation flowSituation = mSubject.getActualSubjectStatus().getFlowSituation();

        switch (flowSituation) {
            case APPROVED:
                subject_situation.setText(mContext.getString(R.string.approved));
                subject_situation.setTextColor(mContext.getResources().getColor(android.R.color.holo_green_light));
                subject_situation.setCompoundDrawablesWithIntrinsicBounds(null, null, mContext.getResources().getDrawable(R.drawable.ic_action_accept), null);
                break;
            case REPROVED:
                subject_situation.setText(mContext.getString(R.string.repproved));
                subject_situation.setTextColor(mContext.getResources().getColor(android.R.color.holo_red_light));
                subject_situation.setCompoundDrawablesWithIntrinsicBounds(null, null, mContext.getResources().getDrawable(R.drawable.ic_action_cancel), null);
                break;
            case WAITING:
                subject_situation.setText(mContext.getString(R.string.waiting));
                subject_situation.setTextColor(mContext.getResources().getColor(android.R.color.darker_gray));
                subject_situation.setCompoundDrawablesWithIntrinsicBounds(null, null, mContext.getResources().getDrawable(R.drawable.ic_action_time), null);
                break;
            case WAITING_FINAL:
                subject_situation.setText(mContext.getString(R.string.waiting_final));
                subject_situation.setTextColor(mContext.getResources().getColor(android.R.color.darker_gray));
                subject_situation.setCompoundDrawablesWithIntrinsicBounds(null, null, mContext.getResources().getDrawable(R.drawable.ic_action_time), null);
                break;
        }

    }

    public Subject getSubject() {
        return mSubject;
    }
}
