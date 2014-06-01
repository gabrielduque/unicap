package com.thm.unicap.app.subject;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.activeandroid.Model;
import com.activeandroid.query.From;
import com.activeandroid.query.Select;
import com.thm.unicap.app.R;
import com.thm.unicap.app.UnicapApplication;
import com.thm.unicap.app.model.Student;
import com.thm.unicap.app.model.Subject;
import com.thm.unicap.app.model.SubjectStatus;

import java.util.ArrayList;
import java.util.List;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardExpand;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.view.CardListView;

public class SubjectsPagerAdapter extends PagerAdapter {

    private Context mContext;

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

        Student student = UnicapApplication.getStudent();

        From query = new Select()
                .from(Subject.class)
                .innerJoin(SubjectStatus.class)
                .on("Subject.Id = SubjectStatus.Subject")
                .where("Subject.Student = ?", student.getId())
                .orderBy("Subject.Period");

//        TODO: Convert to CursorAdapter
        if(position == Session.PAST.ordinal()) {
            query.where("SubjectStatus.Situation = ?", SubjectStatus.Situation.APPROVED);
        } else if(position == Session.ACTUAL.ordinal()) {
            query.where("SubjectStatus.Situation = ?", SubjectStatus.Situation.ACTUAL);
        } else if(position == Session.PENDING.ordinal()) {
            query.where("SubjectStatus.Situation = ?", SubjectStatus.Situation.PENDING);
        }

        List<Subject> subjects = query.execute();

        ArrayList<Card> cardArrayList = new ArrayList<Card>();

        for (Subject subject : subjects)
            cardArrayList.add(new SubjectCard(mContext, subject));

        CardListView cardListView = new CardListView(mContext);

        cardListView.setAdapter(new CardArrayAdapter(mContext, cardArrayList));

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
