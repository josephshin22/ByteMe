import io.javalin.Javalin;
import io.javalin.json.JavalinJackson;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


public class Main {
    public static void main(String[] args) {
        // Configure Jackson ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

        // Create Javalin app and set Jackson as the JSON mapper
        Javalin app = Javalin.create(config -> {
            config.jsonMapper(new JavalinJackson(objectMapper));

            // Enable CORS for frontend access
            config.plugins.enableCors(cors -> {
                cors.add(it -> it.allowHost("http://localhost:5173"));
            });
        });

        // Define a test API endpoint
        app.get("/api/hello", ctx -> ctx.json(new Message("Hello from Javalin with Jackson!")));

    public static void main(String[] args) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            System.out.println("test");
            // Read the root node of the JSON file
            JsonNode rootNode = objectMapper.readTree(new File("data_wolfe.json"));

            // Extract the "classes" array
            JsonNode classesNode = rootNode.get("classes");

            // Convert the classes JSON array to a List<Course>
            List<Course> courses = objectMapper.readerForListOf(Course.class).readValue(classesNode);

            // Print all courses
            for (Course course : courses) {
                System.out.println("=====================================");
                System.out.println("Course Name: " + course.getName());
                System.out.println("Course Number: " + course.getCourseNum());
                System.out.println("Faculty: " + (course.getFaculty() != null ? String.join(", ", course.getFaculty()) : "N/A"));
                System.out.println("Location: " + course.getLocation());
                System.out.println("Credits: " + course.getNumCredits());
                System.out.println("Open Seats: " + course.getOpenSeats());
                System.out.println("Section: " + course.getSection());
                System.out.println("Semester: " + course.getSemester());
                System.out.println("Lab: " + (course.getIs_lab() ? "Yes" : "No"));
                System.out.println("Open for Enrollment: " + (course.getIs_open() ? "Yes" : "No"));

                for (timeBlock timeBlock : course.getTimes()) {
                    System.out.println("\tDay: " + timeBlock.getDay());
                    System.out.println("\tStart Time: " + timeBlock.getStartTime());
                    System.out.println("\tEnd Time: " + timeBlock.getEndTime());
                    System.out.println("\t---------------------");
                }
                System.out.println("=====================================");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
        app.start(7000);
    }

    // Data class for JSON response
    public static class Message {
        public String message;

        public Message(String message) {
            this.message = message;
        }
    }
}
