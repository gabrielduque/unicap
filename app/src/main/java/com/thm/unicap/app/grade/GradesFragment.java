package com.thm.unicap.app.grade;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.devspark.progressfragment.ProgressFragment;
import com.nhaarman.listviewanimations.appearance.AnimationAdapter;
import com.nhaarman.listviewanimations.appearance.simple.SwingBottomInAnimationAdapter;
import com.thm.unicap.app.MainActivity;
import com.thm.unicap.app.R;
import com.thm.unicap.app.UnicapApplication;
import com.thm.unicap.app.menu.NavigationDrawerFragment;
import com.thm.unicap.app.model.Student;
import com.thm.unicap.app.model.Subject;
import com.thm.unicap.app.util.DatabaseDependentFragment;
import com.thm.unicap.app.util.DatabaseListener;
import com.thm.unicap.app.util.NetworkUtils;

import java.util.List;

public class GradesFragment extends ProgressFragment implements DatabaseListener, DatabaseDependentFragment, AdapterView.OnItemClickListener {

    private ListView mGradesListView;
    private GradesListAdapter mGradesListAdapter;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        NavigationDrawerFragment navigationDrawerFragment = ((MainActivity) getActivity()).getNavigationDrawerFragment();

        if(navigationDrawerFragment != null && !navigationDrawerFragment.isDrawerOpen()) {
            inflater.inflate(R.menu.fragment_grades, menu);
        }
        super.onCreateOptionsMenu(menu, inflater);
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
    public void initDatabaseDependentViews() {
        Student student = UnicapApplication.getCurrentStudent();

        List<Subject> subjects = student.getActualSubjects();

        mGradesListView = (ListView) getContentView().findViewById(R.id.subjects_list);

        mGradesListAdapter = new GradesListAdapter(subjects, getActivity());

        AnimationAdapter animCardArrayAdapter = new SwingBottomInAnimationAdapter(mGradesListAdapter);
        animCardArrayAdapter.setAbsListView(mGradesListView);
        mGradesListView.setAdapter(animCardArrayAdapter);
        mGradesListView.setOnItemClickListener(this);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(NavigationDrawerFragment.SESSION_GRADES);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Subject subject = mGradesListAdapter.getItem(position);
        Intent intent = new Intent(getActivity(), GradesActivity.class);
        intent.putExtra("subject_id", subject.getId());
        getActivity().startActivity(intent);
    }

    @Override
    public void databaseSyncing() {

    }

    @Override
    public void databaseUpdated() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setContentView(R.layout.fragment_grades);
                initDatabaseDependentViews();
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
}
