package com.thm.unicap.app.subject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.thm.unicap.app.R;
import com.thm.unicap.app.model.Subject;
import com.thm.unicap.app.model.SubjectStatus;

import it.gmariotti.cardslib.library.internal.Card;

public class SubjectHistoryCard extends Card {

    private Subject mSubject;

    public SubjectHistoryCard(Context context, Subject subject) {
        this(context, R.layout.card_subject_history, subject);
    }

    public SubjectHistoryCard(Context context, int innerLayout, Subject subject) {
        super(context, innerLayout);
        mSubject = subject;
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {

        for (SubjectStatus subjectStatus : mSubject.getSubjectStatus()) {
            if(subjectStatus.situation != SubjectStatus.Situation.PENDING) {
                ViewGroup historyContainer = (ViewGroup) parent.findViewById(R.id.card_subject_info_container);
                LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View historyEntry = layoutInflater.inflate(R.layout.card_subject_history_entry, historyContainer, false);
                historyContainer.addView(historyEntry);

                TextView entry_period = (TextView) historyEntry.findViewById(R.id.card_subject_history_entry_period);
                TextView entry_status = (TextView) historyEntry.findViewById(R.id.card_subject_history_entry_status);

                entry_period.setText(subjectStatus.paidIn);

                if(subjectStatus.situation == SubjectStatus.Situation.APPROVED) {
                    entry_status.setText(mContext.getString(R.string.approved));
                    entry_status.setTextColor(mContext.getResources().getColor(android.R.color.holo_green_light));
                    entry_status.setCompoundDrawablesWithIntrinsicBounds(null, null, mContext.getResources().getDrawable(R.drawable.ic_action_accept), null);
                } else if(subjectStatus.situation == SubjectStatus.Situation.PERFORMED) {
                    entry_status.setText(mContext.getString(R.string.performed));
                    entry_status.setTextColor(mContext.getResources().getColor(android.R.color.holo_green_light));
                    entry_status.setCompoundDrawablesWithIntrinsicBounds(null, null, mContext.getResources().getDrawable(R.drawable.ic_action_accept), null);
                } else if(subjectStatus.situation == SubjectStatus.Situation.DISPENSED) {
                    entry_status.setText(mContext.getString(R.string.dispensed));
                    entry_status.setTextColor(mContext.getResources().getColor(android.R.color.holo_green_light));
                    entry_status.setCompoundDrawablesWithIntrinsicBounds(null, null, mContext.getResources().getDrawable(R.drawable.ic_action_accept), null);
                } else if(subjectStatus.situation == SubjectStatus.Situation.REPROVED) {
                    entry_status.setText(mContext.getString(R.string.repproved));
                    entry_status.setTextColor(mContext.getResources().getColor(android.R.color.holo_red_light));
                    entry_status.setCompoundDrawablesWithIntrinsicBounds(null, null, mContext.getResources().getDrawable(R.drawable.ic_action_cancel), null);
                } else if(subjectStatus.situation == SubjectStatus.Situation.ACTUAL) {
                    entry_status.setText(mContext.getString(R.string.actual));
                    entry_status.setTextColor(mContext.getResources().getColor(android.R.color.holo_green_light));
                    entry_status.setCompoundDrawablesWithIntrinsicBounds(null, null, mContext.getResources().getDrawable(R.drawable.ic_action_forward), null);
                } else if(subjectStatus.situation == SubjectStatus.Situation.IMPORTED) {
                    entry_status.setText(mContext.getString(R.string.imported));
                    entry_status.setTextColor(mContext.getResources().getColor(android.R.color.holo_green_light));
                    entry_status.setCompoundDrawablesWithIntrinsicBounds(null, null, mContext.getResources().getDrawable(R.drawable.ic_action_download), null);
                } else {
                    entry_status.setText(mContext.getString(R.string.unknown));
                    entry_status.setTextColor(mContext.getResources().getColor(android.R.color.black));
                    entry_status.setCompoundDrawablesWithIntrinsicBounds(null, null, mContext.getResources().getDrawable(R.drawable.ic_action_help), null);
                }
            }
        }

    }

}
