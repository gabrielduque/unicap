package com.thm.unicap.app.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.ArrayList;
import java.util.HashMap;

@Table(name = "SubjectStatus")
public class SubjectStatus extends Model {

    public enum Situation {
        APPROVED, REPROVED, ACTUAL, PENDING, UNKNOWN
    }

    public enum ScheduleWeekDay {
        Mon, Tue, Wed, Thu, Fri, Sat
    }

    public enum ScheduleHour {
        AB, CD, EF, GH, IJ, LM, NO, PQ
    }

    @Column(name = "Subject", onDelete = Column.ForeignKeyAction.CASCADE)
    public Subject subject;

    @Column(name = "Situation")
    public Situation situation;

    @Column(name = "Team")
    public String team;

    @Column(name = "Room")
    public String room;

    @Column(name = "Schedule")
    public String schedule;

    @Column(name = "PaidIn")
    public String paidIn;

    @Column(name = "Average")
    public Float average;

    public HashMap<ScheduleWeekDay, ScheduleHour> getFullSchedule() {
        if(schedule == null || schedule.isEmpty()) return null;

        HashMap<ScheduleWeekDay, ScheduleHour> result = new HashMap<ScheduleWeekDay, ScheduleHour>();

        String[] rawSchedules = schedule.trim().split(" ");

        for (String rawSchedule : rawSchedules) {
            String rawWeekDay = rawSchedule.substring(0, 1);
            String rawHour = rawSchedule.substring(1);

            ScheduleWeekDay weekDay = null;
            ScheduleHour hour = null;

            if(rawWeekDay.equals("2")) weekDay = ScheduleWeekDay.Mon;
            else if(rawWeekDay.equals("3")) weekDay = ScheduleWeekDay.Tue;
            else if(rawWeekDay.equals("4")) weekDay = ScheduleWeekDay.Wed;
            else if(rawWeekDay.equals("5")) weekDay = ScheduleWeekDay.Thu;
            else if(rawWeekDay.equals("6")) weekDay = ScheduleWeekDay.Fri;
            else if(rawWeekDay.equals("7")) weekDay = ScheduleWeekDay.Sat;

            if(rawHour.equals("AB")) hour = ScheduleHour.AB;
            else if(rawHour.equals("CD")) hour = ScheduleHour.CD;
            else if(rawHour.equals("EF")) hour = ScheduleHour.EF;
            else if(rawHour.equals("GH")) hour = ScheduleHour.GH;
            else if(rawHour.equals("IJ")) hour = ScheduleHour.IJ;
            else if(rawHour.equals("LM")) hour = ScheduleHour.LM;
            else if(rawHour.equals("NO")) hour = ScheduleHour.NO;
            else if(rawHour.equals("PQ")) hour = ScheduleHour.PQ;

            if(weekDay != null && hour != null)
                result.put(weekDay, hour);
        }

        return result;
    }

    public ArrayList<ScheduleWeekDay> getWeekDays() {
        if(schedule == null || schedule.isEmpty()) return null;

        ArrayList<ScheduleWeekDay> result = new ArrayList<ScheduleWeekDay>();

        if(schedule.contains("2")) result.add(ScheduleWeekDay.Mon);
        if(schedule.contains("3")) result.add(ScheduleWeekDay.Tue);
        if(schedule.contains("4")) result.add(ScheduleWeekDay.Wed);
        if(schedule.contains("5")) result.add(ScheduleWeekDay.Thu);
        if(schedule.contains("6")) result.add(ScheduleWeekDay.Fri);
        if(schedule.contains("7")) result.add(ScheduleWeekDay.Sat);

        return result;
    }
}
