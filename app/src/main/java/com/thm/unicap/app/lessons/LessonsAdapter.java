package com.thm.unicap.app.lessons;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thm.unicap.app.R;
import com.thm.unicap.app.model.Subject;
import com.thm.unicap.app.model.SubjectStatus;
import com.thm.unicap.app.util.GenericAdapter;
import com.thm.unicap.app.util.UnicapUtils;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class LessonsAdapter extends GenericAdapter<Subject> {

    private SubjectStatus.ScheduleWeekday mScheduleWeekday;

    public LessonsAdapter(List<Subject> items, Context context, SubjectStatus.ScheduleWeekday mScheduleWeekday) {
        super(items, context);
        this.mScheduleWeekday = mScheduleWeekday;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Keeps reference to avoid future findViewById()
        LessonItemViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = li.inflate(R.layout.card_weekday_lessons_entry, parent, false);

            viewHolder = new LessonItemViewHolder();

            viewHolder.subject_name_abbreviation = (TextView) convertView.findViewById(R.id.subject_name_abbreviation);
            viewHolder.subject_name = (TextView) convertView.findViewById(R.id.subject_name);
            viewHolder.subject_room = (TextView) convertView.findViewById(R.id.subject_room);
            viewHolder.subject_schedule_begin = (TextView) convertView.findViewById(R.id.subject_schedule_begin);
            viewHolder.subject_schedule_end = (TextView) convertView.findViewById(R.id.subject_schedule_end);
            viewHolder.subject_progress = convertView.findViewById(R.id.subject_progress);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (LessonItemViewHolder) convertView.getTag();
        }

        Subject subject = getItem(position);

        if (subject != null) {
            long currentTime = Calendar.getInstance().getTimeInMillis();
            SimpleDateFormat sdf = new SimpleDateFormat(getContext().getString(R.string.hour_format));
            Animation expand = AnimationUtils.loadAnimation(getContext(), R.anim.expand_from_left_to_right);

            HashMap<SubjectStatus.ScheduleWeekday, char[]> fullSchedule = subject.getActualSubjectStatus().getFullSchedule();

            char[] scheduleHour = fullSchedule.get(mScheduleWeekday);

            Time[] timesFromScheduleHour = UnicapUtils.getTimesFromScheduleHour(scheduleHour);

            long beginTime = timesFromScheduleHour[0].getTime();
            long endTime = timesFromScheduleHour[1].getTime();

            viewHolder.subject_name_abbreviation.setBackgroundResource(subject.getColorCircleResource());
            viewHolder.subject_name_abbreviation.setText(subject.getNameAbbreviation());
            viewHolder.subject_name.setText(subject.name);
            viewHolder.subject_room.setText(subject.getActualSubjectStatus().room);

            viewHolder.subject_schedule_begin.setText(sdf.format(new Date(beginTime)));
            viewHolder.subject_schedule_end.setText(sdf.format(new Date(endTime)));

            SubjectStatus.ScheduleWeekday currentScheduleWeekday = UnicapUtils.getCurrentScheduleWeekDay();

            if (mScheduleWeekday != currentScheduleWeekday || currentTime < beginTime) {
                viewHolder.subject_progress.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 0f));
            } else if (currentTime > endTime) {
                viewHolder.subject_progress.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 100f));
                viewHolder.subject_progress.setBackgroundResource(subject.getColorResource());
            } else {
                float progress = currentTime - beginTime;
                float total = endTime - beginTime;
                float percent = progress / total * 100f;
                viewHolder.subject_progress.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, percent));
                viewHolder.subject_progress.setBackgroundResource(subject.getColorResource());
            }

            viewHolder.subject_progress.startAnimation(expand);
        }

        return convertView;
    }

    static class LessonItemViewHolder {
        public TextView subject_name_abbreviation;
        public TextView subject_name;
        public TextView subject_room;
        public TextView subject_schedule_begin;
        public TextView subject_schedule_end;
        public View subject_progress;
    }
}
