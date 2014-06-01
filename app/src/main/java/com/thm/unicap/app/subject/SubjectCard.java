package com.thm.unicap.app.subject;

import android.content.Context;

import com.thm.unicap.app.model.Subject;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;

public class SubjectCard extends Card {

    private Subject mSubject;

    public SubjectCard(Context context, Subject subject) {
        super(context);
        mSubject = subject;
        SetupSubject();
    }

    public SubjectCard(Context context, int innerLayout, Subject subject) {
        super(context, innerLayout);
        mSubject = subject;
        SetupSubject();
    }

    private void SetupSubject() {

        CardHeader cardHeader = new CardHeader(mContext);
        cardHeader.setTitle(mSubject.name);
        addCardHeader(cardHeader);

        setTitle(mSubject.code);
    }
}
