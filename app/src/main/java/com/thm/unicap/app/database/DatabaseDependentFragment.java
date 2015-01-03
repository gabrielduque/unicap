package com.thm.unicap.app.database;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.devspark.progressfragment.ProgressFragment;
import com.thm.unicap.app.R;
import com.thm.unicap.app.UnicapApplication;
import com.thm.unicap.app.util.NetworkUtils;

public abstract class DatabaseDependentFragment extends ProgressFragment implements IDatabaseListener, SwipeRefreshLayout.OnRefreshListener {
    private SwipeRefreshLayout mSwipeLayout;
    private boolean mContentViewIsInflated = false;
    private int mLayoutResID;

    protected DatabaseDependentFragment(@LayoutRes int mLayoutResID) {
        this.mLayoutResID = mLayoutResID;
    }

    public abstract void initDatabaseDependentViews();

    private void initFragmentViews() {

        initDatabaseDependentViews();

        enterAnimation();

        View swipeRefreshLayout = getContentView().findViewById(R.id.swipe_container);

        if (swipeRefreshLayout != null) {
            mSwipeLayout = (SwipeRefreshLayout) swipeRefreshLayout;
            mSwipeLayout.setOnRefreshListener(this);
            mSwipeLayout.setColorSchemeResources(
                    R.color.unicap_base,
                    R.color.subject_color_1,
                    R.color.subject_color_2,
                    R.color.subject_color_3,
                    R.color.subject_color_4,
                    R.color.subject_color_5,
                    R.color.subject_color_6,
                    R.color.subject_color_7
            );
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        UnicapApplication.addDatabaseListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        UnicapApplication.removeDatabaseListener(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setHasOptionsMenu(true);

        if(UnicapApplication.hasStudentData()) { // Show offline data
            databaseUpdated();
        } else if (!NetworkUtils.isNetworkConnected(getActivity())) { // Show layout for offline error
            setContentView(R.layout.content_offline);
            setContentShown(true);
        }
    }

    @Override
    public void databaseSyncing() {
        if (mSwipeLayout != null) {
            mSwipeLayout.setRefreshing(true);
        }
    }

    @Override
    public void databaseUpdated() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mSwipeLayout != null) {
                    mSwipeLayout.setRefreshing(false);
                }
                if(!mContentViewIsInflated || getContentView() == null) {
                    setContentView(mLayoutResID);
                    mContentViewIsInflated = true;
                }
                initFragmentViews();
                setContentShown(true);
            }
        });
    }

    @Override
    public void databaseUnreachable(String message) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setContentView(R.layout.content_unreachable);
                setContentShown(true);
            }
        });
    }

    @Override
    public void onRefresh() {
        UnicapApplication.forceSync();
    }

    protected void enterAnimation() {
        Animation enterAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.slideup_and_fadein);
        View rootView = getView();
        if (rootView != null) {
            rootView.startAnimation(enterAnimation);
        }
    }
}
