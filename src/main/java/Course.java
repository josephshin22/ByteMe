import com.fasterxml.jackson.annotation.JsonProperty;

public class Course {

    @JsonProperty("name")
    private String name;

    @JsonProperty("number")
    private int referenceNumber;

    @JsonProperty("subject")
    private String abbreviation;

    @JsonProperty("faculty")
    private String professor;

    @JsonProperty("location")
    private String location;

    @JsonProperty("credits")
    private int numCredits;

    // currently don't have info for this
    private String description;

    @JsonProperty("times")
    private timeBlock[] timeBlocks;

    public Course(String name, int referenceNumber, String abbreviation, String professor, String location,
            int numCredits, String description, timeBlock[] timeBlocks) {
        this.name = name;
        this.referenceNumber = referenceNumber;
        this.abbreviation = abbreviation;
        this.professor = professor;
        this.location = location;
        this.numCredits = numCredits;
        this.description = description;
        this.timeBlocks = timeBlocks;
    }

}
