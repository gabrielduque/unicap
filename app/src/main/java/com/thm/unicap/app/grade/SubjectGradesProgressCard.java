package com.thm.unicap.app.grade;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.View;

import com.echo.holographlibrary.Line;
import com.echo.holographlibrary.LineGraph;
import com.echo.holographlibrary.LinePoint;
import com.thm.unicap.app.R;
import com.thm.unicap.app.model.Subject;
import com.thm.unicap.app.model.SubjectTest;

import java.util.ArrayList;

public class SubjectGradesProgressCard extends CardView {

    private Subject mSubject;

    public SubjectGradesProgressCard(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        View.inflate(getContext(), R.layout.card_subject_grades_progress, this);
    }

    private void initData() {

        SubjectTest firstDegreeTest = mSubject.getTestByDegree(SubjectTest.Degree.FIRST_DEGREE);
        Float firstDegreeTestGrade = (firstDegreeTest != null) ? firstDegreeTest.grade : null;

        SubjectTest secondDegreeTest = mSubject.getTestByDegree(SubjectTest.Degree.SECOND_DEGREE);
        Float secondDegreeTestGrade = (secondDegreeTest != null) ? secondDegreeTest.grade : null;

        SubjectTest finalDegreeTest = mSubject.getTestByDegree(SubjectTest.Degree.FINAL_DEGREE);
        Float finalDegreeTestGrade = (finalDegreeTest != null) ? finalDegreeTest.grade : null;

        ArrayList<Float> grades = new ArrayList<Float>();
        grades.add(firstDegreeTestGrade);
        grades.add(secondDegreeTestGrade);
        grades.add(finalDegreeTestGrade);


        // Creating graph structure
        LineGraph graph = (LineGraph) findViewById(R.id.linegraph);
        graph.setUsingDips(true);

        graph.setRangeY(0, 10);
        graph.setRangeX(0, grades.size()-1);
        graph.setLineToFill(1);

        // Creating Grade Line
        Line gradeLine = new Line();
        gradeLine.setUsingDips(true);
        gradeLine.setColor(getContext().getResources().getColor(R.color.unicap_base));

        for (int i = 0; i < grades.size(); i++) {
            if(grades.get(i) != null) {
                gradeLine.addPoint(createGradePoint(i, grades.get(i)));
            }
        }

        graph.addLine(gradeLine);

        // Creating Average Line
        Line averageLine = new Line();
        averageLine.setColor(getContext().getResources().getColor(android.R.color.darker_gray));
        averageLine.setUsingDips(true);
        averageLine.setStrokeWidth(1);
        averageLine.setShowingPoints(false);

        averageLine.addPoint(createAveragePoint(0));
        averageLine.addPoint(createAveragePoint(grades.size()-1));

        graph.addLine(averageLine);

    }

    private LinePoint createGradePoint(int x, Float y)
    {
        LinePoint point = new LinePoint();
        point.setX(x);
        point.setY(y);
        if(y >= SubjectTest.MIN_AVERAGE) {
            point.setColor(getContext().getResources().getColor(android.R.color.holo_green_light));
            point.setSelectedColor(getContext().getResources().getColor(android.R.color.holo_green_dark));
        } else {
            point.setColor(getContext().getResources().getColor(android.R.color.holo_red_light));
            point.setSelectedColor(getContext().getResources().getColor(android.R.color.holo_red_dark));
        }
        return point;
    }

    private LinePoint createAveragePoint(int x)
    {
        LinePoint point = new LinePoint();
        point.setX(x);
        point.setY(SubjectTest.MIN_AVERAGE);

        return point;
    }

    public void setSubject(Subject subject) {
        this.mSubject = subject;
        initData();
    }

}
