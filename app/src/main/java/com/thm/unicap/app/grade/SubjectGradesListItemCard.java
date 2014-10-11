package com.thm.unicap.app.grade;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.thm.unicap.app.R;
import com.thm.unicap.app.model.Subject;
import com.thm.unicap.app.model.SubjectTest;

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
        TextView subject_first_degree  = (TextView) parent.findViewById(R.id.subject_first_degree);
        TextView subject_second_degree = (TextView) parent.findViewById(R.id.subject_second_degree);
        TextView subject_final_degree  = (TextView) parent.findViewById(R.id.subject_final_degree);

        subject_name_abbreviation.setBackgroundResource(mSubject.getColorResource());
        subject_name_abbreviation.setText(mSubject.getNameAbbreviation());
        subject_code.setText(mSubject.code);
        subject_name.setText(mSubject.name);

        initSubjectTest(SubjectTest.Degree.FIRST_DEGREE, subject_first_degree);
        initSubjectTest(SubjectTest.Degree.SECOND_DEGREE, subject_second_degree);
        initSubjectTest(SubjectTest.Degree.FINAL_DEGREE, subject_final_degree);
    }

    private void initSubjectTest(SubjectTest.Degree subjectDegree, TextView textView) {
        SubjectTest subjectTest = mSubject.getTestByDegree(subjectDegree);
        if (subjectTest != null && subjectTest.grade != null) {
            textView.setText(subjectTest.grade.toString());

            if(subjectTest.grade >= SubjectTest.MIN_AVERAGE)
                textView.setTextColor(mContext.getResources().getColor(android.R.color.holo_green_light));
            else
                textView.setTextColor(mContext.getResources().getColor(android.R.color.holo_red_light));

        } else {
            textView.setText("-");
            textView.setTextColor(mContext.getResources().getColor(android.R.color.darker_gray));
        }
    }

    public Subject getSubject() {
        return mSubject;
    }
}
