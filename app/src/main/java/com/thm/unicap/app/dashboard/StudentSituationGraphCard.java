package com.thm.unicap.app.dashboard;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import com.echo.holographlibrary.PieGraph;
import com.echo.holographlibrary.PieSlice;
import com.echo.holographlibrary.Utils;
import com.thm.unicap.app.R;
import com.thm.unicap.app.model.Student;
import com.thm.unicap.app.model.Subject;
import com.thm.unicap.app.model.SubjectStatus;

import java.util.List;

public class StudentSituationGraphCard extends CardView implements PieGraph.OnSliceClickedListener {

    private PieGraph mPieGraph;
    private TextView mStatusPercentage;
    private int mActiveSlice = 0;
    private Student mStudent;

    public StudentSituationGraphCard(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        View.inflate(getContext(), R.layout.card_situation_graph, this);
    }

    private void initData() {

        List<Subject> subjects = mStudent.getActualSubjects();

        //TODO: use enum values instead of static variables
        float approvedCount = 0f;
        float repprovedCount = 0f;
        float waitingCount = 0f;

        float totalCount = 0f;

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

        if (approvedCount >= repprovedCount && approvedCount >= waitingCount)
            mActiveSlice = 0;
        else if (repprovedCount >= approvedCount && repprovedCount >= waitingCount)
            mActiveSlice = 1;
        else if (waitingCount >= approvedCount && waitingCount >= repprovedCount)
            mActiveSlice = 2;

        mPieGraph = (PieGraph) findViewById(R.id.graph);
        mStatusPercentage = (TextView) findViewById(R.id.status_percentage);
        PieSlice slice;

        slice = new PieSlice();
        slice.setColor(getResources().getColor(android.R.color.holo_green_light));
        slice.setSelectedColor(Utils.darkenColor(getResources().getColor(android.R.color.holo_green_light)));
        slice.setTitle(getResources().getString(R.string.approved));
        slice.setValue(1);
        slice.setGoalValue((approvedCount / totalCount) * 100f);
        mPieGraph.addSlice(slice);

        slice = new PieSlice();
        slice.setColor(getResources().getColor(android.R.color.holo_red_light));
        slice.setSelectedColor(Utils.darkenColor(getResources().getColor(android.R.color.holo_red_light)));
        slice.setTitle(getResources().getString(R.string.repproved));
        slice.setValue(1);
        slice.setGoalValue((repprovedCount / totalCount) * 100f);
        mPieGraph.addSlice(slice);

        slice = new PieSlice();
        slice.setColor(getResources().getColor(R.color.unicap_gray_level_2));
        slice.setSelectedColor(Utils.darkenColor(getResources().getColor(R.color.unicap_gray_level_2)));
        slice.setTitle(getResources().getString(R.string.waiting));
        slice.setValue(100);
        slice.setGoalValue((waitingCount / totalCount) * 100f);
        mPieGraph.addSlice(slice);

        mPieGraph.setOnSliceClickedListener(this);
        mPieGraph.setInnerCircleRatio(100);

        onClick(mActiveSlice);

        Animation rotation = AnimationUtils.loadAnimation(getContext(), R.anim.clockwise_scale_rotation);
        mPieGraph.startAnimation(rotation);

        mPieGraph.setDuration(1000);//default if unspecified is 300 ms
        mPieGraph.setInterpolator(new DecelerateInterpolator());//default if unspecified is linear
        mPieGraph.animateToGoalValues();
    }

    public void setStudent(Student student) {
        mStudent = student;
        initData();
    }

    @Override
    public void onClick(int index) {

        if (index == -1) return;

        mActiveSlice = index;
        PieSlice slice = mPieGraph.getSlice(index);

        mStatusPercentage.setText(String.valueOf((int) slice.getGoalValue()) + "%");
        mStatusPercentage.setTextColor(slice.getColor());
    }

}
