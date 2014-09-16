package com.thm.unicap.app.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

@Table(name = "SubjectStatus")
public class SubjectStatus extends Model {

    public enum Situation {
        APPROVED, REPROVED, IMPORTED, PERFORMED, ACTUAL, PENDING, UNKNOWN
    }

    public enum FlowSituation {
        APPROVED, REPROVED, WAITING, WAITING_FINAL
    }

    public enum ScheduleWeekDay {
        Sun, Mon, Tue, Wed, Thu, Fri, Sat
    }

    @Column(name = "Subject", onDelete = Column.ForeignKeyAction.CASCADE)
    public Subject subject;

    @Column(name = "Situation")
    public Situation situation;

    @Column(name = "FlowSituation")
    public FlowSituation flowSituation;

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

    @Column(name = "FinalAverage")
    public Float final_average;

    public HashMap<ScheduleWeekDay, char[]> getFullSchedule() {
        if(schedule == null || schedule.isEmpty()) return null;

        HashMap<ScheduleWeekDay, char[]> result = new HashMap<ScheduleWeekDay, char[]>();

        String[] rawSchedules = schedule.trim().split(" ");

        for (String rawSchedule : rawSchedules) {
            String rawWeekDay = rawSchedule.substring(0, 1);
            String rawHour = rawSchedule.substring(1);

            ScheduleWeekDay weekDay = null;
            char[] hoursArray = rawHour.toUpperCase().toCharArray();

            if(rawWeekDay.equals("1")) weekDay = ScheduleWeekDay.Sun;
            else if(rawWeekDay.equals("2")) weekDay = ScheduleWeekDay.Mon;
            else if(rawWeekDay.equals("3")) weekDay = ScheduleWeekDay.Tue;
            else if(rawWeekDay.equals("4")) weekDay = ScheduleWeekDay.Wed;
            else if(rawWeekDay.equals("5")) weekDay = ScheduleWeekDay.Thu;
            else if(rawWeekDay.equals("6")) weekDay = ScheduleWeekDay.Fri;
            else if(rawWeekDay.equals("7")) weekDay = ScheduleWeekDay.Sat;

            if(weekDay != null && hoursArray.length > 0)
                result.put(weekDay, hoursArray);
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

    public FlowSituation getFlowSituation() {
        return flowSituation;
    }

    public FlowSituation calculateFlowSituation() {

        Float averageGrade = subject.getActualSubjectStatus().average;
        Float finalAverageGrade = subject.getActualSubjectStatus().final_average;

        if(averageGrade == null && finalAverageGrade == null) {
            return FlowSituation.WAITING;
        } else if(averageGrade == null && finalAverageGrade != null) {
            if(finalAverageGrade >= SubjectTest.MIN_AVERAGE)
                return FlowSituation.APPROVED;
            else
                return FlowSituation.REPROVED;
        } else if(averageGrade != null && finalAverageGrade == null) {
            if(averageGrade >= SubjectTest.MIN_AVERAGE)
                return FlowSituation.APPROVED;
            else
                return FlowSituation.WAITING_FINAL;
        } else if(averageGrade != null && finalAverageGrade != null) {
            if(finalAverageGrade >= SubjectTest.MIN_AVERAGE)
                return FlowSituation.APPROVED;
            else
                return FlowSituation.REPROVED;
        }

        return FlowSituation.WAITING;
    }
}
