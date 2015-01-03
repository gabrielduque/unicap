package com.thm.unicap.app.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.thm.unicap.app.R;
import com.thm.unicap.app.UnicapApplication;

import java.util.List;

@Table(name = "Subject")
public class Subject extends Model {

    @Column(name = "Student", onDelete = Column.ForeignKeyAction.CASCADE)
    public Student student;

    @Column(name = "Code")
    public String code;

    @Column(name = "Name")
    public String name;

    @Column(name = "Workload")
    public Integer workload;

    @Column(name = "Credits")
    public Integer credits;

    @Column(name = "Period")
    public Integer period;

    public List<SubjectStatus> getSubjectStatus() {
        return getMany(SubjectStatus.class, "Subject");
    }

    public SubjectStatus getActualSubjectStatus() {
        List<SubjectStatus> subjectStatusList = getMany(SubjectStatus.class, "Subject");

        for (SubjectStatus subjectStatus : subjectStatusList) {
            if (subjectStatus.situation == SubjectStatus.Situation.ACTUAL)
                return subjectStatus;
        }

        return null;
    }

    public boolean isActual() {
        return getActualSubjectStatus() != null;
    }

    public boolean hasHistory() {
        List<SubjectStatus> subjectStatusList = getSubjectStatus();

        for (SubjectStatus subjectStatus : subjectStatusList) {
            if (subjectStatus.situation != SubjectStatus.Situation.PENDING)
                return true;
        }

        return false;
    }

    public List<SubjectTest> getSubjectTests() {
        return getMany(SubjectTest.class, "Subject");
    }

    public SubjectTest getTestByDegree(SubjectTest.Degree degree) {
        List<SubjectTest> subjectTests = getMany(SubjectTest.class, "Subject");

        for (SubjectTest subjectTest : subjectTests) {
            if (subjectTest.degree == degree) return subjectTest;
        }

        return null;
    }

    //TODO: Consider making this property static on sync progress
    public String getNameAbbreviation() {
        String abbreviation = "";
        String filteredName = removeExceptionsFromName(name);
        String[] words = filteredName.trim().split(" +");

        for (String word : words) {
            if(word.length() == 0) break;
            abbreviation += word.charAt(0);
            if(abbreviation.length() >= 3) break;
        }

        return abbreviation.toUpperCase();
    }

    public int getColorResource() {

        int color = R.color.unicap_base;

        int colors[] = {
                R.color.subject_color_1,
                R.color.subject_color_2,
                R.color.subject_color_3,
                R.color.subject_color_4,
                R.color.subject_color_5,
                R.color.subject_color_6,
                R.color.subject_color_7,
        };

        if(period != null)
            color = colors[period % colors.length];

        return color;
    }

    public int getColorCircleResource() {

        int colorCircle = R.drawable.subject_color_circle_unicap;

        int colorsCircles[] = {
                R.drawable.subject_color_circle_1,
                R.drawable.subject_color_circle_2,
                R.drawable.subject_color_circle_3,
                R.drawable.subject_color_circle_4,
                R.drawable.subject_color_circle_5,
                R.drawable.subject_color_circle_6,
                R.drawable.subject_color_circle_7,
        };

        if(period != null)
            colorCircle = colorsCircles[period % colorsCircles.length];

        return colorCircle;
    }

    //Todo: refactor this
    public static String removeExceptionsFromName (String str) {
        String[] exceptions = {
                "da",
                "de",
                "do",
                "à",
                "e",
                "a",
                "ao",
                "i",
                "ii",
                "iii",
                "iv",
                "v",
                "vi",
                "vii",
        };

        for (String exception : exceptions) {
            str = str.replaceAll("(?i)\\b"+exception+"\\b", "");
        }
        return str;
    }

}
