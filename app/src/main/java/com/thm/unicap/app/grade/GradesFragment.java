package com.thm.unicap.app.grade;


import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.thm.unicap.app.R;
import com.thm.unicap.app.UnicapApplication;
import com.thm.unicap.app.database.DatabaseDependentFragment;
import com.thm.unicap.app.model.Student;
import com.thm.unicap.app.model.Subject;

import java.util.List;

public class GradesFragment extends DatabaseDependentFragment implements AdapterView.OnItemClickListener {

    private GradesListAdapter mGradesListAdapter;

    public GradesFragment() {
        super(R.layout.fragment_grades);
    }

    @Override
    public void initDatabaseDependentViews() {
        Student student = UnicapApplication.getCurrentStudent();

        List<Subject> subjects = student.getActualSubjects();

        ListView mGradesListView = (ListView) getContentView().findViewById(R.id.subjects_list);

        mGradesListAdapter = new GradesListAdapter(subjects, getActivity());

        mGradesListView.setAdapter(mGradesListAdapter);
        mGradesListView.setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Subject subject = mGradesListAdapter.getItem(position);
        Intent intent = new Intent(getActivity(), GradesActivity.class);
        intent.putExtra("subject_id", subject.getId());
        getActivity().startActivity(intent);
    }
}
