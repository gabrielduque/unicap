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

        Float firstDegreeTestGrade = mSubject.getTestByDegree(SubjectTest.Degree.FIRST_DEGREE).grade;
        Float secondDegreeTestGrade = mSubject.getTestByDegree(SubjectTest.Degree.SECOND_DEGREE).grade;
        Float finalDegreeTestGrade = mSubject.getTestByDegree(SubjectTest.Degree.FINAL_DEGREE).grade;

        ArrayList<Float> floatPoints = new ArrayList<Float>();
        floatPoints.add(firstDegreeTestGrade);
        floatPoints.add(secondDegreeTestGrade);
        floatPoints.add(finalDegreeTestGrade);

        Line line = new Line();
        LinePoint point;
        line.setUsingDips(true);
        line.setColor(mContext.getResources().getColor(R.color.unicap_base));

        Line averageLine = new Line();
        averageLine.setColor(mContext.getResources().getColor(android.R.color.darker_gray));
        averageLine.setUsingDips(true);
        averageLine.setStrokeWidth(1);
        averageLine.setShowingPoints(false);

        float nextPointIndex = 0;
        for (Float floatPoint : floatPoints) {
            if(floatPoint != null) {
                point = new LinePoint();
                point.setX(nextPointIndex++);
                point.setY(floatPoint);
                if(floatPoint >= SubjectTest.MIN_AVERAGE) {
                    point.setColor(mContext.getResources().getColor(android.R.color.holo_green_light));
                    point.setSelectedColor(mContext.getResources().getColor(android.R.color.holo_green_dark));
                } else {
                    point.setColor(mContext.getResources().getColor(android.R.color.holo_red_light));
                    point.setSelectedColor(mContext.getResources().getColor(android.R.color.holo_red_dark));
                }
                line.addPoint(point);
            }
        }

        point = new LinePoint();
        point.setX(0.0);
        point.setY(SubjectTest.MIN_AVERAGE);
        averageLine.addPoint(point);

        point = new LinePoint();

        if(nextPointIndex > 0)
            point.setX(nextPointIndex-1);
        else
            point.setX(1);

        point.setY(SubjectTest.MIN_AVERAGE);
        averageLine.addPoint(point);

        LineGraph lineGraph = (LineGraph) parent.findViewById(R.id.linegraph);
        lineGraph.setUsingDips(true);

        if(nextPointIndex > 0)
            lineGraph.addLine(line);

        lineGraph.addLine(averageLine);
        lineGraph.setRangeY(0, 10);
        lineGraph.setLineToFill(0);

    }

}
