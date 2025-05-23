import java.util.ArrayList;

public class Filter {

//    boolean required;
//    boolean completed;
    //ex: HUMA 200 or HUMA 200 A
    String courseCode;
    boolean full;
    ArrayList<String> weekDays;
    String startTime;
    String endTime;
    int credits;
    String semester;
    String startTimeExtra;
    String endTimeExtra;



    public Filter()
    {
        full = true; // default to true
        credits = -1; // default to -1 (because 0 is a valid number of credits)
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
    public void setSemester(String semester) {
        this.semester = semester;
    }
    public void setWeekDays(ArrayList<String> weekDays) {
        this.weekDays = weekDays;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
    public void setStartTimeExtra(String startTimeExtra) {
        this.startTimeExtra = startTimeExtra;
    }
    public void setEndTimeExtra(String endTimeExtra) {
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
    public String getSemester() { return semester;}
    public ArrayList<String> getWeekDays() {
        return weekDays;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }
    public String getStartTimeExtra() {
        return startTimeExtra;
    }
    public String getEndTimeExtra() {
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
                    "\n1. courseCode: " + (courseCode != null ? courseCode : "")  +
                    "\n2. show full courses: " + (full) +
                    "\n3. weekDays: " + (weekDays != null ? weekDays : "") +
                    "\n4. minimumStartTime: " + (startTime != null ? startTime : "") +
                    "\n5. maximumEndTime: " + (endTime != null ? endTime : "") +
                    "\n6. credits: " + (credits != -1 ? credits : "") +
                    "\n7. semester: " + (semester == null ? "" : semester);

     }
}
