package com.thm.unicap.app.grade;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.activeandroid.query.Select;
import com.thm.unicap.app.R;
import com.thm.unicap.app.model.Subject;
import com.thm.unicap.app.subject.SubjectListItemCard;

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

        CardView card_situation = (CardView) findViewById(R.id.card_situation);
        SubjectSituationCard subjectSituationCard = new SubjectSituationCard(this, mSubject);
        card_situation.setCard(subjectSituationCard);

        CardView card_grades = (CardView) findViewById(R.id.card_grades);
        SubjectGradesCard subjectGradesCard = new SubjectGradesCard(this, mSubject);
        card_grades.setCard(subjectGradesCard);

        CardView card_grades_progress = (CardView) findViewById(R.id.card_grades_progress);
        SubjectGradesProgressCard subjectGradesProgressCard = new SubjectGradesProgressCard(this, mSubject);
        card_grades_progress.setCard(subjectGradesProgressCard);
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
}
