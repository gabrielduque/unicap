package com.thm.unicap.app.dashboard;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import com.echo.holographlibrary.PieGraph;
import com.echo.holographlibrary.PieSlice;
import com.echo.holographlibrary.Utils;
import com.thm.unicap.app.R;
import com.thm.unicap.app.UnicapApplication;
import com.thm.unicap.app.model.Student;
import com.thm.unicap.app.model.Subject;
import com.thm.unicap.app.model.SubjectStatus;

import java.util.List;

import it.gmariotti.cardslib.library.internal.Card;

public class TodayLessonsCard extends Card {

    public TodayLessonsCard(Context context) {
        super(context, R.layout.card_today_lessons);
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {

//        Student student = UnicapApplication.getStudent();
//
//        List<Subject> subjects = student.getActualSubjects();

    }

}
