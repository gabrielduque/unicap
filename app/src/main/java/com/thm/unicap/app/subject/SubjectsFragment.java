package com.thm.unicap.app.subject;

import android.support.v4.view.ViewPager;

import com.thm.unicap.app.R;
import com.thm.unicap.app.database.DatabaseDependentFragment;

public class SubjectsFragment extends DatabaseDependentFragment {

    public SubjectsFragment() {
        super(R.layout.fragment_subjects);
    }

    @Override
    public void initDatabaseDependentViews() {
        ViewPager mViewPager = (ViewPager) getContentView().findViewById(R.id.pager);
        mViewPager.setAdapter(new SubjectsPagerAdapter(getActivity()));
        mViewPager.setCurrentItem(SubjectsPagerAdapter.Session.ACTUAL.ordinal());
    }

}
