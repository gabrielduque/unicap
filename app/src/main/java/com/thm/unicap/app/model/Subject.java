package com.thm.unicap.app.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.thm.unicap.app.R;

import java.io.Serializable;
import java.util.List;
import java.util.Random;

@Table(name = "Subject")
public class Subject extends Model {

    @Column(name = "Student", onDelete = Column.ForeignKeyAction.CASCADE)
    public Student student;

    @Column(name = "Code", unique = true)
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
        //TODO: Find a good logic to make this
        String abbreviation = "";
        String filteredName = removeExceptionsFromName(name);
        String[] words = filteredName.split(" +");

        for (String word : words) {
            abbreviation += word.charAt(0);
            if(abbreviation.length() >= 3) break;
        }

        return abbreviation.toUpperCase();
    }

    public int getColorResource() {

        int color = R.color.unicap_base;

        int colors[] = {
                android.R.color.holo_blue_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_green_light,
                android.R.color.holo_red_light,
                android.R.color.holo_purple,
        };

        if(period != null)
            color = colors[period % colors.length];

        return color;
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
