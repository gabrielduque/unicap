package com.thm.unicap.app.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.thm.unicap.app.util.HashUtils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Table(name = "Students")
public class Student extends Model {

    @Column(name = "Registration", unique = true)
    public String registration;

    @Column(name = "Name")
    public String name;

    @Column(name = "Course")
    public String course;

    @Column(name = "Shift")
    public String shift;

    @Column(name = "Gender")
    public String gender;

    @Column(name = "Birthday")
    public String birthday;

    @Column(name = "Email")
    public String email;

    @Column(name = "Password")
    public String password;

    public List<Subject> getSubjects() {
        return getMany(Subject.class, "Student");
    }

    public String getGravatarURL(Integer pictureSize) {

        String emailAddress = email != null ? email : "";
        String fallbackImage = "mm";

        String md5Hash = HashUtils.MD5(emailAddress);

        if(md5Hash == null) md5Hash = "";

        return String.format("http://www.gravatar.com/avatar/%s?s=%s&d=%s", md5Hash, String.valueOf(pictureSize), fallbackImage);
    }
}
