package com.thm.unicap.app.grade;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.echo.holographlibrary.Line;
import com.echo.holographlibrary.LineGraph;
import com.echo.holographlibrary.LinePoint;
import com.thm.unicap.app.R;
import com.thm.unicap.app.model.Subject;
import com.thm.unicap.app.model.SubjectStatus;
import com.thm.unicap.app.model.SubjectTest;

import java.util.ArrayList;

import it.gmariotti.cardslib.library.internal.Card;

public class SubjectGradesProgressCard extends Card {

    private Subject mSubject;

    public SubjectGradesProgressCard(Context context, Subject subject) {
        super(context, R.layout.card_subject_grades_progress);
        mSubject = subject;
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {

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
        LineGraph graph = (LineGraph) parent.findViewById(R.id.linegraph);
        graph.setUsingDips(true);

        graph.setRangeY(0, 10);
        graph.setRangeX(0, grades.size()-1);
        graph.setLineToFill(1);

        // Creating Grade Line
        Line gradeLine = new Line();
        gradeLine.setUsingDips(true);
        gradeLine.setColor(mContext.getResources().getColor(R.color.unicap_base));

        for (int i = 0; i < grades.size(); i++) {
            if(grades.get(i) != null) {
                gradeLine.addPoint(createGradePoint(i, grades.get(i)));
            }
        }

        graph.addLine(gradeLine);

        // Creating Average Line
        Line averageLine = new Line();
        averageLine.setColor(mContext.getResources().getColor(android.R.color.darker_gray));
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
            point.setColor(mContext.getResources().getColor(android.R.color.holo_green_light));
            point.setSelectedColor(mContext.getResources().getColor(android.R.color.holo_green_dark));
        } else {
            point.setColor(mContext.getResources().getColor(android.R.color.holo_red_light));
            point.setSelectedColor(mContext.getResources().getColor(android.R.color.holo_red_dark));
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

}
