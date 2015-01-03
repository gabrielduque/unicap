package com.thm.unicap.app.lessons;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.thm.unicap.app.R;
import com.thm.unicap.app.UnicapApplication;
import com.thm.unicap.app.model.SubjectStatus;
import com.thm.unicap.app.database.DatabaseDependentFragment;

public class LessonsFragment extends DatabaseDependentFragment {

    public LessonsFragment() {
        super(R.layout.fragment_lessons);
    }

    @Override
    public void initDatabaseDependentViews() {

        Animation enterAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.slideup_and_fadein);

        WeekdayLessonsCard weekdayLessonsCard;

        // Card: Mon

        weekdayLessonsCard = (WeekdayLessonsCard) getContentView().findViewById(R.id.card_lessons_mon);
        weekdayLessonsCard.setStudent(UnicapApplication.getCurrentStudent());
        weekdayLessonsCard.setScheduleWeekDay(SubjectStatus.ScheduleWeekday.Mon);
        weekdayLessonsCard.startAnimation(enterAnimation);

        // Card: Tue

        weekdayLessonsCard = (WeekdayLessonsCard) getContentView().findViewById(R.id.card_lessons_tue);
        weekdayLessonsCard.setStudent(UnicapApplication.getCurrentStudent());
        weekdayLessonsCard.setScheduleWeekDay(SubjectStatus.ScheduleWeekday.Tue);
        weekdayLessonsCard.startAnimation(enterAnimation);

        // Card: Wed

        weekdayLessonsCard = (WeekdayLessonsCard) getContentView().findViewById(R.id.card_lessons_wed);
        weekdayLessonsCard.setStudent(UnicapApplication.getCurrentStudent());
        weekdayLessonsCard.setScheduleWeekDay(SubjectStatus.ScheduleWeekday.Wed);
        weekdayLessonsCard.startAnimation(enterAnimation);

        // Card: Thu

        weekdayLessonsCard = (WeekdayLessonsCard) getContentView().findViewById(R.id.card_lessons_thu);
        weekdayLessonsCard.setStudent(UnicapApplication.getCurrentStudent());
        weekdayLessonsCard.setScheduleWeekDay(SubjectStatus.ScheduleWeekday.Thu);
        weekdayLessonsCard.startAnimation(enterAnimation);

        // Card: Fri

        weekdayLessonsCard = (WeekdayLessonsCard) getContentView().findViewById(R.id.card_lessons_fri);
        weekdayLessonsCard.setStudent(UnicapApplication.getCurrentStudent());
        weekdayLessonsCard.setScheduleWeekDay(SubjectStatus.ScheduleWeekday.Fri);
        weekdayLessonsCard.startAnimation(enterAnimation);

        // Card: Sat

        weekdayLessonsCard = (WeekdayLessonsCard) getContentView().findViewById(R.id.card_lessons_sat);
        weekdayLessonsCard.setStudent(UnicapApplication.getCurrentStudent());
        weekdayLessonsCard.setScheduleWeekDay(SubjectStatus.ScheduleWeekday.Sat);
        weekdayLessonsCard.startAnimation(enterAnimation);

    }

}