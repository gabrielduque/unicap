package com.thm.unicap.app.dashboard;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.thm.unicap.app.R;
import com.thm.unicap.app.UnicapApplication;
import com.thm.unicap.app.model.Student;

import it.gmariotti.cardslib.library.internal.Card;

/**
 * Created by caoj on 10/12/14.
 */
public class CoefficientCard extends Card {

    public CoefficientCard(Context context) { super(context, R.layout.card_coefficient); }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {

        TextView course_coefficient = (TextView) parent.findViewById(R.id.course_coefficient);
        TextView last_coefficient = (TextView) parent.findViewById(R.id.last_coefficient);

        if (checkCoefficientStatus()) {
            course_coefficient.setText(UnicapApplication.getCurrentStudent().courseCoefficient.toString());
            last_coefficient.setText(UnicapApplication.getCurrentStudent().lastCoefficient.toString());
        }
        else {
            course_coefficient.setText("-");
            last_coefficient.setText("-");
        }

    }

    private boolean checkCoefficientStatus () {

        if ( UnicapApplication.getCurrentStudent().courseCoefficient < 0 )
            return false;
        else if ( UnicapApplication.getCurrentStudent().courseCoefficient > 10 )
            return false;
        else if ( UnicapApplication.getCurrentStudent().courseCoefficient == null)
            return false;
        else if ( UnicapApplication.getCurrentStudent().lastCoefficient < 0 )
            return false;
        else if ( UnicapApplication.getCurrentStudent().lastCoefficient > 10 )
            return false;
        else if ( UnicapApplication.getCurrentStudent().lastCoefficient == null)
            return false;
        else
            return true;
    }

}
