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

import it.gmariotti.cardslib.library.internal.Card;

public class SubjectGradesProgressCard extends Card {

    private Subject mSubject;

    public SubjectGradesProgressCard(Context context, Subject subject) {
        super(context, R.layout.card_subject_grades_progress);
        mSubject = subject;
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {
        Line line = new Line();
        LinePoint point;
        line.setUsingDips(true);
        line.setColor(mContext.getResources().getColor(R.color.unicap_base));

        point = new LinePoint();
        point.setX(0.0);
        point.setY(3.5);
        point.setColor(mContext.getResources().getColor(android.R.color.holo_red_light));
        point.setSelectedColor(mContext.getResources().getColor(android.R.color.holo_red_dark));
        line.addPoint(point);

        point = new LinePoint();
        point.setX(1.0);
        point.setY(8.0);
        point.setColor(mContext.getResources().getColor(android.R.color.holo_green_light));
        point.setSelectedColor(mContext.getResources().getColor(android.R.color.holo_green_dark));
        line.addPoint(point);

        point = new LinePoint();
        point.setX(2.0);
        point.setY(6.0);
        point.setColor(mContext.getResources().getColor(android.R.color.holo_green_light));
        point.setSelectedColor(mContext.getResources().getColor(android.R.color.holo_green_dark));
        line.addPoint(point);

        Line averageLine = new Line();
        averageLine.setColor(mContext.getResources().getColor(android.R.color.darker_gray));
        averageLine.setUsingDips(true);
        averageLine.setStrokeWidth(1);
        averageLine.setShowingPoints(false);

        point = new LinePoint();
        point.setX(0.0);
        point.setY(SubjectTest.MIN_AVERAGE);
        averageLine.addPoint(point);

        point = new LinePoint();
        point.setX(2.0);
        point.setY(SubjectTest.MIN_AVERAGE);
        averageLine.addPoint(point);

        LineGraph lineGraph = (LineGraph) parent.findViewById(R.id.linegraph);
        lineGraph.setUsingDips(true);
        lineGraph.addLine(line);
        lineGraph.addLine(averageLine);
        lineGraph.setRangeY(0, 10);
        lineGraph.setLineToFill(0);

        lineGraph.setOnPointClickedListener(new LineGraph.OnPointClickedListener() {

            @Override
            public void onClick(int lineIndex, int pointIndex) {
                //TODO: implement label on graph
            }
        });
    }

}
