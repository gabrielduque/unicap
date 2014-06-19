package com.thm.unicap.app.dashboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thm.unicap.app.R;
import com.thm.unicap.app.UnicapApplication;
import com.thm.unicap.app.model.Student;
import com.thm.unicap.app.model.Subject;
import com.thm.unicap.app.model.SubjectStatus;
import com.thm.unicap.app.util.UnicapUtils;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import it.gmariotti.cardslib.library.internal.Card;

public class TodayLessonsCard extends Card {

    public TodayLessonsCard(Context context) {
        super(context, R.layout.card_today_lessons);
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {

        Student student = UnicapApplication.getStudent();

        SubjectStatus.ScheduleWeekDay currentScheduleWeekDay = UnicapUtils.getCurrentScheduleWeekDay();

        List<Subject> todaySubjects = student.getSubjectsFromWeekDay(currentScheduleWeekDay);

        ViewGroup lessonsContainer = (ViewGroup) parent.findViewById(R.id.card_today_lessons_container);
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Animation expand = AnimationUtils.loadAnimation(mContext, R.anim.expand_from_left_to_right);
        long currentTime = Calendar.getInstance().getTimeInMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

        for (Subject subject : todaySubjects) {

            HashMap<SubjectStatus.ScheduleWeekDay, SubjectStatus.ScheduleHour> fullSchedule = subject.getActualSubjectStatus().getFullSchedule();

            SubjectStatus.ScheduleHour scheduleHour = fullSchedule.get(currentScheduleWeekDay);

            Time[] timesFromScheduleHour = UnicapUtils.getTimesFromScheduleHour(scheduleHour);

            long beginTime = timesFromScheduleHour[0].getTime();
            long endTime = timesFromScheduleHour[1].getTime();

            View lessonsEntry = layoutInflater.inflate(R.layout.card_today_lessons_entry, lessonsContainer, false);

            TextView subject_name_abbreviation = (TextView) lessonsEntry.findViewById(R.id.subject_name_abbreviation);
            TextView subject_name = (TextView) lessonsEntry.findViewById(R.id.subject_name);
            TextView subject_room = (TextView) lessonsEntry.findViewById(R.id.subject_room);
            TextView subject_schedule_begin = (TextView) lessonsEntry.findViewById(R.id.subject_schedule_begin);
            TextView subject_schedule_end = (TextView) lessonsEntry.findViewById(R.id.subject_schedule_end);
            View subject_progress = lessonsEntry.findViewById(R.id.subject_progress);

            subject_name_abbreviation.setBackgroundResource(subject.getColorResource());
            subject_name_abbreviation.setText(subject.getNameAbbreviation());
            subject_name.setText(subject.name);
            subject_room.setText(subject.getActualSubjectStatus().room);

            subject_schedule_begin.setText(sdf.format(new Date(beginTime)));
            subject_schedule_end.setText(sdf.format(new Date(endTime)));

            if(currentTime <  beginTime) {
                subject_progress.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 0f));
            } else if (currentTime > endTime) {
                subject_progress.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 100f));
                subject_progress.setBackgroundResource(subject.getColorResource());
            } else {
                float progress = currentTime - beginTime;
                float total = endTime - beginTime;
                float percent = progress / total * 100f;
                subject_progress.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, percent));
                subject_progress.setBackgroundResource(subject.getColorResource());
            }

            lessonsContainer.addView(lessonsEntry);

            subject_progress.startAnimation(expand);
        }

    }

}
