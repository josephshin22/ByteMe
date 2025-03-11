import java.io.File;
import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Main {

    public static void main(String[] args) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            System.out.println("test");
            // Read the root node of the JSON file
            JsonNode rootNode = objectMapper.readTree(new File("data_wolfe.json"));

            // Extract the "classes" array
            JsonNode classesNode = rootNode.get("classes");

            // Convert the "classes" JSON array to a List<Course>
            List<Course> courses = objectMapper.readerForListOf(Course.class).readValue(classesNode);

            // Print all courses
            for (Course course : courses) {
                System.out.println(course);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
