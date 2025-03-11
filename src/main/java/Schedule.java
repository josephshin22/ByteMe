import java.util.ArrayList;

public class Schedule {
    String name;
    ArrayList<Course> courses;
    int scheduleID;
    int studentID;

    public Schedule(int studentID, ArrayList<Course> courses, String name) {
        this.scheduleID = createScheduleID();
        this.studentID = studentID;
        this.courses = courses;
        this.name = name;
    }

    public Schedule(int studentID) {
        this.scheduleID = createScheduleID();
        this.studentID = studentID;
        this.courses = new ArrayList<Course>();
        this.name = "";
    }

    private int createScheduleID() {
        return (int) (System.currentTimeMillis() % Integer.MAX_VALUE);
    }

    public void addToSchedule(int studentID, int scheduleID, Course c1) {
        Student s = new Student();
        Schedule sched = s.schedules[scheduleID];
        sched.add(c1);
    }

    public void removefromSchedule(int studentID, int scheduleID, Course c1) {
        Student s = new Student();
        Schedule sched = s.schedules[scheduleID];
        sched.courses.remove(c1);
    }

    public void printSchedule() {
        // print class names and timeslots in order of appearance monday-friday
    }

    public void saveSchedule() {}

}
