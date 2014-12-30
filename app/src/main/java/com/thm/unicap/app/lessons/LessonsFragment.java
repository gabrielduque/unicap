package com.thm.unicap.app.lessons;

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

        WeekdayLessonsCard weekdayLessonsCard;

        // Card: Mon

        weekdayLessonsCard = (WeekdayLessonsCard) getContentView().findViewById(R.id.card_lessons_mon);
        weekdayLessonsCard.setStudent(UnicapApplication.getCurrentStudent());
        weekdayLessonsCard.setScheduleWeekDay(SubjectStatus.ScheduleWeekday.Mon);

        // Card: Tue

        weekdayLessonsCard = (WeekdayLessonsCard) getContentView().findViewById(R.id.card_lessons_tue);
        weekdayLessonsCard.setStudent(UnicapApplication.getCurrentStudent());
        weekdayLessonsCard.setScheduleWeekDay(SubjectStatus.ScheduleWeekday.Tue);

        // Card: Wed

        weekdayLessonsCard = (WeekdayLessonsCard) getContentView().findViewById(R.id.card_lessons_wed);
        weekdayLessonsCard.setStudent(UnicapApplication.getCurrentStudent());
        weekdayLessonsCard.setScheduleWeekDay(SubjectStatus.ScheduleWeekday.Wed);

        // Card: Thu

        weekdayLessonsCard = (WeekdayLessonsCard) getContentView().findViewById(R.id.card_lessons_thu);
        weekdayLessonsCard.setStudent(UnicapApplication.getCurrentStudent());
        weekdayLessonsCard.setScheduleWeekDay(SubjectStatus.ScheduleWeekday.Thu);

        // Card: Fri

        weekdayLessonsCard = (WeekdayLessonsCard) getContentView().findViewById(R.id.card_lessons_fri);
        weekdayLessonsCard.setStudent(UnicapApplication.getCurrentStudent());
        weekdayLessonsCard.setScheduleWeekDay(SubjectStatus.ScheduleWeekday.Fri);

        // Card: Sat

        weekdayLessonsCard = (WeekdayLessonsCard) getContentView().findViewById(R.id.card_lessons_sat);
        weekdayLessonsCard.setStudent(UnicapApplication.getCurrentStudent());
        weekdayLessonsCard.setScheduleWeekDay(SubjectStatus.ScheduleWeekday.Sat);

    }

}