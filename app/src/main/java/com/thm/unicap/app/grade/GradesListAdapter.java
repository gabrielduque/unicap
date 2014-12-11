package com.thm.unicap.app.grade;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thm.unicap.app.R;
import com.thm.unicap.app.model.Subject;
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
            convertView = li.inflate(R.layout.grades_list_item, parent, false);
        }

        SubjectGradesListItemCard subjectGradesListItemCard = (SubjectGradesListItemCard) convertView;
        subjectGradesListItemCard.setSubject(getItem(position));

        return convertView;
    }
}
