import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


public class Main {

    public static void main(String[] args) throws InterruptedException {
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
            //printCourses(courses);

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Let's create an account!");
        Student student1 = new Student();
        Thread.sleep(1000); // Delay for 1 second

        System.out.println("What would you like your username to be?");
        Scanner scnr = new Scanner(System.in);
        student1.setUsername(scnr.nextLine());
        Thread.sleep(1000); // Delay for 1 second

        System.out.println("What would you like your password to be?");
        student1.setPassword(scnr.nextLine());
        Thread.sleep(1000); // Delay for 1 second

        System.out.println("Login: Please enter your password");
        if (student1.verifyPassword(scnr.nextLine())) {
            System.out.println("Login successful!");
        } else {
            System.out.println("Invalid password :(");
        }
    }

    // Prints the entire courses List
    public static void printCourses(List<Course> courses) {
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
    }
}
