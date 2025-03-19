import io.javalin.Javalin;
import io.javalin.json.JavalinJackson;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


public class Main {
    public static void main(String[] args) {
//        ArrayList<Course> masterCourse = new ArrayList<>();
//
//        // Create time blocks for the courses
//        timeBlock[] timeBlocks1 = {
//                new timeBlock("M", "09:00:00", "08:00:00"),
//                new timeBlock("W", "09:00:00", "08:00:00"),
//                new timeBlock("F", "09:00:00", "08:00:00")
//        };
//
//        timeBlock[] timeBlocks2 = {
//                new timeBlock("T", "11:00:00", "10:00:00"),
//                new timeBlock("R", "11:00:00", "10:00:00")
//        };
//
//        timeBlock[] timeBlocks3 = {
//                new timeBlock("M", "09:00:00", "08:00:00"),
//                new timeBlock("W", "09:00:00", "08:00:00"),
//                new timeBlock("F", "09:00:00", "08:00:00"),
//                new timeBlock("T", "13:20:00", "12:30:00")
//
//        };
//
//
//
//
//        // Add 5 courses to the masterCourse list
//        masterCourse.add(new Course("Calculus", 101, "MATH", "A", new String[]{"Dr. Smith"}, "Room 101", 3, null, timeBlocks1, false, true, 10, 30, "Fall 2023"));
//        masterCourse.add(new Course("Physics", 201, "PHYS", "B", new String[]{"Dr. Johnson"}, "Room 202", 4, null, timeBlocks3, false, true, 5, 25, "Fall 2023"));
//        masterCourse.add(new Course("Chemistry", 301, "CHEM", "C", new String[]{"Dr. Brown"}, "Room 303", 3, null, timeBlocks2, true, false, 0, 20, "Fall 2023"));
//        masterCourse.add(new Course("Biology", 401, "BIOL", "D", new String[]{"Dr. Taylor"}, "Room 404", 4, null, timeBlocks3, false, true, 15, 35, "Fall 2023"));
//        masterCourse.add(new Course("Computer Science", 501, "CSCI", "E", new String[]{"Dr. Wilson"}, "Room 505", 3, null, timeBlocks1, false, true, 20, 40, "Fall 2023"));
//
//        // Print the courses to verify
//        for (Course course : masterCourse) {
//            System.out.println(course);
//        }
//        Search searchBoy = new Search();
//        searchBoy.conductSearchLoop(masterCourse);
        //TESTING ^^^^^ FOR SEARCH

        ObjectMapper objectMapper = new ObjectMapper();
        List<Course> courses = null;
        try {
            System.out.println("test");
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

        // Test Schedule Class
        List<Course> courses1 = courses.subList(1, 3);
        Student stu1 = new Student("John Smith",123456789);
        Schedule sch1 = new Schedule(stu1, courses1, "Spring 2025");
        System.out.println(sch1.courses.get(1).getName());  // test to see if courses were added to schedule
        System.out.println(sch1.calendarView());

        // Test Calendar Class
        System.out.println("Which calendar view would you like?");
        System.out.println("(1) Day View (2) Week View");

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

    }

    // Data class for JSON response - frontend testing
    public static class Message {
        public String message;

        public Message(String message) {
            this.message = message;
        }
    }
}
