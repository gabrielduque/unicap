package com.thm.unicap.app.grade;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.thm.unicap.app.R;
import com.thm.unicap.app.model.Subject;
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
        TextView card_subject_grades_final_degree = (TextView) parent.findViewById(R.id.card_subject_grades_final_degree);
        TextView card_subject_grades_average = (TextView) parent.findViewById(R.id.card_subject_grades_average);
        TextView card_subject_grades_situation = (TextView) parent.findViewById(R.id.card_subject_grades_situation);

        Float firstDegreeTestGrade = mSubject.getTestByDegree(SubjectTest.Degree.FIRST_DEGREE).grade;
        setupGradeView(firstDegreeTestGrade, card_subject_grades_first_degree);

        Float secondDegreeTestGrade = mSubject.getTestByDegree(SubjectTest.Degree.SECOND_DEGREE).grade;
        setupGradeView(secondDegreeTestGrade, card_subject_grades_second_degree);

        Float finalDegreeTestGrade = mSubject.getTestByDegree(SubjectTest.Degree.FINAL_DEGREE).grade;
        setupGradeView(finalDegreeTestGrade, card_subject_grades_final_degree);

        Float averageTestGrade = SubjectTest.calculateGrade(firstDegreeTestGrade, secondDegreeTestGrade, finalDegreeTestGrade);
        setupGradeView(averageTestGrade, card_subject_grades_average);

        //TODO: Organize and implement many other possibilities
        if(averageTestGrade != null) {
            if (averageTestGrade >= SubjectTest.MIN_AVERAGE) {
                card_subject_grades_situation.setText(mContext.getString(R.string.approved));
                card_subject_grades_situation.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.ic_action_accept), null, null, null);
                card_subject_grades_situation.setTextColor(mContext.getResources().getColor(android.R.color.holo_green_light));
            } else {
                card_subject_grades_situation.setText(mContext.getString(R.string.repproved));
                card_subject_grades_situation.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.ic_action_cancel), null, null, null);
                card_subject_grades_situation.setTextColor(mContext.getResources().getColor(android.R.color.holo_red_light));
            }
        } else {
            card_subject_grades_situation.setText(mContext.getString(R.string.waiting));
            card_subject_grades_situation.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.ic_action_help), null, null, null);
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
