import java.util.ArrayList;

public class Schedule {
    String name;
    Course[] courses;
    int scheduleID;
    int studentID;
    ArrayList<Course> gccCourses;
    String semester;

    public Schedule(int scheduleID, int studentID, Course[] courses, String name, String semester) {
        this.scheduleID = scheduleID;
        this.studentID = studentID;
        this.courses = courses;
        this.name = name;
        this.semester = semester;
    }
}
