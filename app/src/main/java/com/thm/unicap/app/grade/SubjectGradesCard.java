package com.thm.unicap.app.grade;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.thm.unicap.app.R;
import com.thm.unicap.app.model.Subject;
import com.thm.unicap.app.model.SubjectStatus;
import com.thm.unicap.app.model.SubjectTest;

import it.gmariotti.cardslib.library.internal.Card;

public class SubjectGradesCard extends Card {

    private Subject mSubject;

    public SubjectGradesCard(Context context, Subject subject) {
        this(context, R.layout.card_subject_grades, subject);
    }

    public SubjectGradesCard(Context context, int innerLayout, Subject subject) {
        super(context, innerLayout);
        mSubject = subject;
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {
        TextView card_subject_grades_first_degree = (TextView) parent.findViewById(R.id.card_subject_grades_first_degree);
        TextView card_subject_grades_second_degree = (TextView) parent.findViewById(R.id.card_subject_grades_second_degree);
        TextView card_subject_grades_average = (TextView) parent.findViewById(R.id.card_subject_grades_average);
        TextView card_subject_grades_final_degree = (TextView) parent.findViewById(R.id.card_subject_grades_final_degree);
        TextView card_subject_grades_final_average = (TextView) parent.findViewById(R.id.card_subject_grades_final_average);

        SubjectTest firstDegreeTest = mSubject.getTestByDegree(SubjectTest.Degree.FIRST_DEGREE);
        Float firstDegreeTestGrade = (firstDegreeTest != null) ? firstDegreeTest.grade : null;
        setupGradeView(firstDegreeTestGrade, card_subject_grades_first_degree);

        SubjectTest secondDegreeTest = mSubject.getTestByDegree(SubjectTest.Degree.SECOND_DEGREE);
        Float secondDegreeTestGrade = (secondDegreeTest != null) ? secondDegreeTest.grade : null;
        setupGradeView(secondDegreeTestGrade, card_subject_grades_second_degree);

        SubjectTest finalDegreeTest = mSubject.getTestByDegree(SubjectTest.Degree.FINAL_DEGREE);
        Float finalDegreeTestGrade = (finalDegreeTest != null) ? finalDegreeTest.grade : null;
        setupGradeView(finalDegreeTestGrade, card_subject_grades_final_degree);

        SubjectStatus actualSubjectStatus = mSubject.getActualSubjectStatus();

        if(actualSubjectStatus != null) {
            Float averageTestGrade = actualSubjectStatus.average;
            setupGradeView(averageTestGrade, card_subject_grades_average);

            Float finalAverageTestGrade = actualSubjectStatus.final_average;
            setupGradeView(finalAverageTestGrade, card_subject_grades_final_average);
        }
    }

    private void setupGradeView(Float grade, TextView textView) {
        if(grade == null) {
            textView.setText("-");
        } else {
            textView.setText(grade.toString());
            if (grade >= SubjectTest.MIN_AVERAGE)
                textView.setTextColor(mContext.getResources().getColor(android.R.color.holo_green_light));
            else
                textView.setTextColor(mContext.getResources().getColor(android.R.color.holo_red_light));
        }
    }

}
