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
        TextView card_subject_grades_situation = (TextView) parent.findViewById(R.id.card_subject_grades_situation);

        ViewGroup card_subject_grades_situation_container = (ViewGroup) parent.findViewById(R.id.card_subject_grades_situation_container);

        Float firstDegreeTestGrade = mSubject.getTestByDegree(SubjectTest.Degree.FIRST_DEGREE).grade;
        setupGradeView(firstDegreeTestGrade, card_subject_grades_first_degree);

        Float secondDegreeTestGrade = mSubject.getTestByDegree(SubjectTest.Degree.SECOND_DEGREE).grade;
        setupGradeView(secondDegreeTestGrade, card_subject_grades_second_degree);

        Float finalDegreeTestGrade = mSubject.getTestByDegree(SubjectTest.Degree.FINAL_DEGREE).grade;
        setupGradeView(finalDegreeTestGrade, card_subject_grades_final_degree);

        Float averageTestGrade = mSubject.getActualSubjectStatus().average;
        setupGradeView(averageTestGrade, card_subject_grades_average);

        Float finalAverageTestGrade = mSubject.getActualSubjectStatus().final_average;
        setupGradeView(finalAverageTestGrade, card_subject_grades_final_average);

        SubjectStatus.FlowSituation flowSituation = mSubject.getActualSubjectStatus().getFlowSituation();

        switch (flowSituation) {
            case APPROVED:
                card_subject_grades_situation.setText(mContext.getString(R.string.approved));
                card_subject_grades_situation.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.ic_action_accept_light), null, null, null);
                card_subject_grades_situation_container.setBackgroundColor(mContext.getResources().getColor(android.R.color.holo_green_light));
                break;
            case REPROVED:
                card_subject_grades_situation.setText(mContext.getString(R.string.repproved));
                card_subject_grades_situation.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.ic_action_cancel_light), null, null, null);
                card_subject_grades_situation_container.setBackgroundColor(mContext.getResources().getColor(android.R.color.holo_red_light));
                break;
            case WAITING:
                card_subject_grades_situation.setText(mContext.getString(R.string.waiting));
                card_subject_grades_situation.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.ic_action_time_light), null, null, null);
                card_subject_grades_situation_container.setBackgroundColor(mContext.getResources().getColor(android.R.color.darker_gray));
                break;
            case WAITING_FINAL:
                card_subject_grades_situation.setText(mContext.getString(R.string.waiting_final));
                card_subject_grades_situation.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.ic_action_time_light), null, null, null);
                card_subject_grades_situation_container.setBackgroundColor(mContext.getResources().getColor(android.R.color.darker_gray));
                break;
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
