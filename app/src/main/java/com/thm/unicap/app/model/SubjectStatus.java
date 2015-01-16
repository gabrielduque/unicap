package com.thm.unicap.app.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.ArrayList;
import java.util.HashMap;

@Table(name = "SubjectStatus")
public class SubjectStatus extends Model {

    public enum Situation {
        APPROVED, REPROVED, IMPORTED, PERFORMED, ACTUAL, PENDING, DISPENSED, UNKNOWN
    }

    public enum FlowSituation {
        APPROVED, REPROVED, WAITING, WAITING_FINAL
    }

    public enum ScheduleWeekday {
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

    public HashMap<ScheduleWeekday, char[]> getFullSchedule() {
        if (schedule == null || schedule.isEmpty()) return null;

        HashMap<ScheduleWeekday, char[]> result = new HashMap<ScheduleWeekday, char[]>();

        String[] rawSchedules = schedule.trim().split(" ");

        for (String rawSchedule : rawSchedules) {
            String rawWeekDay = rawSchedule.substring(0, 1);
            String rawHour = rawSchedule.substring(1);

            ScheduleWeekday weekDay = null;
            char[] hoursArray = rawHour.toUpperCase().toCharArray();

            if (rawWeekDay.equals("1")) weekDay = ScheduleWeekday.Sun;
            else if (rawWeekDay.equals("2")) weekDay = ScheduleWeekday.Mon;
            else if (rawWeekDay.equals("3")) weekDay = ScheduleWeekday.Tue;
            else if (rawWeekDay.equals("4")) weekDay = ScheduleWeekday.Wed;
            else if (rawWeekDay.equals("5")) weekDay = ScheduleWeekday.Thu;
            else if (rawWeekDay.equals("6")) weekDay = ScheduleWeekday.Fri;
            else if (rawWeekDay.equals("7")) weekDay = ScheduleWeekday.Sat;

            if (weekDay != null && hoursArray.length > 0)
                result.put(weekDay, hoursArray);
        }

        return result;
    }

    public ArrayList<ScheduleWeekday> getWeekDays() {
        if (schedule == null || schedule.isEmpty()) return null;

        ArrayList<ScheduleWeekday> result = new ArrayList<ScheduleWeekday>();

        if (schedule.contains("2")) result.add(ScheduleWeekday.Mon);
        if (schedule.contains("3")) result.add(ScheduleWeekday.Tue);
        if (schedule.contains("4")) result.add(ScheduleWeekday.Wed);
        if (schedule.contains("5")) result.add(ScheduleWeekday.Thu);
        if (schedule.contains("6")) result.add(ScheduleWeekday.Fri);
        if (schedule.contains("7")) result.add(ScheduleWeekday.Sat);

        return result;
    }

    public FlowSituation getFlowSituation() {
        return flowSituation;
    }

    public FlowSituation calculateFlowSituation() {

        Float averageGrade = subject.getActualSubjectStatus().average;
        Float finalAverageGrade = subject.getActualSubjectStatus().final_average;

        if (averageGrade == null && finalAverageGrade == null) {
            return FlowSituation.WAITING;
        } else if (averageGrade == null && finalAverageGrade != null) {
            if (finalAverageGrade >= SubjectTest.MIN_AVERAGE)
                return FlowSituation.APPROVED;
            else
                return FlowSituation.REPROVED;
        } else if (averageGrade != null && finalAverageGrade == null) {
            if (averageGrade >= SubjectTest.MIN_AVERAGE)
                return FlowSituation.APPROVED;
            else
                return FlowSituation.WAITING_FINAL;
        } else if (averageGrade != null && finalAverageGrade != null) {
            if (finalAverageGrade >= SubjectTest.MIN_AVERAGE)
                return FlowSituation.APPROVED;
            else
                return FlowSituation.REPROVED;
        }

        return FlowSituation.WAITING;
    }
}
