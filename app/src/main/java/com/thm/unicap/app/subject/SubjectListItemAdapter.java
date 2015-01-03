package com.thm.unicap.app.subject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.thm.unicap.app.R;
import com.thm.unicap.app.model.Subject;
import com.thm.unicap.app.util.GenericAdapter;

import java.util.List;

public class SubjectListItemAdapter extends GenericAdapter<Subject> {

    protected SubjectListItemAdapter(List<Subject> items, Context context) {
        super(items, context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = li.inflate(R.layout.card_subject_list_item, parent, false);
        }

        Subject subject = getItem(position);

        TextView subject_name_abbreviation = (TextView) convertView.findViewById(R.id.subject_name_abbreviation);
        TextView subject_code = (TextView) convertView.findViewById(R.id.subject_code);
        TextView subject_name = (TextView) convertView.findViewById(R.id.subject_name);
        TextView subject_period = (TextView) convertView.findViewById(R.id.subject_period);

        subject_name_abbreviation.setBackgroundResource(subject.getColorCircleResource());
        subject_name_abbreviation.setText(subject.getNameAbbreviation());
        subject_code.setText(subject.code);
        subject_name.setText(subject.name);

        if (subject.period == null)
            subject_period.setText("");
        else
            subject_period.setText(String.format(getContext().getString(R.string.period_format), subject.period));

        return convertView;
    }
}
