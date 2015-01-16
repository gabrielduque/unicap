package com.thm.unicap.app.subject;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.thm.unicap.app.R;
import com.thm.unicap.app.model.Subject;
import com.thm.unicap.app.model.SubjectStatus;

public class SubjectHistoryCard extends CardView {

    private Subject mSubject;

    public SubjectHistoryCard(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        View.inflate(getContext(), R.layout.card_subject_history, this);
    }

    private void initData() {
        for (SubjectStatus subjectStatus : mSubject.getSubjectStatus()) {
            if (subjectStatus.situation != SubjectStatus.Situation.PENDING) {
                ViewGroup historyContainer = (ViewGroup) findViewById(R.id.card_subject_info_container);
                LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View historyEntry = layoutInflater.inflate(R.layout.subject_history_entry, historyContainer, false);
                historyContainer.addView(historyEntry);

                TextView entry_period = (TextView) historyEntry.findViewById(R.id.card_subject_history_entry_period);
                TextView entry_status = (TextView) historyEntry.findViewById(R.id.card_subject_history_entry_status);

                entry_period.setText(subjectStatus.paidIn);

                if (subjectStatus.situation == SubjectStatus.Situation.APPROVED) {
                    entry_status.setText(getContext().getString(R.string.approved));
                    entry_status.setTextColor(getContext().getResources().getColor(android.R.color.holo_green_light));
                    entry_status.setCompoundDrawablesWithIntrinsicBounds(null, null, getContext().getResources().getDrawable(R.drawable.ic_action_accept), null);
                } else if (subjectStatus.situation == SubjectStatus.Situation.PERFORMED) {
                    entry_status.setText(getContext().getString(R.string.performed));
                    entry_status.setTextColor(getContext().getResources().getColor(android.R.color.holo_green_light));
                    entry_status.setCompoundDrawablesWithIntrinsicBounds(null, null, getContext().getResources().getDrawable(R.drawable.ic_action_accept), null);
                } else if (subjectStatus.situation == SubjectStatus.Situation.DISPENSED) {
                    entry_status.setText(getContext().getString(R.string.dispensed));
                    entry_status.setTextColor(getContext().getResources().getColor(android.R.color.holo_green_light));
                    entry_status.setCompoundDrawablesWithIntrinsicBounds(null, null, getContext().getResources().getDrawable(R.drawable.ic_action_accept), null);
                } else if (subjectStatus.situation == SubjectStatus.Situation.REPROVED) {
                    entry_status.setText(getContext().getString(R.string.repproved));
                    entry_status.setTextColor(getContext().getResources().getColor(android.R.color.holo_red_light));
                    entry_status.setCompoundDrawablesWithIntrinsicBounds(null, null, getContext().getResources().getDrawable(R.drawable.ic_action_cancel), null);
                } else if (subjectStatus.situation == SubjectStatus.Situation.ACTUAL) {
                    entry_status.setText(getContext().getString(R.string.actual));
                    entry_status.setTextColor(getContext().getResources().getColor(android.R.color.holo_green_light));
                    entry_status.setCompoundDrawablesWithIntrinsicBounds(null, null, getContext().getResources().getDrawable(R.drawable.ic_action_forward), null);
                } else if (subjectStatus.situation == SubjectStatus.Situation.IMPORTED) {
                    entry_status.setText(getContext().getString(R.string.imported));
                    entry_status.setTextColor(getContext().getResources().getColor(android.R.color.holo_green_light));
                    entry_status.setCompoundDrawablesWithIntrinsicBounds(null, null, getContext().getResources().getDrawable(R.drawable.ic_action_download), null);
                } else {
                    entry_status.setText(getContext().getString(R.string.unknown));
                    entry_status.setTextColor(getContext().getResources().getColor(android.R.color.black));
                    entry_status.setCompoundDrawablesWithIntrinsicBounds(null, null, getContext().getResources().getDrawable(R.drawable.ic_action_help), null);
                }
            }
        }
    }

    public void setSubject(Subject subject) {
        this.mSubject = subject;
        initData();
    }
}
