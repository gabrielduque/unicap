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

    @Column(name = "Workload")
    public String workload;

    @Column(name = "Credits")
    public int credits;

    @Column(name = "Period")
    public int period;

    @Override
    public String toString() {
        return "Subject{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", workload='" + workload + '\'' +
                ", credits='" + credits + '\'' +
                ", period='" + period + '\'' +
                '}';
    }
}
