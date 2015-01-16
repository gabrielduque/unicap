package com.thm.unicap.app.subject;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.thm.unicap.app.R;
import com.thm.unicap.app.model.Subject;
import com.thm.unicap.app.model.SubjectStatus;

public class SubjectScheduleCard extends CardView {

    private Subject mSubject;

    public SubjectScheduleCard(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        View.inflate(getContext(), R.layout.card_subject_schedule, this);
    }

    private void initData() {

        if (mSubject == null) return;

        SubjectStatus actualSubjectStatus = mSubject.getActualSubjectStatus();

        if (actualSubjectStatus != null) {

            int colorResource = mSubject.getColorResource();

            for (SubjectStatus.ScheduleWeekday weekDay : actualSubjectStatus.getFullSchedule().keySet()) {

                TextView tvWeekDay = null;

                switch (weekDay) {
                    case Mon:
                        tvWeekDay = (TextView) findViewById(R.id.card_subject_schedule_mon);
                        break;
                    case Tue:
                        tvWeekDay = (TextView) findViewById(R.id.card_subject_schedule_tue);
                        break;
                    case Wed:
                        tvWeekDay = (TextView) findViewById(R.id.card_subject_schedule_wed);
                        break;
                    case Thu:
                        tvWeekDay = (TextView) findViewById(R.id.card_subject_schedule_thu);
                        break;
                    case Fri:
                        tvWeekDay = (TextView) findViewById(R.id.card_subject_schedule_fri);
                        break;
                    case Sat:
                        tvWeekDay = (TextView) findViewById(R.id.card_subject_schedule_sat);
                        break;
                }

                if (tvWeekDay != null) {
                    tvWeekDay.setBackgroundResource(colorResource);
                    tvWeekDay.setTextColor(getContext().getResources().getColor(android.R.color.white));
                }
            }
        }
    }

    public Subject getSubject() {
        return mSubject;
    }

    public void setSubject(Subject subject) {
        this.mSubject = subject;
        initData();
    }

}
