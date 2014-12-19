package com.thm.unicap.app.subject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
            convertView = li.inflate(R.layout.subject_list_item, parent, false);
        }

        SubjectListItemCard subjectListItemCard = (SubjectListItemCard) convertView.findViewById(R.id.card_subject_list_item);
        subjectListItemCard.setSubject(getItem(position));

        return convertView;
    }
}
