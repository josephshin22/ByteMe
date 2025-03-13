public class Student {

    private String username;
    private int studentID;
    private String password;
    private String[] major;
    private String[] minor;
    private String gradYear;
    private String gradMonth;
    private Course[] takenCourses;
    private Schedule[] schedules;

    public Student() {
    }

    public boolean verifyPassword(String passwordAttempt) {
        return passwordAttempt.equals(password);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword(String password) {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
