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

import java.sql.*;

public class Main {

    // part of backend testing - define variables
    static boolean loggedIn = false;
    static Scanner scnr = new Scanner(System.in);
    private static Student student; // Store the student object globally
    public static List<Course> courses;

    public static void main(String[] args) throws SQLException {
        String url = "jdbc:sqlite:database.db";
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

        // Step 1: Create tables
        try (Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement()) {

            String createCoursesSQL = """
                CREATE TABLE IF NOT EXISTS courses (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT,
                    location TEXT,
                    section TEXT,
                    semester TEXT,
                    courseNum INTEGER,
                    numCredits INTEGER,
                    is_lab BOOLEAN,
                    subjCode TEXT,
                    totalSeats INTEGER
                );
            """;

            String createTimesSQL = """
                CREATE TABLE IF NOT EXISTS course_times (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    course_id INTEGER,
                    day TEXT,
                    start_time TEXT,
                    end_time TEXT,
                    FOREIGN KEY(course_id) REFERENCES courses(id)
                );
            """;

            String createFacultySQL = """
                CREATE TABLE IF NOT EXISTS course_faculty (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    course_id INTEGER,
                    faculty_name TEXT,
                    FOREIGN KEY(course_id) REFERENCES courses(id)
                );
            """;
            String createUserCoursesSQL = """
                CREATE TABLE IF NOT EXISTS user_courses (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    user_id INTEGER NOT NULL,
                    course_id INTEGER NOT NULL,
                    date_added TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    FOREIGN KEY(course_id) REFERENCES courses(id),
                    UNIQUE(user_id, course_id)
                );
            """;

            stmt.execute(createCoursesSQL);
            stmt.execute(createTimesSQL);
            stmt.execute(createFacultySQL);
            stmt.execute(createUserCoursesSQL);

            System.out.println("All tables created or already exist.");
        } catch (SQLException e) {
            System.out.println("Error creating tables: " + e.getMessage());
        }

        // Step 2: Insert course + times + faculty
        try (Connection conn = DriverManager.getConnection(url)) {
            String insertCourseSQL = """
                INSERT INTO courses (
                    name, location, section, semester, courseNum,
                    numCredits, is_lab, subjCode, totalSeats
                ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

            PreparedStatement courseStmt = conn.prepareStatement(insertCourseSQL, Statement.RETURN_GENERATED_KEYS);
            PreparedStatement timeStmt = conn.prepareStatement("""
                INSERT INTO course_times (course_id, day, start_time, end_time) VALUES (?, ?, ?, ?)
            """);

            PreparedStatement facultyStmt = conn.prepareStatement("""
                INSERT INTO course_faculty (course_id, faculty_name) VALUES (?, ?)
            """);

            for (Course c : courses) {
                // Insert course
                courseStmt.setString(1, c.getName());
                courseStmt.setString(2, c.getLocation());
                courseStmt.setString(3, c.getSection());
                courseStmt.setString(4, c.getSemester());
                courseStmt.setInt(5, c.getCourseNum());
                courseStmt.setInt(6, c.getNumCredits());
                courseStmt.setBoolean(7, c.getIs_lab());
                courseStmt.setString(8, c.getSubjCode());
                courseStmt.setInt(9, c.getTotalSeats());
                courseStmt.executeUpdate();

                // Get course ID
                ResultSet rs = courseStmt.getGeneratedKeys();
                int courseId = -1;
                if (rs.next()) {
                    courseId = rs.getInt(1);
                }

                // Insert times
                for (timeBlock t : c.getTimes()) {
                    timeStmt.setInt(1, courseId);
                    timeStmt.setString(2, t.getDay());
                    timeStmt.setString(3, t.getStartTime());
                    timeStmt.setString(4, t.getEndTime());
                    timeStmt.executeUpdate();
                }

                // Insert faculty
                for (String prof : c.getFaculty()) {
                    facultyStmt.setInt(1, courseId);
                    facultyStmt.setString(2, prof);
                    facultyStmt.executeUpdate();
                }
            }

        System.out.println("All courses, times, and faculty inserted.");

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

        int totalCoursesCount = getTotalCoursesCount();
        List<Course> allCoursesFromDB = getCoursesFromDB(totalCoursesCount, 0);


            // Define a test API endpoint
        app.get("/api/hello", ctx -> ctx.json(new Message("Hello from Javalin with Jackson!")));

//        app.get("/api/courses", ctx -> ctx.json(courses));
        app.get("/api/courses", ctx -> {
            String semester = ctx.queryParam("semester");
            int page = ctx.queryParamAsClass("page", Integer.class).getOrDefault(1);
            int limit = ctx.queryParamAsClass("limit", Integer.class).getOrDefault(10);
            int offset = (page - 1) * limit;

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

        // DELETE SCHEDULE by ID
        app.delete("/api/schedules/{scheduleId}", ctx -> {
            String scheduleIdStr = ctx.pathParam("scheduleId");
            int scheduleId;

            try {
                scheduleId = Integer.parseInt(scheduleIdStr);
            } catch (NumberFormatException e) {
                ctx.status(400).json(Map.of("error", "Invalid schedule ID format"));
                return;
            }

            // Find the schedule with the given ID
            Optional<Schedule> scheduleToRemove = student.getSchedules()
                    .stream()
                    .filter(schedule -> schedule.getScheduleID() == scheduleId)
                    .findFirst();

            if (scheduleToRemove.isPresent()) {
                // Remove the schedule from the student's schedules
                student.getSchedules().remove(scheduleToRemove.get());
                ctx.status(200).json(Map.of("message", "Schedule deleted successfully"));
            } else {
                ctx.status(404).json(Map.of("error", "Schedule not found"));
            }
        });


        //app.get("/api/search", ctx -> ctx.json(new Message("Hello from Javalin with Jackson!")));
        app.get("/api/search-courses", ctx -> {
            String searchTerm = ctx.queryParamAsClass("searchTerm", String.class).getOrDefault("").toLowerCase();
            int page = ctx.queryParamAsClass("page", Integer.class).getOrDefault(1);
            int limit = ctx.queryParamAsClass("limit", Integer.class).getOrDefault(10);
            int offset = (page - 1) * limit;

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



//            List<Course> courses_from_db = getCoursesFromDB(limit, offset);


            // Filter courses based on the search term and semester
            List<Course> filteredCourses = (semester != null && !semester.isEmpty())
                    ? allCoursesFromDB.stream().filter(course -> semester.equalsIgnoreCase(course.getSemester())).toList()
                    : allCoursesFromDB;

            // Filter courses based on the search term
//            List<Course> filteredCourses = courses;
            if (searchTerm != null && !searchTerm.trim().isEmpty()) {
                filteredCourses = filteredCourses.stream()
                        .filter(course -> course.getName().toLowerCase().contains(searchTerm.trim().toLowerCase()) || course.getFullCourseCode().trim().toLowerCase().contains(searchTerm.trim().toLowerCase()))
                        .toList();
            }
            if (code != "") {
                filteredCourses = filteredCourses.stream()

                        .filter(course -> course.getFullCourseCode().trim().toLowerCase().contains(code.trim().toLowerCase()))
                        .toList();
            }

            StringBuilder days = new StringBuilder();
            days.append(day1).append(" ").append(day2).append(" ").append(day3).append(" ").append(day4).append(" ").append(day5);
            String daysString = days.toString().trim().toLowerCase();
            if (daysString != "") {
                filteredCourses = filteredCourses.stream()

                        .filter(course -> daysString.contains(course.daysString().trim().toLowerCase()))
                        .toList();

            }
            if (startTime != "") {
                filteredCourses = filteredCourses.stream()
                        .filter(course -> course.getTimes().stream()
                                .anyMatch(timeBlock -> timeBlock.getStartTime().compareTo((startTime + ":00").trim()) >= 0))
                        .toList();

            }
            if (endTime != "") {
                filteredCourses = filteredCourses.stream()
                        .filter(course -> course.getTimes().stream()
                                .anyMatch(timeBlock -> timeBlock.getEndTime().compareTo((endTime + ":00").trim()) <= 0))
                        .toList();
            }
            if (hideFullCourses) {
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

        }



    }
    public static List<Course> getCoursesFromDB(int limit, int offset) {
        List<Course> courseList = new ArrayList<>();
        String url = "jdbc:sqlite:database.db";
        String courseSql = "SELECT * FROM courses LIMIT ? OFFSET ?";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement courseStmt = conn.prepareStatement(courseSql)) {

            courseStmt.setInt(1, limit);
            courseStmt.setInt(2, offset);

            var rs = courseStmt.executeQuery();

            while (rs.next()) {
                Course c = new Course();
                int courseId = rs.getInt("id"); // Get the course ID for related data

                c.setName(rs.getString("name"));
                c.setLocation(rs.getString("location"));
                c.setSection(rs.getString("section"));
                c.setSemester(rs.getString("semester"));
                c.setCourseNum(rs.getInt("courseNum"));
                c.setNumCredits(rs.getInt("numCredits"));
                c.setIs_lab(rs.getBoolean("is_lab"));
                c.setSubjCode(rs.getString("subjCode"));
                c.setTotalSeats(rs.getInt("totalSeats"));

                // Get times for this course
                String timesSql = "SELECT * FROM course_times WHERE course_id = ?";
                try (PreparedStatement timeStmt = conn.prepareStatement(timesSql)) {
                    timeStmt.setInt(1, courseId);
                    var timesRs = timeStmt.executeQuery();

                    List<timeBlock> times = new ArrayList<>();
                    while (timesRs.next()) {
                        timeBlock time = new timeBlock();
                        time.setDay(timesRs.getString("day"));
                        time.setStartTime(timesRs.getString("start_time"));
                        time.setEndTime(timesRs.getString("end_time"));
                        times.add(time);
                    }
                    c.setTimes(times);
                }

                // Get faculty for this course
                String facultySql = "SELECT * FROM course_faculty WHERE course_id = ?";
                try (PreparedStatement facultyStmt = conn.prepareStatement(facultySql)) {
                    facultyStmt.setInt(1, courseId);
                    var facultyRs = facultyStmt.executeQuery();

                    List<String> faculty = new ArrayList<>();
                    while (facultyRs.next()) {
                        faculty.add(facultyRs.getString("faculty_name"));
                    }
                    c.setFaculty(faculty);
                }

                courseList.add(c);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return courseList;
    }
    public static int getTotalCoursesCount() {
        String url = "jdbc:sqlite:database.db";
        String sql = "SELECT COUNT(*) AS total FROM courses";


        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement();
             var rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt("total");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    // Data class for JSON response - frontend testing
    public static class Message {
        public String message;

        public Message(String message) {
            this.message = message;
        }
    }
}


