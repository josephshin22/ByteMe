import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@JsonIgnoreProperties(ignoreUnknown = true)  // ✅ Ignore unknown properties like "is_lab"
public class Course {

    @JsonProperty("name")
    private String name;

    @JsonProperty("number")
    private int referenceNumber;

    @JsonProperty("subject")
    private String abbreviation;

    @JsonProperty("faculty")
    private String[] faculty;

    @JsonProperty("location")
    private String location;

    @JsonProperty("credits")
    private int numCredits;

    // currently don't have info for this
    private String description;

    @JsonProperty("times")
    //constructor need day (M, T, W...), then end_time (string in military time: Hour, min, sec), then start_time (in military time)
    //three vars for timeBlock constructor
    private List<timeBlock> times; // Change to List

    private boolean is_lab;
    private boolean is_open;

    // Three digit, 101, 220, etc.....
    private int courseNum;
    private int openSeats;
    //Like A, or B
    private String section;
    private String semester;
    //Like ACCT
    private String subjCode;

    public Course() {
    }

    public Course(String name, int referenceNumber, String abbreviation, String[] faculty, String location,
                  int numCredits, String description, timeBlock[] timeBlocks) {
        this.name = name;
        this.referenceNumber = referenceNumber;
        this.abbreviation = abbreviation;
        this.faculty = faculty;
        this.location = location;
        this.numCredits = numCredits;
        this.description = description;
        this.times = times;
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
        return subjCode;
    }

    public List<timeBlock> getTimes() {
        return times;
    }



}
