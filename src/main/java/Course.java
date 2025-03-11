public class Course {

    private int numCredits;
    private String[] faculty;
    private boolean is_lab;
    private boolean is_open;
    private String location;
    private String name;
    // Three digit, 101, 220, etc.....
    private int courseNum;
    private int openSeats;
    //Like A, or B
    private String section;
    private String semester;
    //Like ACCT
    private String subjCode;

//constructor need day (M, T, W...), then end_time (string in military time: Hour, min, sec), then start_time (in military time)
    //three vars for timeBlock constructor
    private timeBlock times;

    public Course(int numCredits, String[] faculty, boolean is_lab, boolean is_open, String location, String name, int courseNum, int openSeats, String section, String semester, String subjCode, timeBlock times) {
//        {"credits":3,"faculty":["Graybill, Keith B."],"is_lab":false,"is_open":true,
//        "location":"SHAL 316","name":"PRINCIPLES OF ACCOUNTING I","number":201,
//        "open_seats":1,"section":"A","semester":"2023_Fall","subject":"ACCT",
//        "times":[{"day":"T","end_time":"16:45:00","start_time":"15:30:00"}
        this.numCredits = numCredits;
        this.faculty = faculty;
        this.is_lab = is_lab;
        this.is_open = is_open;
        this.location = location;
        this.name = name;
        this.courseNum = courseNum;
        this.openSeats = openSeats;
        this.section = section;
        this.semester = semester;
        this.subjCode = subjCode;
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

    public timeBlock getTimes() {
        return times;
    }
    
}
