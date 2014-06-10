package com.thm.unicap.app.subject;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.thm.unicap.app.R;
import com.thm.unicap.app.model.Subject;

import it.gmariotti.cardslib.library.internal.Card;

public class SubjectScheduleCard extends Card {

    private Subject mSubject;

    public SubjectScheduleCard(Context context, Subject subject) {
        this(context, R.layout.card_subject_schedule, subject);
    }

    public SubjectScheduleCard(Context context, int innerLayout, Subject subject) {
        super(context, innerLayout);
        mSubject = subject;
    }

//    @Override
//    public void setupInnerViewElements(ViewGroup parent, View view) {
//        TextView card_subject_info_code = (TextView) parent.findViewById(R.id.card_subject_info_code);
//        TextView card_subject_info_workload = (TextView) parent.findViewById(R.id.card_subject_info_workload);
//        TextView card_subject_info_credits = (TextView) parent.findViewById(R.id.card_subject_info_credits);
//        TextView card_subject_info_period = (TextView) parent.findViewById(R.id.card_subject_info_period);
//
//        card_subject_info_code.setText(mSubject.code);
//
//        if (mSubject.workload == null)
//            card_subject_info_workload.setText(mContext.getString(R.string.unavailable));
//        else
//            card_subject_info_workload.setText(String.format(mContext.getString(R.string.workload_format), mSubject.workload));
//
//        if (mSubject.credits == null)
//            card_subject_info_credits.setText(mContext.getString(R.string.unavailable));
//        else
//            card_subject_info_credits.setText(String.format(mContext.getString(R.string.credits_format), mSubject.credits));
//
//        if (mSubject.period == null)
//            card_subject_info_period.setText(mContext.getString(R.string.unavailable));
//        else
//            card_subject_info_period.setText(String.format(mContext.getString(R.string.period_format), mSubject.period));
//    }

}
