import io.javalin.Javalin;
import io.javalin.json.JavalinJackson;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

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
