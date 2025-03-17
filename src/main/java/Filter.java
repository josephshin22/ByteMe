import java.util.ArrayList;

public class Filter {

//    boolean required;
//    boolean completed;

    String courseCode;
    boolean full;
    ArrayList<String> weekDays;
    int startTime;
    int endTime;
    int startTimeExtra;
    int endTimeExtra;
    int credits;
    public Filter()
    {

    }

    //setters for all variables
//    public void setRequired(boolean required) {
//        this.required = required;
//    }
//
//    public void setCompleted(boolean completed) {
//        this.completed = completed;
//    }
    //setter for courseCode

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }
    public void setFull(boolean full) {
        this.full = full;
    }

    public void setWeekDays(ArrayList<String> weekDays) {

        this.weekDays = weekDays;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }
    public void setStartTimeExtra(int startTimeExtra) {
        this.startTimeExtra = startTimeExtra;
    }
    public void setEndTimeExtra(int endTimeExtra) {
        this.endTimeExtra = endTimeExtra;
    }
    public void setCredits(int credits) {
        this.credits = credits;
    }

    //getters for all variables
//    public boolean getRequired() {
//        return required;
//    }
//
//    public boolean getCompleted() {
//        return completed;
//    }

    public boolean getFull() {
        return full;
    }

    public ArrayList<String> getWeekDays() {
        return weekDays;
    }

    public int getStartTime() {
        return startTime;
    }

    public int getEndTime() {
        return endTime;
    }
    public int getStartTimeExtra() {
        return startTimeExtra;
    }
    public int getEndTimeExtra() {
        return endTimeExtra;
    }
    public String getCourseCode() {
        return courseCode;
    }
    public int getCredits() {
        return credits;
    }
    //create a filter toString

     @Override
     public String toString() {

            return "Filter: " +
                    "courseCode: " + (courseCode != null ? courseCode : "") + '\'' +
                    ", is full: " + (full ? full : "") +
                    ", weekDays: " + (weekDays != null ? weekDays : "") +
                    ", startTime: " + (startTime != 0 ? startTime : "") +
                    ", endTime: " + (endTime != 0 ? endTime : "") +

                    ", credits: " + (credits != 0 ? credits : "");

     }
}
