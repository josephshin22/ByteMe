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

    public void showSavedCourses() {
        if (savedCourses != null && !savedCourses.isEmpty()) {
            System.out.println("Saved Courses:");
            for (Course course : savedCourses) {
                course.showCourse();
            }
            boolean done = false;
            System.out.println("Here is your saved courses page.");
//            System.out.println("Type 'exit' to return to the main menu.");
//            System.out.println("Type 'remove' to remove a course from your saved courses.");
//            System.out.println("Type 'add' to add a course to one of your schedules.");
            while(done)
            {
                System.out.println("Type 'exit' to return to the main menu.");
                System.out.println("Type 'remove' to remove a course from your saved courses.");
                System.out.println("Type 'add' to add a course to one of your schedules.");
                Scanner scanner = new Scanner(System.in);

                String input = scanner.nextLine();
                if (input.equalsIgnoreCase("exit")) {
                    done = true;
                } else if (input.equalsIgnoreCase("remove")) {
                    System.out.println("Enter course subj, number, then section to remove (example: HUMA 200 D):");
                    String courseLabel = scanner.nextLine();
                    String[] parts = courseLabel.split(" ");
                    if (parts.length == 3) {
                        String subj = parts[0];
                        int courseNum = Integer.parseInt(parts[1]);
                        String section = parts[2];
=======


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
