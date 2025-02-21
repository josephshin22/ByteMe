public class Course {

    String name;
    int referenceNumber;
    String abbreviation;
    String professor;
    String location;
    int numCredits;
    String description;
    timeBlock[] timeblocks;

    public Course(String name, int referenceNumber, String abbreviation, String professor, String location, int numCredits, String description, timeBlock[] timeblocks) {
        this.name = name;
        this.referenceNumber = referenceNumber;
        this.abbreviation = abbreviation;
        this.professor = professor;
        this.location = location;
        this.numCredits = numCredits;
        this.description = description;
        this.timeblocks = timeblocks;
    }

}
