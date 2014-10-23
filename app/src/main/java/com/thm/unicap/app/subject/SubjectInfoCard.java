package com.thm.unicap.app.subject;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.thm.unicap.app.R;
import com.thm.unicap.app.model.Subject;

public class SubjectInfoCard extends CardView {

    private Subject mSubject;

    public SubjectInfoCard(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        View.inflate(getContext(), R.layout.card_subject_info, this);
    }

    private void initData() {

        if (mSubject == null) return;

        TextView card_subject_info_code = (TextView) findViewById(R.id.card_subject_info_code);
        TextView card_subject_info_workload = (TextView) findViewById(R.id.card_subject_info_workload);
        TextView card_subject_info_credits = (TextView) findViewById(R.id.card_subject_info_credits);
        TextView card_subject_info_period = (TextView) findViewById(R.id.card_subject_info_period);

        card_subject_info_code.setText(mSubject.code);

        if (mSubject.workload == null)
            card_subject_info_workload.setText(getContext().getString(R.string.unavailable));
        else
            card_subject_info_workload.setText(String.format(getContext().getString(R.string.workload_format), mSubject.workload));

        if (mSubject.credits == null)
            card_subject_info_credits.setText(getContext().getString(R.string.unavailable));
        else
            card_subject_info_credits.setText(String.format(getContext().getString(R.string.credits_format), mSubject.credits));

        if (mSubject.period == null)
            card_subject_info_period.setText(getContext().getString(R.string.unavailable));
        else
            card_subject_info_period.setText(String.format(getContext().getString(R.string.period_format), mSubject.period));
    }

    public Subject getSubject() {
        return mSubject;
    }

    public void setSubject(Subject subject) {
        this.mSubject = subject;
        initData();
    }
}
