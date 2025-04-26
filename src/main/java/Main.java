import io.javalin.Javalin;
import io.javalin.json.JavalinJackson;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.JsonNode;

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
                    schedule_id INTEGER NOT NULL,
                    date_added TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    FOREIGN KEY(course_id) REFERENCES courses(id),
                    UNIQUE(course_id, schedule_id)
                );
            """;
            String createSchedulesSQL = """
                CREATE TABLE IF NOT EXISTS schedules (
                    schedule_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    semester TEXT,
                    studentID INTEGER,
                    name TEXT
                );
            """;
            stmt.execute(createCoursesSQL);
            stmt.execute(createTimesSQL);
            stmt.execute(createFacultySQL);
            stmt.execute(createUserCoursesSQL);
            System.out.println("Created user_courses table");
            stmt.execute(createSchedulesSQL);
            System.out.println("Created schedules table");

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


        //--------------------------------------------------------------------------------------
        } ///test


        // Frontend testing --------------------------------------------------------------------
        //  Configure Jackson ObjectMapper
        ObjectMapper objectMapper1 = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

        // Create Javalin app and set Jackson as the JSON mapper
        Javalin app = Javalin.create(config -> {
            config.jsonMapper(new JavalinJackson(objectMapper1));

            // Enable CORS for frontend access
            config.plugins.enableCors(cors -> {
                cors.add(it -> it.allowHost("http://localhost:5173"));
            });
        });
        System.out.println("Set up Javalin");

        //System.out.println("Total courses count: " + totalCoursesCount);
        //List<Course> allCoursesFromDB = getCoursesFromDB(totalCoursesCount, 0);
        System.out.println("Retrieved courses from DB");
        //System.out.println(allCoursesFromDB);

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
            if (semester == null) {
                semester = "";
            }
            ctx.json(getSchedulesFromDB(semester));
        });

        // ADD SCHEDULE
        app.post("/api/schedules", ctx -> {
            String semester = ctx.queryParam("semester");
            String name = "New Schedule";
            if (name == null || name.isEmpty() || semester == null || semester.isEmpty()) {
                ctx.status(400).json(Map.of("error", "Name and semester are required."));
                return;
            }

            int newScheduleId = addScheduleToDatabase(semester);

            if (newScheduleId != -1) {
                // Assuming you want to associate the new schedule ID with the student's in-memory representation
                Schedule newSchedule = new Schedule(student, new ArrayList<>(), name);
                newSchedule.setSemester(semester);
                ctx.status(201).json(Map.of("message", "Schedule created successfully", "scheduleId", newScheduleId));
            } else {
                ctx.status(500).json(Map.of("error", "Failed to create schedule in the database"));
            }
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

            boolean deletedFromDb = removeScheduleFromDatabase(scheduleId);

            if (deletedFromDb) {
                // Find and remove the schedule from the in-memory student object
                student.getSchedules().removeIf(schedule -> schedule.getScheduleID() == scheduleId);
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

//            List<Course> courses_from_db = getCoursesFromDB(limit, offset);
            int totalCoursesCount = getTotalCoursesCount();
            List<Course> allCoursesFromDB = getCoursesFromDB(totalCoursesCount, 0);

            // Filter courses based on the search term and semester
            List<Course> filteredCourses = (semester != null && !semester.isEmpty())
                    ? allCoursesFromDB.stream().filter(course -> semester.equalsIgnoreCase(course.getSemester())).toList()
                    : allCoursesFromDB;
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

        // Add course to user's saved courses
        app.post("/api/user-courses", ctx -> {
            // Extract userId from query parameters or use default
            int userId = ctx.queryParamAsClass("userId", Integer.class).getOrDefault(student.getId());

            // Extract courseId from query parameters
            int courseId = ctx.queryParamAsClass("courseId", Integer.class).get();

            // Extract scheduleId from query parameters and convert to integer
            String scheduleIdStr = ctx.queryParam("scheduleId");  // Get scheduleId from query parameter
            int scheduleId;

            // Ensure scheduleId is a valid integer
            try {
                scheduleId = Integer.parseInt(scheduleIdStr);  // Convert to integer
            } catch (NumberFormatException e) {
                ctx.status(400).json(Map.of("error", "Valid scheduleId is required"));
                return;
            }

            // Validate courseId and scheduleId
            if (courseId <= 0) {
                ctx.status(400).json(Map.of("error", "Valid courseId is required"));
                return;
            }
            if (scheduleId <= 0) {  // Ensure scheduleId is a positive integer
                ctx.status(400).json(Map.of("error", "Valid scheduleId is required"));
                return;
            }

            // Try to add the user-course association
            try {
                addUserCourse(userId, courseId, scheduleId);
                ctx.status(201).json(Map.of("message", "Course added successfully"));
            } catch (Exception e) {
                ctx.status(500).json(Map.of("error", "Failed to add course: " + e.getMessage()));
            }
        });


        // Remove course from user's saved courses
        app.delete("/api/user-courses", ctx -> {
            int userId = ctx.queryParamAsClass("userId", Integer.class).getOrDefault(student.getId());
            int courseId = ctx.queryParamAsClass("courseId", Integer.class).get();

            if (courseId <= 0) {
                ctx.status(400).json(Map.of("error", "Valid courseId is required"));
                return;
            }

            try {
                removeUserCourse(userId, courseId);
                ctx.status(200).json(Map.of("message", "Course removed successfully"));
            } catch (Exception e) {
                ctx.status(500).json(Map.of("error", "Failed to remove course: " + e.getMessage()));
            }
        });

// Get user's saved courses
        app.get("/api/user-courses", ctx -> {
            int scheduleId = ctx.queryParamAsClass("scheduleId", Integer.class).getOrDefault(-1);
            int userId = ctx.queryParamAsClass("userId", Integer.class).getOrDefault(student.getId());

            try {
                List<Course> userCourses = getUserCourses(userId);
                ctx.json(Map.of(
                        "courses", userCourses,
                        "totalCourses", userCourses.size()
                ));
            } catch (Exception e) {
                ctx.status(500).json(Map.of("error", "Failed to retrieve courses: " + e.getMessage()));
            }
        });
        app.start(7000);


    }

    public static List<Schedule> getSchedulesFromDB(String semester) throws SQLException {

        String url = "jdbc:sqlite:database.db";
        List<Schedule> schedules = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(url)) {
            // 1. Load all courses
            String scheduleSql = "";
            if (!semester.isEmpty()) {
                scheduleSql = "SELECT * FROM schedules WHERE semester = ?";
            } else {
                scheduleSql = "SELECT * FROM schedules";
            }
            try (PreparedStatement schedulestmt = conn.prepareStatement(scheduleSql)) {
                if (!semester.isEmpty()) {
                    schedulestmt.setString(1, semester);
                }
                var rs = schedulestmt.executeQuery();
                while (rs.next()) {
                    Schedule c = new Schedule();
                    c.setSemester(rs.getString("semester"));
                    c.setScheduleID(rs.getInt("schedule_id"));
                    c.setStudentID(rs.getInt("studentID"));
                    c.setName(rs.getString("name"));
                    schedules.add(c);
                }
            }
        } catch (Exception e) {

        }
        return schedules;
    }
    public static List<Course> getCoursesFromDB(int limit, int offset) {
        Map<Integer, Course> courseMap = new HashMap<>();
        String url = "jdbc:sqlite:database.db";

        try (Connection conn = DriverManager.getConnection(url)) {
            // 1. Load all courses
            String courseSql = "SELECT * FROM courses LIMIT ? OFFSET ?";
            try (PreparedStatement courseStmt = conn.prepareStatement(courseSql)) {
                courseStmt.setInt(1, limit);
                courseStmt.setInt(2, offset);
                var rs = courseStmt.executeQuery();
                while (rs.next()) {
                    Course c = new Course();
                    int courseId = rs.getInt("id");
                    c.setId(courseId);
                    c.setName(rs.getString("name"));
                    c.setLocation(rs.getString("location"));
                    c.setSection(rs.getString("section"));
                    c.setSemester(rs.getString("semester"));
                    c.setCourseNum(rs.getInt("courseNum"));
                    c.setNumCredits(rs.getInt("numCredits"));
                    c.setIs_lab(rs.getBoolean("is_lab"));
                    c.setSubjCode(rs.getString("subjCode"));
                    c.setTotalSeats(rs.getInt("totalSeats"));

                    courseMap.put(courseId, c);
                }
            }

            if (courseMap.isEmpty()) return new ArrayList<>();

            // Helper to create IN clause: (?, ?, ?, ...)
            String inClause = courseMap.keySet().stream()
                    .map(id -> "?")
                    .collect(Collectors.joining(", ", "(", ")"));

            List<Integer> courseIds = new ArrayList<>(courseMap.keySet());

            // 2. Load all course_times
            String timesSql = "SELECT * FROM course_times WHERE course_id IN " + inClause;
            try (PreparedStatement timeStmt = conn.prepareStatement(timesSql)) {
                for (int i = 0; i < courseIds.size(); i++) {
                    timeStmt.setInt(i + 1, courseIds.get(i));
                }

                var timesRs = timeStmt.executeQuery();
                while (timesRs.next()) {
                    int courseId = timesRs.getInt("course_id");
                    timeBlock tb = new timeBlock();
                    tb.setDay(timesRs.getString("day"));
                    tb.setStartTime(timesRs.getString("start_time"));
                    tb.setEndTime(timesRs.getString("end_time"));

                    Course course = courseMap.get(courseId);
                    if (course.getTimes() == null) course.setTimes(new ArrayList<>());
                    course.getTimes().add(tb);
                }
            }

            // 3. Load all course_faculty
            String facultySql = "SELECT * FROM course_faculty WHERE course_id IN " + inClause;
            try (PreparedStatement facultyStmt = conn.prepareStatement(facultySql)) {
                for (int i = 0; i < courseIds.size(); i++) {
                    facultyStmt.setInt(i + 1, courseIds.get(i));
                }

                var facultyRs = facultyStmt.executeQuery();
                Map<Integer, List<String>> facultyMap = new HashMap<>();

                while (facultyRs.next()) {
                    int courseId = facultyRs.getInt("course_id");
                    String facultyName = facultyRs.getString("faculty_name");

                    facultyMap.computeIfAbsent(courseId, k -> new ArrayList<>()).add(facultyName);
                }

                for (Map.Entry<Integer, List<String>> entry : facultyMap.entrySet()) {
                    Course course = courseMap.get(entry.getKey());
                    course.setFaculty(List.of(entry.getValue().toArray(new String[0])));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Return in original order
        List<Course> courseList = new ArrayList<>();
        courseList.addAll(courseMap.values());
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
    // Helper methods for database operations
    private static void addUserCourse(int userId, int courseId, int scheduleId) {
        String url = "jdbc:sqlite:database.db";
        // Update the SQL query to include scheduleId
        String sql = "INSERT INTO user_courses (user_id, course_id, schedule_id) VALUES (?, ?, ?)";
        System.out.println("Called addUserCourse function");

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setInt(2, courseId);
            stmt.setInt(3, scheduleId);  // Set scheduleId as an integer

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Failed to add course relationship");
            }

            // If using in-memory model in addition to database
            if (userId == student.getId()) {
                // Find the course in the global courses list
                Course courseToAdd = courses.stream()
                        .filter(c -> getCourseDbId(c) == courseId)
                        .findFirst()
                        .orElse(null);

                if (courseToAdd != null) {
                    student.addSavedCourse(courseToAdd);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error adding user course: " + e.getMessage());
            throw new RuntimeException("Database error: " + e.getMessage(), e);
        }
    }


    private static void removeUserCourse(int userId, int courseId) {
        String url = "jdbc:sqlite:database.db";
        String sql = "DELETE FROM user_courses WHERE user_id = ? AND course_id = ?";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setInt(2, courseId);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                // No rows were deleted, relationship might not exist
                System.out.println("No course relationship found to remove");
            }


        } catch (SQLException e) {
            System.out.println("Error removing user course: " + e.getMessage());
            throw new RuntimeException("Database error: " + e.getMessage(), e);
        }
    }

    private static List<Course> getUserCourses(int scheduleId) {
        List<Course> courseList = new ArrayList<>();
        String url = "jdbc:sqlite:database.db";

        String sql = """
        SELECT c.* FROM courses c
        JOIN user_courses uc ON c.id = uc.course_id
        WHERE uc.schedule_id = ?
    """;

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, scheduleId);
            var rs = stmt.executeQuery();

            while (rs.next()) {
                Course c = new Course();
                int courseId = rs.getInt("id");

                c.setId(courseId);
                c.setName(rs.getString( "name"));
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
            System.out.println("Error getting user courses: " + e.getMessage());
            throw new RuntimeException("Database error: " + e.getMessage(), e);
        }

        return courseList;
    }

    // Helper method for database operations
    private static int addScheduleToDatabase(String semester) {
        String url = "jdbc:sqlite:database.db";
        String sql = "INSERT INTO schedules (semester, studentID, name) VALUES (?, ?, ?)"; // Add semester column
        System.out.println("Called addScheduleToDatabase with semester function");
        int generatedId = -1;

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, semester); // Set the semester value
            stmt.setInt(2, 123456); // Set the student ID
            stmt.setString(3, "New Schedule"); // Set the schedule name
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Failed to add new schedule with semester");
            }

            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                generatedId = generatedKeys.getInt(1);
            } else {
                throw new SQLException("Failed to retrieve generated schedule ID");
            }

        } catch (SQLException e) {
            System.out.println("Error adding schedule with semester to database: " + e.getMessage());
            throw new RuntimeException("Database error: " + e.getMessage(), e);
        }
        return generatedId;
    }

    // Helper method for database operations
    private static boolean removeScheduleFromDatabase(int scheduleId) {
        String url = "jdbc:sqlite:database.db";
        String sql = "DELETE FROM schedules WHERE schedule_id = ?";
        System.out.println("Called removeScheduleFromDatabase function for ID: " + scheduleId);

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, scheduleId); // Set the schedule ID to delete
            stmt.executeUpdate();
            return true; // Returns true if at least one row was deleted
        } catch (SQLException e) {
            System.err.println("Error removing schedule from database: " + e.getMessage());
            throw new RuntimeException("Database error: Failed to remove schedule with ID: " + scheduleId, e);
        }
    }
    // Helper method to get course DB ID
    private static int getCourseDbId(Course course) {
        String url = "jdbc:sqlite:database.db";
        String sql = "SELECT id FROM courses WHERE subjCode = ? AND courseNum = ? AND section = ? AND semester = ?";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, course.getSubjCode());
            stmt.setInt(2, course.getCourseNum());
            stmt.setString(3, course.getSection());
            stmt.setString(4, course.getSemester());

            var rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }

        } catch (SQLException e) {
            System.out.println("Error getting course ID: " + e.getMessage());
        }

        return -1; // Course not found
    }
    // Data class for JSON response - frontend testing
    public static class Message {
        public String message;

        public Message(String message) {
            this.message = message;
        }
    }
}


