package com.thm.unicap.app.subject;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.activeandroid.query.From;
import com.activeandroid.query.Select;
import com.nhaarman.listviewanimations.appearance.AnimationAdapter;
import com.nhaarman.listviewanimations.appearance.simple.SwingBottomInAnimationAdapter;
import com.thm.unicap.app.R;
import com.thm.unicap.app.UnicapApplication;
import com.thm.unicap.app.model.Student;
import com.thm.unicap.app.model.Subject;
import com.thm.unicap.app.model.SubjectStatus;

import java.util.List;

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

        Student student = UnicapApplication.getCurrentStudent();

        From query = new Select("Subject.*")
                .from(Subject.class)
                .innerJoin(SubjectStatus.class)
                .on("Subject.Id = SubjectStatus.Subject")
                .where("Subject.Student = ?", student.getId())
                .orderBy("Subject.Period, SubjectStatus.PaidIn, Subject.Name");

        if(position == Session.PAST.ordinal()) {
            query.where("SubjectStatus.Situation != ?", SubjectStatus.Situation.ACTUAL)
                    .and("SubjectStatus.Situation != ?", SubjectStatus.Situation.PENDING);
        } else if(position == Session.ACTUAL.ordinal()) {
            query.where("SubjectStatus.Situation = ?", SubjectStatus.Situation.ACTUAL);
        } else if(position == Session.PENDING.ordinal()) {
            query.where("SubjectStatus.Situation = ?", SubjectStatus.Situation.PENDING);
        }

        List<Subject> subjects = query.execute();

        ListView subjectListView = new ListView(mContext);

        subjectListView.setDivider(null);
        subjectListView.setDividerHeight(0);

        final SubjectListItemAdapter subjectListItemAdapter = new SubjectListItemAdapter(subjects, mContext);

        AnimationAdapter animCardArrayAdapter = new SwingBottomInAnimationAdapter(subjectListItemAdapter);
        animCardArrayAdapter.setAbsListView(subjectListView);
        subjectListView.setAdapter(animCardArrayAdapter);

        subjectListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Subject subject = subjectListItemAdapter.getItem(position);

                Intent intent = new Intent(mContext, SubjectActivity.class);
                intent.putExtra("subject_id", subject.getId());
                mContext.startActivity(intent);
            }
        });

        container.addView(subjectListView);

        return subjectListView;
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
