package com.example.diuteacher.PoJo;

public class ClassPoJo {

    public String id;
    public String subjectName;
    public String teacherName;
    public String startingTime;
    public String endingTime;

    public ClassPoJo(String id, String subjectName, String teacherName, String startingTime, String endingTime) {
        this.id = id;
        this.subjectName = subjectName;
        this.teacherName = teacherName;
        this.startingTime = startingTime;
        this.endingTime = endingTime;
    }

    public ClassPoJo() {
    }

    public String getId() {
        return id;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public String getStartingTime() {
        return startingTime;
    }

    public String getEndingTime() {
        return endingTime;
    }
}
