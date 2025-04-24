import io.javalin.Javalin;
import io.javalin.json.JavalinJackson;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


public class Main {

    // part of backend testing - define variables
    static boolean loggedIn = false;
    static Scanner scnr = new Scanner(System.in);
    private static Student student; // Store the student object globally
    public static List<Course> courses;

    public static void main(String[] args) {

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Read the root node of the JSON file
            JsonNode rootNode = objectMapper.readTree(new File("data_wolfe.json"));

            // Extract the "classes" array
            JsonNode classesNode = rootNode.get("classes");

            // Convert the classes JSON array to a List<Course>
            courses = objectMapper.readerForListOf(Course.class).readValue(classesNode);

        } catch (IOException e) {
            e.printStackTrace();
        }

        // default user
        student = new Student("testUser", 123456, "password123");
        Schedule testSchedule1 = new Schedule(student, new ArrayList<>(), "My Schedule");
        testSchedule1.setSemester("2023_Fall");
        Course testCourse1 = courses.get(33);
        Course testCourse3 = courses.get(300);
        Course testCourse4 = courses.get(361);
        testSchedule1.addToSchedule(testCourse1);
        testSchedule1.addToSchedule(testCourse3);
        testSchedule1.addToSchedule(testCourse4);
        Schedule testSchedule2 = new Schedule(student, new ArrayList<>(), "Backup #1");
        testSchedule2.setSemester("2023_Fall");
        Course testCourse2 = courses.get(1);
        Course testCourse5 = courses.get(273);
        testSchedule2.addToSchedule(testCourse2);
        testSchedule2.addToSchedule(testCourse5);
        Schedule testSchedule3 = new Schedule(student, new ArrayList<>(), "Spring!");
        testSchedule3.setSemester("2024_Spring");
        Course testCourse6 = courses.get(895);
        testSchedule3.addToSchedule(testCourse6);

        student.addSavedCourse(testCourse1);



        // Frontend testing --------------------------------------------------------------------
        // Configure Jackson ObjectMapper
        ObjectMapper objectMapper1 = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

        // Create Javalin app and set Jackson as the JSON mapper
        Javalin app = Javalin.create(config -> {
            config.jsonMapper(new JavalinJackson(objectMapper1));

            // Enable CORS for frontend access
            config.plugins.enableCors(cors -> {
                cors.add(it -> it.allowHost("http://localhost:5173"));
            });
        });

        // Define a test API endpoint
        app.get("/api/hello", ctx -> ctx.json(new Message("Hello from Javalin with Jackson!")));

//        app.get("/api/courses", ctx -> ctx.json(courses));
        app.get("/api/courses", ctx -> {
            String semester = ctx.queryParam("semester");
            int page = ctx.queryParamAsClass("page", Integer.class).getOrDefault(1);
            int limit = ctx.queryParamAsClass("limit", Integer.class).getOrDefault(10);

            List<Course> filteredCourses = (semester != null && !semester.isEmpty())
                ? courses.stream().filter(course -> semester.equalsIgnoreCase(course.getSemester())).toList()
                : courses;

            int start = (page - 1) * limit;
            int end = Math.min(start + limit, filteredCourses.size());

            List<Course> paginatedCourses = filteredCourses.subList(start, end);

            Map<String, Object> response = new HashMap<>();
            response.put("courses", paginatedCourses);
            response.put("totalCourses", filteredCourses.size());
            response.put("totalPages", (int) Math.ceil((double) filteredCourses.size() / limit));

            ctx.json(response);
        });

        app.get("/api/schedules", ctx -> {
            String semester = ctx.queryParam("semester");
            if (semester == null || semester.isEmpty()) {
                List<String> allSemesters = student.getSchedules().stream()
                    .map(Schedule::getSemester)
                    .distinct()
                    .toList();
                ctx.json(allSemesters);
                return;
            }

            List<Schedule> filteredSchedules = student.getSchedules().stream()
                .filter(schedule -> schedule.getSemester().equalsIgnoreCase(semester))
                .toList();

            ctx.json(filteredSchedules);
        });

        // ADD SCHEDULE
       app.post("/api/schedules", ctx -> {
           String name = ctx.queryParam("name");
           String semester = ctx.queryParam("semester");

           if (name == null || name.isEmpty() || semester == null || semester.isEmpty()) {
               ctx.status(400).json(Map.of("error", "Name and semester are required."));
               return;
           }

           Schedule newSchedule = new Schedule(student, new ArrayList<>(), name);
           newSchedule.setSemester(semester);
           ctx.status(201).json(Map.of("message", "Schedule created successfully."));
       });

