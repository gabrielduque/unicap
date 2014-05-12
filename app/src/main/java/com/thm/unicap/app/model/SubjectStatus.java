package com.thm.unicap.app.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "SubjectStatus")
public class SubjectStatus extends Model {

    public enum Situation {
        PAST, ACTUAL, FUTURE
    }

    @Column(name = "Subject")
    public Subject subject;

    @Column(name = "Situation")
    public Situation situation;

    @Column(name = "Team")
    public String team;

    @Column(name = "Room")
    public String room;

    @Column(name = "Schedule")
    public String schedule;

    @Override
    public String toString() {
        return "SubjectStatus{" +
                "situation=" + situation +
                ", team='" + team + '\'' +
                ", room='" + room + '\'' +
                ", schedule='" + schedule + '\'' +
                '}';
    }
}
