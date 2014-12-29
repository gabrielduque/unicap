package com.thm.unicap.app.grade;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.activeandroid.query.Select;
import com.thm.unicap.app.R;
import com.thm.unicap.app.model.Subject;
import com.thm.unicap.app.subject.SubjectListItemCard;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class GradesActivity extends ActionBarActivity {

    private Subject mSubject;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void initTaskDescriptionConfig() {
        int color = getResources().getColor(R.color.unicap_base_dark);
        setTaskDescription(new ActivityManager.TaskDescription(null, null, color));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grades);

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

        SubjectSituationCard card_situation = (SubjectSituationCard) findViewById(R.id.card_situation);
        card_situation.setSubject(mSubject);

        SubjectGradesCard card_grades = (SubjectGradesCard) findViewById(R.id.card_grades);
        card_grades.setSubject(mSubject);

        SubjectGradesProgressCard card_grades_progress = (SubjectGradesProgressCard) findViewById(R.id.card_grades_progress);
        card_grades_progress.setSubject(mSubject);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_grades, menu);
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

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(new CalligraphyContextWrapper(newBase));
    }
}
