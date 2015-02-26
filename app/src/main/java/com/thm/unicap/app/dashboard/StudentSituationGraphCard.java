package com.thm.unicap.app.dashboard;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.View;

import com.thm.unicap.app.R;
import com.thm.unicap.app.model.Student;
import com.thm.unicap.app.model.Subject;
import com.thm.unicap.app.model.SubjectStatus;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;

public class StudentSituationGraphCard extends CardView {

    private Student student;

    public StudentSituationGraphCard(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        View.inflate(getContext(), R.layout.card_situation_graph, this);
    }

    private void initData() {

        List<Subject> subjects = student.getActualSubjects();

        //TODO: use enum values instead of static variables
        float approvedCount = 0f;
        float repprovedCount = 0f;
        float waitingCount = 0f;

        float totalCount;

        for (Subject subject : subjects) {
            SubjectStatus actualSubjectStatus = subject.getActualSubjectStatus();

            if (actualSubjectStatus == null) continue;

            switch (actualSubjectStatus.getFlowSituation()) {
                case APPROVED:
                    approvedCount++;
                    break;
                case REPROVED:
                    repprovedCount++;
                    break;
                case WAITING:
                case WAITING_FINAL:
                    waitingCount++;
                    break;
            }
        }

        totalCount = approvedCount + repprovedCount + waitingCount;

        PieChartView pieChartView = (PieChartView) findViewById(R.id.graph);
        pieChartView.setValueSelectionEnabled(true);

        List<SliceValue> sliceValues = new ArrayList<>();

        float approvedPercentage = (approvedCount / totalCount) * 100f;
        int approvedColor = getResources().getColor(android.R.color.holo_green_light);
        SliceValue approvedSliceValue = new SliceValue();
        approvedSliceValue.setColor(approvedColor);
        approvedSliceValue.setValue(approvedPercentage);
        approvedSliceValue.setLabel(getLabelForValue(approvedPercentage));
        sliceValues.add(approvedSliceValue);

        float repprovedPercentage = (repprovedCount / totalCount) * 100f;
        int repprovedColor = getResources().getColor(android.R.color.holo_red_light);
        SliceValue repprovedSliceValue = new SliceValue();
        repprovedSliceValue.setColor(repprovedColor);
        repprovedSliceValue.setValue(repprovedPercentage);
        repprovedSliceValue.setLabel(getLabelForValue(repprovedPercentage));
        sliceValues.add(repprovedSliceValue);

        float waitingPercentage = (waitingCount / totalCount) * 100f;
        int waitingColor = getResources().getColor(R.color.unicap_gray_level_2);
        SliceValue waitingSliceValue = new SliceValue();
        waitingSliceValue.setColor(waitingColor);
        waitingSliceValue.setValue(waitingPercentage);
        waitingSliceValue.setLabel(getLabelForValue(waitingPercentage));
        sliceValues.add(waitingSliceValue);

        PieChartData data = new PieChartData(sliceValues);
        data.setHasLabelsOnlyForSelected(true);
        data.setHasCenterCircle(true);
        data.setCenterCircleScale(0.3f);

        pieChartView.setPieChartData(data);
    }

    private char[] getLabelForValue(float value) {
        return String.valueOf(Math.round(value)).concat("%").toCharArray();
    }

    public void setStudent(Student student) {
        this.student = student;
        initData();
    }

}
