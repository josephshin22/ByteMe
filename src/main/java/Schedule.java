import java.util.*;

public class Schedule {
    String name;
    List<Course> courses;
    int scheduleID;
    int studentID;

    public Schedule(Student s, List<Course> courses, String name) {
        this.scheduleID = createScheduleID();
        this.studentID = s.getStudentID();
        this.courses = courses;
        this.name = name;
        s.setSchedule(this);
    }

    public Schedule(Student s) {
        this.scheduleID = createScheduleID();
        this.studentID = s.getStudentID();
        this.courses = new ArrayList<Course>();
        this.name = "";
    }

    private int createScheduleID() {
        return (int) (Math.random() * 900000) + 100000;
    }

    public void addToSchedule(Student s, int scheduleID, Course c1) {
        for (int i = 0; i < s.getSchedules().size(); i++) {
            if (s.getSchedules().get(i) != null && s.getSchedules().get(i).scheduleID == scheduleID) {
                Schedule sched = s.getSchedules().get(i);
                sched.courses.add(c1);
                break;
            }
        }
    }

    public void removefromSchedule(Student s, int scheduleID, Course c1) {
        for (int i = 0; i < s.getSchedules().size(); i++) {
            if(s.getSchedules().get(i) != null && s.getSchedules().get(i).scheduleID == scheduleID) {
                Schedule sched = s.getSchedules().get(i);
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

    public String calendarView() {
        StringBuilder calendar = new StringBuilder();
        String[] days = {"M", "T", "W", "R", "F"};
        int startHour = 8; // Start time for the calendar
        int endHour = 18; // End time for the calendar

        // Print the header with days of the week
        calendar.append("Time  ");
        for (String day : days) {
            calendar.append(String.format("%-10s", day));
        }
        calendar.append("\n");

        // Print the time slots and courses
        for (int hour = startHour; hour <= endHour; hour++) {
            calendar.append(String.format("%02d:00 ", hour));
            for (String day : days) {
                boolean courseFound = false;
                for (Course course : courses) {
                    for (timeBlock time : course.getTimes()) {
                        if (time.getDay().equals(day) && Integer.parseInt(time.getStartTime().split(":")[0]) == hour) {
                            calendar.append(String.format("%-10s", course.getCourseNum() + "-" + course.getSection()));

                            courseFound = true;
                            break;
                        }
                    }
                    if (courseFound) break;
                }
                if (!courseFound) {
                    calendar.append(String.format("%-10s", ""));
                }
            }
            calendar.append("\n");
        }
        return calendar.toString();
    }

    public void addToSchedule(int referenceNumber, int studentID){

    }

    public void removeFromSchedule(int referenceNumber, int studentID){

    }


}
