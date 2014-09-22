package com.thm.unicap.app.subject;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.activeandroid.query.Select;
import com.thm.unicap.app.R;
import com.thm.unicap.app.model.Subject;

import it.gmariotti.cardslib.library.view.CardView;

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

            if (mSubject != null) {
                init();
            } else {
                finish();
            }
        }

    }

    private void init() {
        setTitle(mSubject.name);

        CardView card_list_item = (CardView) findViewById(R.id.card_list_item);
        SubjectListItemCard subjectListItemCard = new SubjectListItemCard(this, mSubject);
        card_list_item.setCard(subjectListItemCard);

        if(mSubject.isActual()) {
            CardView card_schedule = (CardView) findViewById(R.id.card_schedule);
            SubjectScheduleCard subjectScheduleCard = new SubjectScheduleCard(this, mSubject);
            card_schedule.setCard(subjectScheduleCard);
            card_schedule.setVisibility(View.VISIBLE);
        }

        if(mSubject.hasHistory()) {
            CardView card_history = (CardView) findViewById(R.id.card_history);
            SubjectHistoryCard subjectHistoryCard = new SubjectHistoryCard(this, mSubject);
            card_history.setCard(subjectHistoryCard);
            card_history.setVisibility(View.VISIBLE);
        }

        CardView card_info = (CardView) findViewById(R.id.card_info);
        SubjectInfoCard subjectInfoCard = new SubjectInfoCard(this, mSubject);
        card_info.setCard(subjectInfoCard);
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
