package com.thm.unicap.app.calendar;


import com.thm.unicap.app.R;
import com.thm.unicap.app.UnicapApplication;
import com.thm.unicap.app.database.DatabaseDependentFragment;
import com.thm.unicap.app.model.Student;
import com.thm.unicap.app.model.SubjectTest;

import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class CalendarFragment extends DatabaseDependentFragment {

    public CalendarFragment() {
        super(R.layout.fragment_calendar);
    }

    @Override
    public void initDatabaseDependentViews() {
        Student student = UnicapApplication.getCurrentStudent();

        List<SubjectTest> subjectTests = student.getSubjectTestsOrdered();

        if (subjectTests.size() == 0) {
            setContentView(R.layout.content_empty_calendar);
            return;
        }

        StickyListHeadersListView subjectCalendarListView = (StickyListHeadersListView) getContentView().findViewById(R.id.calendar_list);

        SubjectCalendarListItemAdapter subjectCalendarListItemAdapter = new SubjectCalendarListItemAdapter(subjectTests, getActivity());

        subjectCalendarListView.setAdapter(subjectCalendarListItemAdapter);
    }

}
