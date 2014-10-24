package com.thm.unicap.app.subject;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
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

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_actionbar);
        setSupportActionBar(toolbar);

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
            SubjectScheduleCard subjectScheduleCard = (SubjectScheduleCard) findViewById(R.id.card_schedule);
            subjectScheduleCard.setSubject(mSubject);
            subjectScheduleCard.setVisibility(View.VISIBLE);
        }

        if(mSubject.hasHistory()) {
            CardView card_history = (CardView) findViewById(R.id.card_history);
            SubjectHistoryCard subjectHistoryCard = new SubjectHistoryCard(this, mSubject);
            card_history.setCard(subjectHistoryCard);
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
