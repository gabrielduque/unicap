package com.thm.unicap.app.lessons;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.thm.unicap.app.R;
import com.thm.unicap.app.model.Student;
import com.thm.unicap.app.model.Subject;
import com.thm.unicap.app.model.SubjectStatus;
import com.thm.unicap.app.subject.SubjectActivity;
import com.thm.unicap.app.util.UnicapUtils;
import com.thm.unicap.app.util.ViewUtils;

import java.util.List;

public class WeekdayLessonsCard extends CardView implements AdapterView.OnItemClickListener {

    private SubjectStatus.ScheduleWeekday mScheduleWeekday;
    private Student mStudent;
    private ListView mLessonsList;

    public WeekdayLessonsCard(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        View.inflate(getContext(), R.layout.card_lessons, this);
    }

    private void initData() {
        if(mStudent == null || mScheduleWeekday == null)
            return;

        TextView card_lessons_weekday = (TextView) findViewById(R.id.card_lessons_weekday);
        ImageView empty_day_view = (ImageView) findViewById(R.id.empty_day_view);
        card_lessons_weekday.setText(UnicapUtils.scheduleWeekDayToString(getContext(), mScheduleWeekday));

        List<Subject> subjectsFromWeekDay = mStudent.getSubjectsFromWeekDay(mScheduleWeekday);

        if(subjectsFromWeekDay.size() > 0) {
            LessonsAdapter lessonsAdapter = new LessonsAdapter(subjectsFromWeekDay, getContext(), mScheduleWeekday);
            mLessonsList = (ListView) findViewById(R.id.lessons_list);

            mLessonsList.setAdapter(lessonsAdapter);

            ViewUtils.setListViewHeightBasedOnChildren(mLessonsList);

            mLessonsList.setOnItemClickListener(this);

            empty_day_view.setVisibility(GONE);
        } else {
            empty_day_view.setVisibility(VISIBLE);
        }
    }

    public void setStudent(Student student) {
        mStudent = student;
        initData();
    }

    public void setScheduleWeekDay(SubjectStatus.ScheduleWeekday scheduleWeekday) {
        mScheduleWeekday = scheduleWeekday;
        initData();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getContext(), SubjectActivity.class);
        Subject subject = (Subject) mLessonsList.getAdapter().getItem(position);
        intent.putExtra("subject_id", subject.getId());
        getContext().startActivity(intent);
    }


}
