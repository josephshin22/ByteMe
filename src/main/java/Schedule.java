import java.util.ArrayList;

public class Schedule {
    String name;
    Course[] courses;
    int scheduleID;
    int studentID;

    public Schedule(int scheduleID, int studentID, Course[] courses, String name) {
        this.scheduleID = scheduleID;
        this.studentID = studentID;
        this.courses = courses;
        this.name = name;
    }

    public void addToSchedule(int referenceNumber, int studentID){

    }

    public void removeFromSchedule(int referenceNumber, int studentID){

    }


}
