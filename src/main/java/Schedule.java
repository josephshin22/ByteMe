import java.util.*;

public class Schedule {
    String name;
    List<Course> courses;
    int scheduleID;
    int studentID;

    public Schedule(Student s, List<Course> courses, String name) {
        this.scheduleID = createScheduleID();
        this.studentID = s.studentID;
        this.courses = courses;
        this.name = name;
        for (int i = 0; i < s.schedules.size(); i++) {
            if (s.schedules.get(i) == null) {
                s.schedules.set(i, this);
                break;
            }
        }
    }

    public Schedule(Student s) {
        this.scheduleID = createScheduleID();
        this.studentID = s.studentID;
        this.courses = new ArrayList<Course>();
        this.name = "";
        for (int i = 0; i < s.schedules.size(); i++) {
            if (s.schedules.get(i) == null) {
                s.schedules.set(i, this);
                break;
            }
        }
    }

    private int createScheduleID() {
        return (int) (Math.random() * 900000) + 100000;
    }

    public void addToSchedule(Student s, int scheduleID, Course c1) {
        for (int i = 0; i < s.schedules.size(); i++) {
            if (s.schedules.get(i) != null && s.schedules.get(i).scheduleID == scheduleID) {
                Schedule sched = s.schedules.get(i);
                sched.courses.add(c1);
                break;
            }
        }
    }

    public void removefromSchedule(Student s, int scheduleID, Course c1) {
        for (int i = 0; i < s.schedules.size(); i++) {
            if(s.schedules.get(i) != null && s.schedules.get(i).scheduleID == scheduleID) {
                Schedule sched = s.schedules.get(i);
                sched.courses.remove(c1);
                break;
            }
        }
    }

    @Override
    public String toString() {
        return "Schedule Name: " + name + "\nSchedule ID: " + scheduleID + "\n" + "Student ID: " + studentID + "\n" +
                "Courses: " + courses + "\n";
    }
}
