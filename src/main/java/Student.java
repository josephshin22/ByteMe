import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Student {
    String username;
    int studentID;
    String password;
    String[] major;
    String[] minor;
    String gradYear;
    String gradMonth;
    Course[] takenCourses;
    ArrayList<Schedule> schedules;
    ArrayList<Course> savedCourses;
    public Student() {
        this.schedules = new ArrayList<>();
    }

    public void addSchedule(Schedule schedule) {
        schedules.add(schedule);
    }
    public void showSavedCourses() {
        if (savedCourses != null && !savedCourses.isEmpty()) {
            System.out.println("Saved Courses:");
            for (Course course : savedCourses) {
                course.showCourse();
            }
            boolean done = false;
            System.out.println("Here is your saved courses page.");
            System.out.println("Type 'exit' to return to the main menu.");
            System.out.println("Type 'remove' to remove a course from your saved courses.");
            System.out.println("Type 'add' to add a course to one of your schedules.");
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

                        Course toRemove = null;
                        for (Course course : savedCourses) {
                            if (course.getSubjCode().equalsIgnoreCase(subj) &&
                                course.getCourseNum() == courseNum &&
                                course.getSection().equalsIgnoreCase(section)) {
                                toRemove = course;
                                break;
                            }
                        }
                        if (toRemove != null) {
                            savedCourses.remove(toRemove);
                            System.out.println("Course removed from saved courses.");
                        } else {
                            System.out.println("Course not found in saved courses.");
                        }
                    } else {
                        System.out.println("Invalid input. Please enter in the format: SUBJ NUMBER SECTION");
                    }

                } else if (input.equalsIgnoreCase("add")) {
                    System.out.println("Enter course subj, number, then section to add to a schedule (example: HUMA 200 D):");
                    String courseLabel = scanner.nextLine();
                    String[] parts = courseLabel.split(" ");
                    if (parts.length == 3) {
                        String subj = parts[0];
                        int courseNum = Integer.parseInt(parts[1]);
                        String section = parts[2];

                        Course toAdd = null;
                        for (Course course : savedCourses) {
                            if (course.getSubjCode().equalsIgnoreCase(subj) &&
                                    course.getCourseNum() == courseNum &&
                                    course.getSection().equalsIgnoreCase(section)) {
                                toAdd = course;
                                break;
                            }
                        }
                        if (toAdd != null) {
                            System.out.println("Schedule List");
                            for(Schedule schedule : schedules) {
                                System.out.println(schedule);
                            }
                            System.out.println("Successfully selected course to add to a schedule.");
                            System.out.println("Now enter the schedule ID of a schedule to have a course added to it");
                            int scheduleID = scanner.nextInt();
                            for(Schedule schedule: schedules) {
                                if(schedule.scheduleID == scheduleID) {
                                    schedule.addToSchedule(this, scheduleID, toAdd);
                                    System.out.println("Course added to schedule.");
                                    break;
                                }
                                System.out.println("Schedule not found. Please enter a valid schedule ID.");
                            }

                        } else {
                            System.out.println("Course not found in saved courses.");
                        }
                    } else {
                        System.out.println("Invalid input. Please enter in the format: SUBJ NUMBER SECTION");
                    }
                } else {
                    System.out.println("Invalid input. Please try again.");
                }
            }
        } else {
            System.out.println("No saved courses.");
        }
    }

    public ArrayList<Schedule> getSchedules() {
        return schedules;
    }

    public void removeSchedule(Schedule schedule) {
        schedules.remove(schedule);
    }
    public void addSavedCourse(Course course) {
        if (savedCourses == null) {
            savedCourses = new ArrayList<>();
        }
        for (Course c : savedCourses) {

            if(c.getCourseNum() == course.getCourseNum() && c.getSubjCode().equals(course.getSubjCode())&& c.getSemester().equals(course.getSemester())&& c.getSection().equals(course.getSection()))
            {
                System.out.println("Course already saved.");
                return;
            }
        }
        savedCourses.add(course);
    }


}