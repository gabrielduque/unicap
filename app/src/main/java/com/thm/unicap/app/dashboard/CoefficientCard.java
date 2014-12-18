package com.thm.unicap.app.dashboard;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.thm.unicap.app.R;
import com.thm.unicap.app.UnicapApplication;


/**
 * Created by caoj on 10/12/14.
 */
public class CoefficientCard extends CardView {

    public CoefficientCard(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        View.inflate(getContext(), R.layout.card_coefficient, this);
    }

    private void initData() {

        TextView course_coefficient = (TextView) findViewById(R.id.course_coefficient);
        TextView last_coefficient = (TextView) findViewById(R.id.last_coefficient);

        course_coefficient.setText(UnicapApplication.getCurrentStudent().courseCoefficient.toString());
        last_coefficient.setText(UnicapApplication.getCurrentStudent().lastCoefficient.toString());

    }

    public void setCoefficient() {
        initData();
    }

}
