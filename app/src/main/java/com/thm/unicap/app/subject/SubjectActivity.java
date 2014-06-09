package com.thm.unicap.app.subject;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.activeandroid.query.Select;
import com.thm.unicap.app.R;
import com.thm.unicap.app.model.Subject;

public class SubjectActivity extends ActionBarActivity {

    private Subject mSubject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        long subject_id = getIntent().getLongExtra("subject_id", -1);

        if(subject_id != -1) {
            mSubject = new Select().from(Subject.class).where("Subject.Id = ?", subject_id).executeSingle();
            setTitle(mSubject.name);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.subject, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
