package com.thm.unicap.app.calendar;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.thm.unicap.app.R;
import com.thm.unicap.app.model.Subject;
import com.thm.unicap.app.model.SubjectTest;

import java.text.SimpleDateFormat;
import java.util.Date;

import it.gmariotti.cardslib.library.internal.Card;

public class CalendarListItemCard extends Card {

    private SubjectTest mSubjectTest;

    public CalendarListItemCard(Context context, SubjectTest subjectTest) {
        super(context, R.layout.card_test_list_item);
        mSubjectTest = subjectTest;
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {
        TextView subject_name_abbreviation = (TextView) parent.findViewById(R.id.subject_name_abbreviation);
        TextView subject_name = (TextView) parent.findViewById(R.id.subject_name);
        TextView subject_degree = (TextView) parent.findViewById(R.id.subject_degree);
        TextView subject_date1 = (TextView) parent.findViewById(R.id.subject_date1);
        TextView subject_date2 = (TextView) parent.findViewById(R.id.subject_date2);

        subject_name_abbreviation.setBackgroundResource(mSubjectTest.subject.getColorResource());
        subject_name_abbreviation.setText(mSubjectTest.subject.getNameAbbreviation());
        subject_name.setText(mSubjectTest.subject.name);

        SimpleDateFormat sdf = new SimpleDateFormat(mContext.getString(R.string.date_format));

        if (mSubjectTest.date1 != null) {
            subject_date1.setText(sdf.format(mSubjectTest.date1));
        }

        if (mSubjectTest.date2 != null) {
            subject_date2.setText(sdf.format(mSubjectTest.date2));
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

}
