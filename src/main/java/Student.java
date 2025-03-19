import java.util.*;

public class Student {

    private String username;
    private int studentID;
    private String password;
    private String[] major;
    private String[] minor;
    private String gradYear;
    private String gradMonth;
    private Course[] takenCourses;
    private ArrayList<Schedule> schedules;

    public Student() {
        this.schedules = new ArrayList<Schedule>();
    }

    public Student(String username, int studentID) {
        this.username = username;
        this.studentID = studentID;
        this.schedules = new ArrayList<Schedule>();
    }

    public boolean verifyPassword(String passwordAttempt) {
        return passwordAttempt.equals(password);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword(String password) {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getStudentID() {
        return studentID;
    }

    public ArrayList<Schedule> getSchedules() {
        return schedules;
    }

    public void setSchedule(Schedule schedule) {
        schedules.add(schedule);
    }
}
