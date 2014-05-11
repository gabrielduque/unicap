package com.thm.unicap.app.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "Subjects")
public class Subject extends Model {

    @Column(name = "Code")
    public String code;

    @Column(name = "Name")
    public String name;

    @Column(name = "Team")
    public String team;

    @Column(name = "Room")
    public String room;

    @Column(name = "Schedule")
    public String schedule;

    @Column(name = "Workload")
    public String workload;

    @Column(name = "Credits")
    public String credits;

    @Column(name = "Period")
    public String period;

    @Override
    public String toString() {
        return "Subject{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", team='" + team + '\'' +
                ", room='" + room + '\'' +
                ", schedule='" + schedule + '\'' +
                ", workload='" + workload + '\'' +
                ", credits='" + credits + '\'' +
                ", period='" + period + '\'' +
                '}';
    }
}
