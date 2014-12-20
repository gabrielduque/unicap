package com.thm.unicap.app.calendar;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.thm.unicap.app.R;
import com.thm.unicap.app.model.SubjectTest;

import java.text.SimpleDateFormat;


public class CalendarListItemCard extends CardView {

    private SubjectTest mSubjectTest;

    public CalendarListItemCard(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        View.inflate(getContext(), R.layout.card_test_list_item, this);
    }

    private void initData() {
        TextView subject_name_abbreviation = (TextView) findViewById(R.id.subject_name_abbreviation);
        TextView subject_name = (TextView) findViewById(R.id.subject_name);
        TextView subject_degree = (TextView) findViewById(R.id.subject_degree);
        TextView subject_date1 = (TextView) findViewById(R.id.subject_date1);
        TextView subject_date2 = (TextView) findViewById(R.id.subject_date2);

        subject_name_abbreviation.setBackgroundResource(mSubjectTest.subject.getColorResource());
        subject_name_abbreviation.setText(mSubjectTest.subject.getNameAbbreviation());
        subject_name.setText(mSubjectTest.subject.name);

        SimpleDateFormat sdf = new SimpleDateFormat(getContext().getString(R.string.date_format));

        if (mSubjectTest.date1 != null) {
            subject_date1.setText(sdf.format(mSubjectTest.date1));
        } else {
            subject_date1.setText("-");
        }

        if (mSubjectTest.date2 != null) {
            subject_date2.setText(sdf.format(mSubjectTest.date2));
        } else {
            subject_date2.setText("-");
        }

        switch (mSubjectTest.degree) {
            case FIRST_DEGREE:
                subject_degree.setText(R.string.first_degree);
                break;
            case SECOND_DEGREE:
                subject_degree.setText(R.string.second_degree);
                break;
            case FINAL_DEGREE:
                subject_degree.setText(R.string.final_degree);
                break;
        }
    }

    public void setSubjectTest(SubjectTest subjectTest) {
        this.mSubjectTest = subjectTest;
        initData();
    }
}
