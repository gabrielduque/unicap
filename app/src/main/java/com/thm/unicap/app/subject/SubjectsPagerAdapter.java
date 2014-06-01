package com.thm.unicap.app.subject;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.thm.unicap.app.R;

public class SubjectsPagerAdapter extends PagerAdapter {

    private Context context;

    public static enum Session {
        PAST, ACTUAL, PENDING
    }

    public SubjectsPagerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return Session.values().length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        //TODO: Check this
        return false;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //TODO: Destroy page
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        //TODO: Instantiate page
        return Session.values()[position];
    }

    @Override
    public CharSequence getPageTitle(int position) {

        switch (Session.values()[position]) {
            case PAST:
                return context.getString(R.string.past);
            case ACTUAL:
                return context.getString(R.string.actual);
            case PENDING:
                return context.getString(R.string.pending);
            default:
                return null;
        }

    }
}
