package com.thm.unicap.app.subject;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.thm.unicap.app.R;
import com.thm.unicap.app.model.Subject;

public class SubjectListItemCard extends CardView {

    private Subject mSubject;

    public SubjectListItemCard(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        View.inflate(getContext(), R.layout.card_subject_list_item, this);
    }

    private void initData() {
        TextView subject_name_abbreviation = (TextView) findViewById(R.id.subject_name_abbreviation);
        TextView subject_code = (TextView) findViewById(R.id.subject_code);
        TextView subject_name = (TextView) findViewById(R.id.subject_name);
        TextView subject_period = (TextView) findViewById(R.id.subject_period);

        subject_name_abbreviation.setBackgroundResource(mSubject.getColorResource());
        subject_name_abbreviation.setText(mSubject.getNameAbbreviation());
        subject_code.setText(mSubject.code);
        subject_name.setText(mSubject.name);

        if (mSubject.period == null)
            subject_period.setText("");
        else
            subject_period.setText(String.format(getContext().getString(R.string.period_format), mSubject.period));
    }

    public void setSubject(Subject subject) {
        this.mSubject = subject;
        initData();
    }
}
