import java.util.*;

public class Event {
    private String eventName;
    private String semester;
    private List<timeBlock> times;


    public Event(){
        this.eventName = "";
        this.times = new ArrayList<timeBlock>();
        this.semester = "";
    }

    public void setEventName(String eventName) {this.eventName = eventName;}
    public String getEventName() {return eventName;}
    public void setSemester(String semester){this.semester = semester;}
    public String getSemester(){return semester;}
    public void setTimes(){this.times = new ArrayList<timeBlock>();}
    public List<timeBlock> getTimes(){return times;}
}
