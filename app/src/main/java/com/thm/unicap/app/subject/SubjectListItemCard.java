package com.thm.unicap.app.subject;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.thm.unicap.app.R;
import com.thm.unicap.app.model.Subject;

import it.gmariotti.cardslib.library.internal.Card;

public class SubjectListItemCard extends Card implements Card.OnCardClickListener {

    private Subject mSubject;

    public SubjectListItemCard(Context context, Subject subject) {
        this(context, R.layout.card_subject_list_item, subject);
    }

    public SubjectListItemCard(Context context, int innerLayout, Subject subject) {
        super(context, innerLayout);
        mSubject = subject;
        init();
    }

    private void init() {
        setClickable(true);
        setOnClickListener(this);
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {
        TextView subject_name_abbreviation = (TextView) parent.findViewById(R.id.subject_name_abbreviation);
        TextView subject_code = (TextView) parent.findViewById(R.id.subject_code);
        TextView subject_name = (TextView) parent.findViewById(R.id.subject_name);
        TextView subject_period = (TextView) parent.findViewById(R.id.subject_period);

        subject_name_abbreviation.setBackgroundResource(mSubject.getColorResource());
        subject_name_abbreviation.setText(mSubject.getNameAbbreviation());
        subject_code.setText(mSubject.code);
        subject_name.setText(mSubject.name);

        if (mSubject.period == null)
            subject_period.setText("");
        else
            subject_period.setText(String.format(mContext.getString(R.string.period_format), mSubject.period));
    }

    @Override
    public void onClick(Card card, View view) {
        Intent intent = new Intent(mContext, SubjectActivity.class);
        intent.putExtra("subject_id", mSubject.getId());
        mContext.startActivity(intent);
    }

}
