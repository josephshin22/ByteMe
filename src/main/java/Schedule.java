    import java.util.*;

    public class Schedule {
        private String name;
        private List<Course> courses;
        private List<Event> events;
        private int scheduleID;
        private int studentID;
        private String semester;

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
            this.events = new ArrayList<Event>();
            this.name = "";
            s.setSchedule(this);
        }

        private int createScheduleID() {
            return (int) (Math.random() * 900000) + 100000;
        }

        public void addToSchedule(Course c1) {
            if(this.semester.equals(c1.getSemester())) {
                for (Course existingCourse : this.getCourses()) {
                    if (existingCourse.hasConflict(c1)) {
                        System.out.println("Conflict detected between " + existingCourse.getName() + " and " + c1.getName());
                        System.out.println("Course not added.");
                        return;
                    }
                }
                this.courses.add(c1);
                System.out.println("Course " + c1.getName() + " added to the schedule.");
            } else {
                System.out.println("Course " + c1.getName() + " is not in the same semester as the schedule.");
            }
        }


        public void removeFromSchedule(Course c1) {
                if (this.courses != null && this.courses.contains(c1)) {
                    this.courses.remove(c1);
                    System.out.println("Course " + c1.getName() + " removed from the schedule.");
                }
                else {
                    System.out.println("Course not found in schedule.");
                }
        }

        @Override
        public String toString() {
            return "Schedule Name: " + name + "\nSchedule ID: " + scheduleID + "\n" + "Student ID: " + studentID + "\n" +
                    "Courses: " + this.courses + "\n";
        }

        // getter for courses
        public List<Course> getCourses() {
            return courses;
        }

        public void setSemester(String semester){
            this.semester = semester;
        }

        public String getSemester() {
            return semester;
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
                    for (Course course : this.courses) {
                        for (timeBlock time : course.getTimes()) {
                            if (time.getDay().equals(day) && Integer.parseInt(time.getStartTime().split(":")[0]) == hour) {
                                calendar.append(String.format("%-10s", course.getSubjCode() + " " + course.getCourseNum() + "-" + course.getSection()));


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

        public int getScheduleID() {
            return scheduleID;
        }
    }
