package com.thm.unicap.app.dashboard;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.thm.unicap.app.R;
import com.thm.unicap.app.model.Student;

public class StudentCoefficientCard extends CardView {

    private Student mStudent;

    public StudentCoefficientCard(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        View.inflate(getContext(), R.layout.card_student_coefficient, this);
    }

    private void initData() {
        TextView course_coefficient = (TextView) findViewById(R.id.course_coefficient);
        TextView last_coefficient = (TextView) findViewById(R.id.last_coefficient);

        course_coefficient.setText(mStudent.courseCoefficient.toString());
        last_coefficient.setText(mStudent.lastCoefficient.toString());
    }

    public void setStudent(Student student) {
        mStudent = student;
        initData();
    }

}
