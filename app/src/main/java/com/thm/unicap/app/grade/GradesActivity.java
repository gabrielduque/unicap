package com.thm.unicap.app.grade;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.activeandroid.query.Select;
import com.thm.unicap.app.R;
import com.thm.unicap.app.model.Subject;
import com.thm.unicap.app.subject.SubjectHistoryCard;
import com.thm.unicap.app.subject.SubjectInfoCard;
import com.thm.unicap.app.subject.SubjectListItemCard;
import com.thm.unicap.app.subject.SubjectScheduleCard;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.view.CardView;

public class GradesActivity extends ActionBarActivity {

    private Subject mSubject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grades);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        long subject_id = getIntent().getLongExtra("subject_id", -1);

        if(subject_id != -1) {
            mSubject = new Select().from(Subject.class).where("Subject.Id = ?", subject_id).executeSingle();
            init();
        }

    }

    private void init() {
        setTitle(mSubject.name);

        CardView card_list_item = (CardView) findViewById(R.id.card_list_item);
        SubjectListItemCard subjectListItemCard = new SubjectListItemCard(this, mSubject);
        card_list_item.setCard(subjectListItemCard);

        CardView card_schedule = (CardView) findViewById(R.id.card_grades);
        SubjectGradesCard subjectGradesCard = new SubjectGradesCard(this, mSubject);
        card_schedule.setCard(subjectGradesCard);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.grades, menu);
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
