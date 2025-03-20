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
    private ArrayList<Course> savedCourses;
    public Student() {
        this.schedules = new ArrayList<Schedule>();
    }

    public Student(String username, int studentID) {
        this.username = username;
        this.studentID = studentID;
        this.schedules = new ArrayList<Schedule>();
    }

    public void showSavedCourses() {
        if (savedCourses == null || savedCourses.isEmpty()) {
            System.out.println("No saved courses.");
            return;
        }

        System.out.println("Saved Courses:");
        for (Course course : savedCourses) {
            course.showCourse();
        }

        Scanner scanner = new Scanner(System.in);
        boolean done = false;
        while (!done) {
            System.out.println("Here is your saved courses page.");
            System.out.println("Type 'exit' to return to the main menu.");
            System.out.println("Type 'remove' to remove a course from your saved courses.");
            System.out.println("Type 'add' to add a course to one of your schedules.");
            String input = scanner.nextLine();

            switch (input.toLowerCase()) {
                case "exit":
                    done = true;
                    break;
                case "remove":
                    removeCourse(scanner);
                    break;
                case "add":
                    addCourse(scanner);
                    break;
                default:
                    System.out.println("Invalid input. Please try again.");
                    break;
            }
        }
    }

    private void removeCourse(Scanner scanner) {
        System.out.println("Enter course subj, number, then section to remove (example: HUMA 200 D):");
        String[] parts = scanner.nextLine().split(" ");
        if (parts.length == 3) {
            String subj = parts[0];
            int courseNum = Integer.parseInt(parts[1]);
            String section = parts[2];
            Course toRemove = findCourse(subj, courseNum, section);
            if (toRemove != null) {
                savedCourses.remove(toRemove);
                System.out.println("Course removed from saved courses.");
            } else {
                System.out.println("Course not found in saved courses.");
            }
        } else {
            System.out.println("Invalid input. Please enter in the format: SUBJ NUMBER SECTION");
        }
    }

    private void addCourse(Scanner scanner) {
        System.out.println("Enter course subj, number, then section to add to a schedule (example: HUMA 200 D):");
        String[] parts = scanner.nextLine().split(" ");
        if (parts.length == 3) {
            String subj = parts[0];
            int courseNum = Integer.parseInt(parts[1]);
            String section = parts[2];
            Course toAdd = findCourse(subj, courseNum, section);
            if (toAdd != null) {
                System.out.println("Schedule List");
                for (Schedule schedule : schedules) {
                    System.out.println(schedule);
                }
                System.out.println("Successfully selected course to add to a schedule.");
                System.out.println("Now enter the schedule ID of a schedule to have a course added to it");
                int scheduleID = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                Schedule schedule = findSchedule(scheduleID);
                if (schedule != null) {
                    schedule.addToSchedule(this, scheduleID, toAdd);
                    System.out.println("Course added to schedule.");
                } else {
                    System.out.println("Schedule not found. Please enter a valid schedule ID.");
                }
            } else {
                System.out.println("Course not found in saved courses.");
            }
        } else {
            System.out.println("Invalid input. Please enter in the format: SUBJ NUMBER SECTION");
        }
    }

    private Course findCourse(String subj, int courseNum, String section) {
        for (Course course : savedCourses) {
            if (course.getSubjCode().equalsIgnoreCase(subj) &&
                    course.getCourseNum() == courseNum &&
                    course.getSection().equalsIgnoreCase(section)) {
                return course;
            }
        }
        return null;
    }

    private Schedule findSchedule(int scheduleID) {
        for (Schedule schedule : schedules) {
            if (schedule.getScheduleID() == scheduleID) {
                return schedule;
            }
        }
        return null;
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

