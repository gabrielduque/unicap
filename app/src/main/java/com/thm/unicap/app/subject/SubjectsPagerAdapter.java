package com.thm.unicap.app.subject;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.activeandroid.query.From;
import com.activeandroid.query.Select;
import com.nhaarman.listviewanimations.appearance.AnimationAdapter;
import com.nhaarman.listviewanimations.appearance.simple.SwingBottomInAnimationAdapter;
import com.thm.unicap.app.R;
import com.thm.unicap.app.UnicapApplication;
import com.thm.unicap.app.model.Student;
import com.thm.unicap.app.model.Subject;
import com.thm.unicap.app.model.SubjectStatus;

import java.util.ArrayList;
import java.util.List;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.view.CardListView;

public class SubjectsPagerAdapter extends PagerAdapter implements Card.OnCardClickListener {

    private Context mContext;

    @Override
    public void onClick(Card card, View view) {
        Intent intent = new Intent(mContext, SubjectActivity.class);
        intent.putExtra("subject_id", ((SubjectListItemCard)card).getSubject().getId());
        mContext.startActivity(intent);
    }

    public static enum Session {
        PAST, ACTUAL, PENDING
    }

    public SubjectsPagerAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return Session.values().length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        Student student = UnicapApplication.getCurrentStudent();

        From query = new Select("Subject.*")
                .from(Subject.class)
                .innerJoin(SubjectStatus.class)
                .on("Subject.Id = SubjectStatus.Subject")
                .where("Subject.Student = ?", student.getId())
                .orderBy("Subject.Period, SubjectStatus.PaidIn, Subject.Name");

        if(position == Session.PAST.ordinal()) {
            query.where("SubjectStatus.Situation = ?", SubjectStatus.Situation.APPROVED)
                    .or("SubjectStatus.Situation = ?", SubjectStatus.Situation.IMPORTED)
                    .or("SubjectStatus.Situation = ?", SubjectStatus.Situation.PERFORMED)
                    .or("SubjectStatus.Situation = ?", SubjectStatus.Situation.UNKNOWN);
        } else if(position == Session.ACTUAL.ordinal()) {
            query.where("SubjectStatus.Situation = ?", SubjectStatus.Situation.ACTUAL);
        } else if(position == Session.PENDING.ordinal()) {
            query.where("SubjectStatus.Situation = ?", SubjectStatus.Situation.PENDING);
        }

        List<Subject> subjects = query.execute();

        ArrayList<Card> cardArrayList = new ArrayList<Card>();

        for (Subject subject : subjects) {
            SubjectListItemCard subjectListItemCard = new SubjectListItemCard(mContext, subject);
            subjectListItemCard.setClickable(true);
            subjectListItemCard.setOnClickListener(this);
            cardArrayList.add(subjectListItemCard);
        }

        CardListView cardListView = new CardListView(mContext);

        CardArrayAdapter cardArrayAdapter = new CardArrayAdapter(mContext, cardArrayList);

        AnimationAdapter animCardArrayAdapter = new SwingBottomInAnimationAdapter(cardArrayAdapter);
        animCardArrayAdapter.setAbsListView(cardListView);
        cardListView.setExternalAdapter(animCardArrayAdapter,cardArrayAdapter);

        container.addView(cardListView);

        return cardListView;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        switch (Session.values()[position]) {
            case PAST:
                return mContext.getString(R.string.past);
            case ACTUAL:
                return mContext.getString(R.string.actual);
            case PENDING:
                return mContext.getString(R.string.pending);
            default:
                return null;
        }

    }
}
