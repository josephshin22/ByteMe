import java.util.ArrayList;

public class Student {
    String username;
    int studentID;
    String password;
    String[] major;
    String[] minor;
    String gradYear;
    String gradMonth;
    Course[] takenCourses;
    ArrayList<Schedule> schedules;

    public Student() {
        this.schedules = new ArrayList<>();
    }

    public void addSchedule(Schedule schedule) {
        schedules.add(schedule);
    }

    public ArrayList<Schedule> getSchedules() {
        return schedules;
    }

    public void removeSchedule(Schedule schedule) {
        schedules.remove(schedule);
    }
}