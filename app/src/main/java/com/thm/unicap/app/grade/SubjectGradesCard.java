package com.thm.unicap.app.grade;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.thm.unicap.app.R;
import com.thm.unicap.app.model.Subject;
import com.thm.unicap.app.model.SubjectStatus;
import com.thm.unicap.app.model.SubjectTest;

public class SubjectGradesCard extends CardView {

    private Subject mSubject;

    public SubjectGradesCard(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        View.inflate(getContext(), R.layout.card_subject_grades, this);
    }

    private void initData() {
        TextView card_subject_grades_first_degree = (TextView) findViewById(R.id.card_subject_grades_first_degree);
        TextView card_subject_grades_second_degree = (TextView) findViewById(R.id.card_subject_grades_second_degree);
        TextView card_subject_grades_average = (TextView) findViewById(R.id.card_subject_grades_average);
        TextView card_subject_grades_final_degree = (TextView) findViewById(R.id.card_subject_grades_final_degree);
        TextView card_subject_grades_final_average = (TextView) findViewById(R.id.card_subject_grades_final_average);

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
                textView.setTextColor(getContext().getResources().getColor(android.R.color.holo_green_light));
            else
                textView.setTextColor(getContext().getResources().getColor(android.R.color.holo_red_light));
        }
    }

    public void setSubject(Subject subject) {
        this.mSubject = subject;
        initData();
    }
}
