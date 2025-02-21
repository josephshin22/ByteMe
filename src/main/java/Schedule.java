import java.util.ArrayList;

public class Schedule {
    String name;
    Course[] courses;
    int scheduleID;
    int studentID;
    ArrayList<Course> gccCourses;

    public Schedule(int scheduleID, int studentID, Course[] courses, String name) {
        this.scheduleID = scheduleID;
        this.studentID = studentID;
        this.courses = courses;
        this.name = name;
    }
}
