import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneOffset;

@JsonIgnoreProperties(ignoreUnknown = true)  // ✅ Ignore unknown properties like "is_lab"
public class Course {

    @JsonProperty("name")
    private String name;

    @JsonProperty("number")
    private int courseNum;
    //Like ACCT
    @JsonProperty("subject")
    private String abbreviation;

    @JsonProperty("faculty")
    private String[] faculty;

    @JsonProperty("location")
    private String location;

    @JsonProperty("credits")
    private int numCredits;

    // currently don't have info for this
//    private String description;

    @JsonProperty("times")
    //constructor need day (M, T, W...), then end_time (string in military time: Hour, min, sec), then start_time (in military time)
    //three vars for timeBlock constructor
    private List<timeBlock> times; // Change to List

    private boolean is_lab;
    private boolean is_open;

    // Three digit, 101, 220, etc.....
//    private int courseNum;
    private int openSeats;

    private int totalSeats;

    //Like A, or B
    private String section;

//    @JsonProperty("semester")
    private String semester;



    public Course() {
    }

    public Course(String name, int courseNum, String abbreviation, String section, String[] faculty, String location,
                  int numCredits, String description, timeBlock[] timeBlocks, boolean is_lab, boolean is_open, int openSeats, int totalSeats, String semester ) {
        this.name = name;
        this.courseNum = courseNum;
        this.abbreviation = abbreviation;
        this.section = section;
        this.faculty = faculty;
        this.location = location;
        this.numCredits = numCredits;
        this.is_open = is_open;
        this.is_lab = is_lab;
        this.totalSeats = totalSeats;
//        this.description = description;
        this.times = List.of(timeBlocks);
        this.openSeats = openSeats;

        this.semester = semester;
    }


    public int getNumCredits() {
        return numCredits;
    }

    public String[] getFaculty() {
        return faculty;
    }

    public boolean getIs_lab() {
        return is_lab;
    }

    public boolean getIs_open() {
        return is_open;
    }

    public String getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }

    public int getCourseNum() {
        return courseNum;
    }

    public int getOpenSeats() {
        return openSeats;
    }

    public String getSection() {
        return section;
    }

    public String getSemester() {
        return semester;
    }

    public String getSubjCode() {
        return abbreviation;
    }
    public int getTotalSeats() {
        return totalSeats;
    }

    public List<timeBlock> getTimes() {
        return times;
    }


    @Override
    public String toString(){
        return "Course: " + name + "\nCourse Number: " + courseNum + "\nAbbrevation: " + abbreviation + "\nLocation: " + location + "\nCredits: " + numCredits + "\nTimes: " + times;
    }

    public boolean hasConflict(Course c2) {
        for(timeBlock t1 : this.times) {
            ZonedDateTime start1 = convertToUTC(t1.getStartTime());
            ZonedDateTime end1 = convertToUTC(t1.getEndTime());

            for(timeBlock t2 : c2.getTimes()) {
                ZonedDateTime start2 = convertToUTC(t2.getStartTime());
                ZonedDateTime end2 = convertToUTC(t2.getEndTime());

                if(t1.getDay().equals(t2.getDay())) {
                    if(!(end1.isBefore(start2) || end2.isBefore(start1))) {
                        return true; // conflict
                    }
                }
            }
        }
        return false; // no conflict
    }

    private ZonedDateTime convertToUTC(String timeString) {
        // convert string in HH:mm:ss format to UTC
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        // parse the time as a ZonedDateTime in UTC
        ZonedDateTime utcDateTime = ZonedDateTime.of(
                1970, 1, 1,   // placeholder date (doesn't matter)
                Integer.parseInt(timeString.substring(0, 2)), // hour digits
                Integer.parseInt(timeString.substring(3, 5)), // minute digits
                Integer.parseInt(timeString.substring(6, 8)), // second digits
                0, ZoneOffset.UTC);  // UTC zone offset

        return utcDateTime;
    }
    public void showCourse() {
        System.out.print(getSubjCode() + " " + getCourseNum() + " " + getSection());
        System.out.print("      " + getName());
        System.out.print("      " + (getFaculty() != null ? String.join(", ", getFaculty()) : "N/A"));
        System.out.print("    " + getOpenSeats() + "/" + getTotalSeats() + "  ");
        System.out.print("   status: " + (getIs_open() ? "Open" : "Full")+"\n");

        for (timeBlock timeBlock : getTimes()) {
            System.out.println("\tDay: " + timeBlock.getDay());
            System.out.println("\tStart Time: " + timeBlock.getStartTime());
            System.out.println("\tEnd Time: " + timeBlock.getEndTime());
            System.out.println("\t---------------------");
        }
        System.out.println("Location: " + getLocation());
        System.out.println("Credits: " + getNumCredits());
        System.out.println("Semester: " + getSemester());
        System.out.println("Lab: " + (getIs_lab() ? "Yes" : "No"));
        System.out.println("");
        System.out.println("");
        System.out.println("");
    }
    public String fullCourseCode(){
        return getSubjCode() + " " + getCourseNum() + " " + getSection();
    }

}





