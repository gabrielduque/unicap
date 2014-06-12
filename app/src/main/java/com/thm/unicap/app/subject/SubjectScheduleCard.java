package com.thm.unicap.app.subject;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.thm.unicap.app.R;
import com.thm.unicap.app.model.Subject;
import com.thm.unicap.app.model.SubjectStatus;

import java.util.Map;

import it.gmariotti.cardslib.library.internal.Card;

public class SubjectScheduleCard extends Card {

    private Subject mSubject;

    public SubjectScheduleCard(Context context, Subject subject) {
        this(context, R.layout.card_subject_schedule, subject);
    }

    public SubjectScheduleCard(Context context, int innerLayout, Subject subject) {
        super(context, innerLayout);
        mSubject = subject;
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {
        SubjectStatus actualSubjectStatus = mSubject.getActualSubjectStatus();

        if(actualSubjectStatus != null) {

            int colorResource = mSubject.getColorResource();

            for (SubjectStatus.ScheduleWeekDay weekDay : actualSubjectStatus.getFullSchedule().keySet()) {

                TextView tvWeekDay = null;

                switch (weekDay) {
                    case Mon:
                        tvWeekDay = (TextView) parent.findViewById(R.id.card_subject_schedule_mon);
                        break;
                    case Tue:
                        tvWeekDay = (TextView) parent.findViewById(R.id.card_subject_schedule_tue);
                        break;
                    case Wed:
                        tvWeekDay = (TextView) parent.findViewById(R.id.card_subject_schedule_wed);
                        break;
                    case Thu:
                        tvWeekDay = (TextView) parent.findViewById(R.id.card_subject_schedule_thu);
                        break;
                    case Fri:
                        tvWeekDay = (TextView) parent.findViewById(R.id.card_subject_schedule_fri);
                        break;
                    case Sat:
                        tvWeekDay = (TextView) parent.findViewById(R.id.card_subject_schedule_sat);
                        break;
                }

                if (tvWeekDay != null) {
                    tvWeekDay.setBackgroundResource(colorResource);
                    tvWeekDay.setTextColor(mContext.getResources().getColor(android.R.color.white));
                }
            }
        }
    }

}
