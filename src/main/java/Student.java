public class Student {

    String username;
    int studentID;
    String password;
    String[] major;
    String[] minor;
    String gradYear;
    String gradMonth;
    Course[] takenCourses;
    Schedule[] schedules;
    Course[] savedCourses;

    //set up constructor for student class
    public Student() {

    }

    public void showSavedCourses() {


        if (savedCourses != null && savedCourses.length > 0) {
            System.out.println("Saved Courses:");
            for (Course course : savedCourses) {
                System.out.println(course.getName());
                System.out.println();
            }
        } else {
            System.out.println("No saved courses.");
        }
    }

    public Student(String username, int studentID, String password, String[] major, String[] minor, String gradYear, String gradMonth, Course[] takenCourses, Schedule[] schedules, Course[] savedCourses) {
        this.username = username;
        this.studentID = studentID;
        this.password = password;
        this.major = major;
        this.minor = minor;
        this.gradYear = gradYear;
        this.gradMonth = gradMonth;
        this.takenCourses = takenCourses;
        this.schedules = schedules;
    }
    //GETTERS

    public String getUsername() {
        return username;
    }

    public int getStudentID() {
        return studentID;
    }

    public String getPassword() {
        return password;
    }

    public String[] getMajor() {
        return major;
    }

    public String[] getMinor() {
        return minor;
    }

    public String getGradYear() {
        return gradYear;
    }

    public String getGradMonth() {
        return gradMonth;
    }

    public Course[] getTakenCourses() {
        return takenCourses;
    }

    public Schedule[] getSchedules() {
        return schedules;
    }

    public Course[] getSavedCourses() {
        return savedCourses;
    }


}