        //app.get("/api/search", ctx -> ctx.json(new Message("Hello from Javalin with Jackson!")));
        app.get("/api/search-courses", ctx -> {
            String searchTerm = ctx.queryParamAsClass("searchTerm", String.class).getOrDefault("").toLowerCase();
            int page = ctx.queryParamAsClass("page", Integer.class).getOrDefault(1);
            int limit = ctx.queryParamAsClass("limit", Integer.class).getOrDefault(10);
            String semester = ctx.queryParam("semester");
            String code = ctx.queryParamAsClass("code", String.class).getOrDefault("").toLowerCase();
            String day1 = ctx.queryParamAsClass("day1", String.class).getOrDefault("").toLowerCase();
            String day2 = ctx.queryParamAsClass("day2", String.class).getOrDefault("").toLowerCase();
            String day3 = ctx.queryParamAsClass("day3", String.class).getOrDefault("").toLowerCase();
            String day4 = ctx.queryParamAsClass("day4", String.class).getOrDefault("").toLowerCase();
            String day5 = ctx.queryParamAsClass("day5", String.class).getOrDefault("").toLowerCase();
            String startTime = ctx.queryParamAsClass("startTime", String.class).getOrDefault("").toLowerCase();
            String endTime = ctx.queryParamAsClass("endTime", String.class).getOrDefault("").toLowerCase();
            Boolean hideFullCourses = ctx.queryParamAsClass("hideFullCourses", Boolean.class).getOrDefault(false);
            String credits = ctx.queryParamAsClass("credits", String.class).getOrDefault("").toLowerCase();



            // Filter courses based on the search term and semester
            List<Course> filteredCourses = (semester != null && !semester.isEmpty())
                    ? courses.stream().filter(course -> semester.equalsIgnoreCase(course.getSemester())).toList()
                    : courses;

            // Filter courses based on the search term
//            List<Course> filteredCourses = courses;
            if(searchTerm != null && !searchTerm.trim().isEmpty()) {
                filteredCourses = filteredCourses.stream()
                        .filter(course -> course.getName().toLowerCase().contains(searchTerm.trim().toLowerCase()) || course.getFullCourseCode().trim().toLowerCase().contains(searchTerm.trim().toLowerCase()))
                        .toList();
            }
            if(code!="") {
                filteredCourses = filteredCourses.stream()

                        .filter(course -> course.getFullCourseCode().trim().toLowerCase().contains(code.trim().toLowerCase()))
                        .toList();
            }

            StringBuilder days = new StringBuilder();
            days.append(day1).append(" ").append(day2).append(" ").append(day3).append(" ").append(day4).append(" ").append(day5);
            String daysString = days.toString().trim().toLowerCase();
            if(daysString!="") {
                filteredCourses = filteredCourses.stream()

                        .filter(course -> daysString.contains(course.daysString().trim().toLowerCase()))
                        .toList();

            }
            if(startTime!="") {
                filteredCourses = filteredCourses.stream()
                        .filter(course -> course.getTimes().stream()
                                .anyMatch(timeBlock -> timeBlock.getStartTime().compareTo((startTime+":00").trim()) >= 0))
                        .toList();

            }
            if(endTime!="") {
                filteredCourses = filteredCourses.stream()
                        .filter(course -> course.getTimes().stream()
                                .anyMatch(timeBlock -> timeBlock.getEndTime().compareTo((endTime+":00").trim()) <= 0))
                        .toList();
            }
            if(hideFullCourses){
                filteredCourses = filteredCourses.stream()
                        .filter(course -> course.getIs_open())
                        .toList();
            }
            if(credits!="") {
                filteredCourses = filteredCourses.stream()
                        .filter(course -> course.getNumCredits() == Integer.parseInt(credits))
                        .toList();
            }
            // Paginate the filtered results
            int start = (page - 1) * limit;
            int end = Math.min(start + limit, filteredCourses.size());
            List<Course> paginatedCourses = filteredCourses.subList(start, end);

            // Prepare the response
            Map<String, Object> response = new HashMap<>();
            response.put("courses", paginatedCourses);
            response.put("totalCourses", filteredCourses.size());
            response.put("totalPages", (int) Math.ceil((double) filteredCourses.size() / limit));

            ctx.json(response);
        });

