import io.javalin.Javalin;
import io.javalin.json.JavalinJackson;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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

            // Print all courses
            for (Course course : courses) {
                course.showCourse();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


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
                System.out.println("Please enter a username:");
                String username = scnr.nextLine();
                System.out.println("Please enter a student ID:");
                int studentID = Integer.parseInt(scnr.nextLine());
                System.out.println("Please enter a password:");
                String password = scnr.nextLine();
                student = new Student(username, studentID, password);
                break;
            case 2:
                for (int i = 0; i < 3; i++) {
                    System.out.println("Please enter your username:");
                    String usernameAttempt = scnr.nextLine();
                    System.out.println("Please enter your password:");
                    String passwordAttempt = scnr.nextLine();
                    if (student.verifyPassword(passwordAttempt)) {
                        loggedIn = true;
                        System.out.println("Login successful.");
                        break;
                    } else {
                        System.out.println("Incorrect password. Please try again.");
                    }
                }
                break;
            case 3:
                if (loggedIn) {
                    Schedule newSchedule = new Schedule(student); // Create a new schedule under student
                    System.out.println("New schedule created with ID: " + newSchedule.getScheduleID());
                } else {
                    System.out.println("You must be logged in to modify schedules.");
                }
                break;
            case 4:
                if (loggedIn) {
                    System.out.println("Which schedule would you like to remove? (Enter a schedule ID)");
                    int scheduleID = Integer.parseInt(scnr.nextLine());
                    student.deleteSchedule(scheduleID);
                } else {
                    System.out.println("You must be logged in to modify schedules.");
                }
                break;
            case 5:
                if (loggedIn) {
                    System.out.println("Which schedule would you like to view? (Enter a schedule ID)");
                    int scheduleID = Integer.parseInt(scnr.nextLine());
                    System.out.println(student.getSchedules().get(0).getScheduleID());
                    for (Schedule schedule : student.getSchedules()) {
                        System.out.println(schedule.getScheduleID());
                        if (schedule.getScheduleID() == scheduleID) {


                            System.out.println(schedule.calendarView());
                            break;
                        }
                    }
                } else {
                    System.out.println("You must be logged in to modify schedules.");
                }
                break;
            case 6:
                if (loggedIn) {
                    System.out.println("Which schedule would you like to modify? (Enter a schedule ID)");
                    int scheduleID = Integer.parseInt(scnr.nextLine());
                    System.out.println("Enter the course subject code:");
                    String subjCode = scnr.nextLine();
                    System.out.println("Enter the course number:");
                    int courseNum = Integer.parseInt(scnr.nextLine());
                    System.out.println("Enter the section:");
                    String section = scnr.nextLine();
                    for (Course course : courses) {
                        if (course.getSubjCode().equals(subjCode) && course.getCourseNum() == courseNum && course.getSection().equals(section)) {
                            for (Schedule schedule : student.getSchedules()) {
                                if (schedule.getScheduleID() == scheduleID) {
                                    schedule.addToSchedule(student, course);
                                    break;
                                }
                            }
                            break;
                        }
                    }
                } else {
                    System.out.println("You must be logged in to modify schedules.");
                }
                break;
            case 7:
                if (loggedIn) {
                    // remove course
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
