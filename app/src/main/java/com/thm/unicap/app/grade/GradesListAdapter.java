package com.thm.unicap.app.grade;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.thm.unicap.app.R;
import com.thm.unicap.app.model.Subject;
import com.thm.unicap.app.model.SubjectTest;
import com.thm.unicap.app.util.GenericAdapter;

import java.util.List;

public class GradesListAdapter extends GenericAdapter<Subject> {

    protected GradesListAdapter(List<Subject> items, Context context) {
        super(items, context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = li.inflate(R.layout.card_subject_grades_list_item, parent, false);
        }

        Subject subject = getItem(position);

        TextView subject_name_abbreviation = (TextView) convertView.findViewById(R.id.subject_name_abbreviation);
        TextView subject_code = (TextView) convertView.findViewById(R.id.subject_code);
        TextView subject_name = (TextView) convertView.findViewById(R.id.subject_name);
        TextView subject_first_degree  = (TextView) convertView.findViewById(R.id.subject_first_degree);
        TextView subject_second_degree = (TextView) convertView.findViewById(R.id.subject_second_degree);
        TextView subject_final_degree  = (TextView) convertView.findViewById(R.id.subject_final_degree);

        subject_name_abbreviation.setBackgroundResource(subject.getColorCircleResource());
        subject_name_abbreviation.setText(subject.getNameAbbreviation());
        subject_code.setText(subject.code);
        subject_name.setText(subject.name);

        initSubjectTest(SubjectTest.Degree.FIRST_DEGREE, subject_first_degree, subject);
        initSubjectTest(SubjectTest.Degree.SECOND_DEGREE, subject_second_degree, subject);
        initSubjectTest(SubjectTest.Degree.FINAL_DEGREE, subject_final_degree, subject);

        return convertView;
    }

    private void initSubjectTest(SubjectTest.Degree subjectDegree, TextView textView, Subject subject) {
        SubjectTest subjectTest = subject.getTestByDegree(subjectDegree);
        if (subjectTest != null && subjectTest.grade != null) {
            textView.setText(subjectTest.grade.toString());

            if(subjectTest.grade >= SubjectTest.MIN_AVERAGE)
                textView.setTextColor(getContext().getResources().getColor(android.R.color.holo_green_light));
            else
                textView.setTextColor(getContext().getResources().getColor(android.R.color.holo_red_light));

        } else {
            textView.setText("-");
            textView.setTextColor(getContext().getResources().getColor(android.R.color.darker_gray));
        }
    }
}
