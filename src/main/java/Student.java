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

    public Student(String username, int studentID, String password) {
        this.username = username;
        this.studentID = studentID;
        this.password = password;
        this.schedules = new ArrayList<Schedule>();
    }

//    public void showSavedCourses() {
//        if (savedCourses != null && !savedCourses.isEmpty()) {
//            System.out.println("Saved Courses:");
//            for (Course course : savedCourses) {
//                course.showCourse();
//            }
//            boolean done = false;
//            System.out.println("Here is your saved courses page.");
////            System.out.println("Type 'exit' to return to the main menu.");
////            System.out.println("Type 'remove' to remove a course from your saved courses.");
////            System.out.println("Type 'add' to add a course to one of your schedules.");
//            while(done)
//            {
//                System.out.println("Type 'exit' to return to the main menu.");
//                System.out.println("Type 'remove' to remove a course from your saved courses.");
//                System.out.println("Type 'add' to add a course to one of your schedules.");
//                Scanner scanner = new Scanner(System.in);
//
//                String input = scanner.nextLine();
//                if (input.equalsIgnoreCase("exit")) {
//                    done = true;
//                } else if (input.equalsIgnoreCase("remove")) {
//                    System.out.println("Enter course subj, number, then section to remove (example: HUMA 200 D):");
//                    String courseLabel = scanner.nextLine();
//                    String[] parts = courseLabel.split(" ");
//                    if (parts.length == 3) {
//                        String subj = parts[0];
//                        int courseNum = Integer.parseInt(parts[1]);
//                        String section = parts[2];
//                        Course toRemove = null;
//                        for (Course course : savedCourses) {
//                            if (course.getSubjCode().equalsIgnoreCase(subj) &&
//                                    course.getCourseNum() == courseNum &&
//                                    course.getSection().equalsIgnoreCase(section)) {
//                                toRemove = course;
//                                break;
//                            }
//                        }
//                        if (toRemove != null) {
//                            savedCourses.remove(toRemove);
//                            System.out.println("Course removed from saved courses.");
//                        } else {
//                            System.out.println("Course not found in saved courses.");
//                        }
//                    } else {
//                        System.out.println("Invalid input. Please enter in the format: SUBJ NUMBER SECTION");
//                    }
//
//                } else if (input.equalsIgnoreCase("add")) {
//                    System.out.println("Enter course subj, number, then section to add to a schedule (example: HUMA 200 D):");
//                    String courseLabel = scanner.nextLine();
//                    String[] parts = courseLabel.split(" ");
//                    if (parts.length == 3) {
//                        String subj = parts[0];
//                        int courseNum = Integer.parseInt(parts[1]);
//                        String section = parts[2];
//
//                        Course toAdd = null;
//                        for (Course course : savedCourses) {
//                            if (course.getSubjCode().equalsIgnoreCase(subj) &&
//                                    course.getCourseNum() == courseNum &&
//                                    course.getSection().equalsIgnoreCase(section)) {
//                                toAdd = course;
//                                break;
//                            }
//                        }
//                        if (toAdd != null) {
//                            System.out.println("Schedule List");
//                            for(Schedule schedule : schedules) {
//                                System.out.println(schedule);
//                            }
//                            System.out.println("Successfully selected course to add to a schedule.");
//                            System.out.println("Now enter the schedule ID of a schedule to have a course added to it");
//                            int scheduleID = scanner.nextInt();
//                            for(Schedule schedule: schedules) {
//                                if(schedule.getScheduleID() == scheduleID) {
//                                    schedule.addToSchedule(this, scheduleID, toAdd);
//                                    System.out.println("Course added to schedule.");
//                                    break;
//                                }
//                                System.out.println("Schedule not found. Please enter a valid schedule ID.");
//                            }
//
//                        } else {
//                            System.out.println("Course not found in saved courses.");
//                        }
//                    } else {
//                        System.out.println("Invalid input. Please enter in the format: SUBJ NUMBER SECTION");
//                    }
//                } else {
//                    System.out.println("Invalid input. Please try again.");
//                }
//            }
//        } else {
//            System.out.println("No saved courses.");
//        }
//    }





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
    public void deleteSchedule(int scheduleID) {
        schedules.removeIf(schedule -> schedule.getScheduleID() == scheduleID);
    }

}

