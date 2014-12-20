package com.thm.unicap.app.subject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.thm.unicap.app.R;
import com.thm.unicap.app.calendar.CalendarListItemCard;
import com.thm.unicap.app.model.SubjectTest;
import com.thm.unicap.app.util.GenericAdapter;

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
            convertView = li.inflate(R.layout.subject_calendar_list_item, parent, false);
        }

        CalendarListItemCard calendarListItemCard = (CalendarListItemCard) convertView.findViewById(R.id.card_subject_calendar_list_item);
        calendarListItemCard.setSubjectTest(getItem(position));

        return convertView;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = li.inflate(R.layout.subject_calendar_list_item_header, parent, false);
        }

        SubjectTest subjectTest = getItem(position);

        if(subjectTest != null) {
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
