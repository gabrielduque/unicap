package com.thm.unicap.app.subject;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.activeandroid.query.Select;
import com.thm.unicap.app.R;
import com.thm.unicap.app.model.Subject;

public class SubjectActivity extends ActionBarActivity {

    private Subject mSubject;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void initTaskDescriptionConfig() {
        int color = getResources().getColor(R.color.unicap_base_dark);
        setTaskDescription(new ActivityManager.TaskDescription(null, null, color));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_actionbar);
        setSupportActionBar(toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            initTaskDescriptionConfig();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        long subject_id = getIntent().getLongExtra("subject_id", -1);

        if(subject_id != -1) {

            mSubject = new Select().from(Subject.class).where("Subject.Id = ?", subject_id).executeSingle();

            if (mSubject != null) {
                init();
            } else {
                finish();
            }
        }

    }

    private void init() {
        setTitle(mSubject.name);

        SubjectListItemCard card_list_item = (SubjectListItemCard) findViewById(R.id.card_list_item);
        card_list_item.setSubject(mSubject);

        if(mSubject.isActual()) {
            SubjectScheduleCard subjectScheduleCard = (SubjectScheduleCard) findViewById(R.id.card_schedule);
            subjectScheduleCard.setSubject(mSubject);
            subjectScheduleCard.setVisibility(View.VISIBLE);
        }

        if(mSubject.hasHistory()) {
            SubjectHistoryCard card_history = (SubjectHistoryCard) findViewById(R.id.card_history);
            card_history.setSubject(mSubject);
            card_history.setVisibility(View.VISIBLE);
        }

        SubjectInfoCard subjectInfoCard = (SubjectInfoCard) findViewById(R.id.card_info);
        subjectInfoCard.setSubject(mSubject);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_subject, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
