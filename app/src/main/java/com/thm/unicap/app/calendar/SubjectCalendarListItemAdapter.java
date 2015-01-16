package com.thm.unicap.app.calendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.thm.unicap.app.R;
import com.thm.unicap.app.model.SubjectTest;
import com.thm.unicap.app.util.GenericAdapter;

import java.text.SimpleDateFormat;
import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

public class SubjectCalendarListItemAdapter extends GenericAdapter<SubjectTest> implements StickyListHeadersAdapter {

    public SubjectCalendarListItemAdapter(List<SubjectTest> items, Context context) {
        super(items, context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = li.inflate(R.layout.subject_test_list_item, parent, false);
        }

        SubjectTest subjectTest = getItem(position);

        TextView subject_name_abbreviation = (TextView) convertView.findViewById(R.id.subject_name_abbreviation);
        TextView subject_name = (TextView) convertView.findViewById(R.id.subject_name);
        TextView subject_date1 = (TextView) convertView.findViewById(R.id.subject_date1);
        TextView subject_date2 = (TextView) convertView.findViewById(R.id.subject_date2);

        subject_name_abbreviation.setBackgroundResource(subjectTest.subject.getColorCircleResource());
        subject_name_abbreviation.setText(subjectTest.subject.getNameAbbreviation());
        subject_name.setText(subjectTest.subject.name);

        SimpleDateFormat sdf = new SimpleDateFormat(getContext().getString(R.string.date_format));

        if (subjectTest.date1 != null) {
            subject_date1.setText(sdf.format(subjectTest.date1));
        } else {
            subject_date1.setText("-");
        }

        if (subjectTest.date2 != null) {
            subject_date2.setText(sdf.format(subjectTest.date2));
        } else {
            subject_date2.setText("-");
        }

        return convertView;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = li.inflate(R.layout.subject_calendar_list_item_header, parent, false);
        }

        SubjectTest subjectTest = getItem(position);

        if (subjectTest != null) {
            TextView headerTextView = (TextView) convertView.findViewById(R.id.header_subject_calendar_list_item);

            switch (subjectTest.degree) {
                case FIRST_DEGREE:
                    headerTextView.setText(getContext().getString(R.string.first_degree));
                    break;
                case SECOND_DEGREE:
                    headerTextView.setText(getContext().getString(R.string.second_degree));
                    break;
                case FINAL_DEGREE:
                    headerTextView.setText(getContext().getString(R.string.final_degree));
                    break;
            }

        }


        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        return getItem(position).degree.ordinal();
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }
}