        app.start(7000);
        //--------------------------------------------------------------------------------------

        // Backend Testing ---------------------------------------------------------------------
        while(true) {
            showMenu();
            int choice = getUserChoice();
            handlechoice(choice);
        }

    }

    private static void showMenu() {
        System.out.println("\nWhat would you like to do?");
        System.out.println("1. Create a new profile");
        System.out.println("2. Login (required before modification)");
        System.out.println("3. Create a new schedule");
        System.out.println("4. Remove a schedule");
        System.out.println("5. View calendar");
        System.out.println("6. Add course to schedule");
        System.out.println("7. Remove course from schedule");
        System.out.println("8. Search for a course");
        System.out.println("9. Print all schedules");
        System.out.println("10. Exit");
        System.out.print("Enter your choice: ");
    }

    private static String validateInput(String prompt, String errorMsg, String inputType){
        String input = "";
        boolean valid = false;

        while (!valid) {
            System.out.println(prompt);
            input = scnr.nextLine().trim();

            // Check validation based on input type
            switch (inputType) {
                case "username":
                    if (input.isEmpty() || input.length() < 3) {
                        System.out.println(errorMsg);
                    } else {
                        valid = true;
                    }
                    break;

                case "ID":
                    if (input.length() != 6 || !input.matches("\\d{6}")) {
                        System.out.println(errorMsg);
                    } else {
                        valid = true;
                    }
                    break;

                case "password":
                    if (input.length() < 6 || !input.matches("[a-zA-Z0-9]+")) {
                        System.out.println(errorMsg);
                    } else {
                        valid = true;
                    }
                    break;
                case "semester":
                    if (input.isEmpty() || !input.matches("\\d{4}_[a-zA-Z]+")) {
                        System.out.println(errorMsg);
                    } else {
                        valid = true;
                    }
                    break;
            }
        }
        return input;
    }

    private static int getUserChoice() {
        int choice = -1;
        try {
            choice = Integer.parseInt(scnr.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
        }
        return choice;
    }

    private static void handlechoice(int choice) {
        switch (choice) {
            case 1:
                String username = validateInput("Please enter a username:", "Username must be at least 3 characters long.", "username");
                int studentID = Integer.parseInt(validateInput("Please enter student ID:", "ID must be exactly 6 digits.", "ID"));
                String password = validateInput("Please enter a password:", "Password must be at least 6 alphanumeric characters long.", "password");

                student = new Student(username, studentID, password);
                break;
            case 2:
                for (int i = 0; i < 3; i++) {
                    System.out.println("Please enter your username:");
                    String usernameAttempt = scnr.nextLine();
                    System.out.println("Please enter your password:");
                    String passwordAttempt = scnr.nextLine();
                    if(student.verifyLogin(usernameAttempt, passwordAttempt)) {
                        loggedIn = true;
                        System.out.println("Login successful.");
                        break;
                    } else {
                        System.out.println("Incorrect username or password. Please try again.");
                    }
                }
                break;
            case 3:
                if (loggedIn) {
                    Schedule newSchedule = new Schedule(student); // Create a new schedule under student
                    String input = validateInput("What semester is this schedule for? Enter Year_Semester:", "Semester must be in the format YYYY_Semester", "semester");
                    newSchedule.setSemester(input);
                    System.out.println("New schedule created for " + input + " with ID: " + newSchedule.getScheduleID());                } else {
                    System.out.println("You must be logged in to modify schedules.");
                }
                break;
            case 4:
                if (loggedIn) {
                    int scheduleID = Integer.parseInt(validateInput("Which schedule would you like to remove? Enter schedule ID:", "ID must be exactly 6 digits.", "ID"));
                    // check that schedule exists
                    for (Schedule sched : student.getSchedules()){
                        if (scheduleID == sched.getScheduleID()){
                            student.deleteSchedule(scheduleID);
                            System.out.println("Schedule removed.");
                            break;
                        } else {
                            System.out.println("Schedule not found.");
                        }
                    }

                } else {
                    System.out.println("You must be logged in to modify schedules.");
                }
                break;
            case 5:
                if (loggedIn) {
                    int scheduleID = Integer.parseInt(validateInput("Which schedule would you like to view? Enter schedule ID:", "ID must be exactly 6 digits.", "ID"));//                    System.out.println(student.getSchedules().get(0).getScheduleID());
                    for (Schedule schedule : student.getSchedules()) {
//                        System.out.println(schedule.getScheduleID());
                        if (schedule.getScheduleID() == scheduleID) {
                            System.out.println(schedule.calendarView());
                            break;
                        } else {
                            System.out.println("Schedule not found. Check ID and try again.");
                        }
                    }

                } else {
                    System.out.println("You must be logged in to modify schedules.");
                }
                break;
            case 6:
                if (loggedIn) {
                    int scheduleID = Integer.parseInt(validateInput("Which schedule would you like to modify? Enter schedule ID:", "ID must be exactly 6 digits.", "ID"));
                    System.out.println("Enter the course subject code:");
                    String subjCode = scnr.nextLine().trim();
                    System.out.println("Enter the course number:");
                    int courseNum = 0;
                    try {
                        courseNum = Integer.parseInt(scnr.nextLine().trim());
                    } catch (NumberFormatException e) {
                        System.out.println("Error: Invalid input. Please enter a valid numeric course number.");
                    }
                    System.out.println("Enter the section:");
                    String section = scnr.nextLine();

                    System.out.println("Enter the semester:");
                    String semester = scnr.nextLine();
                    boolean courseFound = false;
                    for (Course course : courses) {
                        if (course.getSubjCode().equalsIgnoreCase(subjCode) && course.getCourseNum() == courseNum && course.getSection().equalsIgnoreCase(section) && course.getSemester().equals(semester)) {
                            courseFound = true;
                            for (Schedule schedule : student.getSchedules()) {
                                if (schedule.getScheduleID() == scheduleID) {
                                    schedule.addToSchedule(course);
//                                    System.out.println("Course successfully added to schedule.");
                                    break;
                                }
                            }
                            break;
                        }
                    }
                    if (!courseFound) {
                        System.out.println("Schedule/course not found. Check ID and try again.");
                        break;
                    }
                } else {
                    System.out.println("You must be logged in to modify schedules.");
                }
                break;
            case 7:
                if (loggedIn) {
                    // remove course
                    int scheduleID = Integer.parseInt(validateInput("Which schedule would you like to modify? Enter schedule ID:", "ID must be exactly 6 digits.", "ID"));
                    System.out.println("Enter the course subject code:");
                    String subjCode = scnr.nextLine().trim();
                    System.out.println("Enter the course number:");
                    int courseNum = 0;
                    try {
                        courseNum = Integer.parseInt(scnr.nextLine().trim());
                    } catch (NumberFormatException e) {
                        System.out.println("Error: Invalid input. Please enter a valid numeric course number.");
                    }
                    System.out.println("Enter the section:");
                    String section = scnr.nextLine().trim();

                    System.out.println("Enter the semester:");
                    String semester = scnr.nextLine();

                    boolean courseFound2 = false;
                    for (Course course : courses) {
                        if (course.getSubjCode().equalsIgnoreCase(subjCode) && course.getCourseNum() == courseNum && course.getSection().equalsIgnoreCase(section) && course.getSemester().equals(semester)) {
                            courseFound2 = true;
                            for (Schedule schedule : student.getSchedules()) {
                                if (schedule.getScheduleID() == scheduleID) {
                                    schedule.removeFromSchedule(course);
                                    break;
                                }
                            }
                            break;
                        }
                    }
                    if (!courseFound2) {
                        System.out.println("Schedule/course not found. Check ID and try again.");
                        break;
                    }

                } else {
                    System.out.println("You must be logged in to modify schedules.");
                }
                break;
            case 8:
                Search search = new Search();
                search.conductSearchLoop((ArrayList<Course>) courses);
                break;
            case 9:
                // print all schedules
                if (loggedIn){
                    for (Schedule schedule : student.getSchedules()) {
                        System.out.println(schedule);
                    }
                    break;
                } else {
                    System.out.println("You must be logged in to view schedules.");
                    break;
                }
            case 10:
                System.out.println("Exiting the application...");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid choice. Please select a valid option.");
        }
    }

    // Data class for JSON response - frontend testing
    public static class Message {
        public String message;

        public Message(String message) {
            this.message = message;
        }
    }
}
