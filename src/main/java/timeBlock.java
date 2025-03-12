import com.fasterxml.jackson.annotation.JsonProperty;
public class timeBlock {


    @JsonProperty("day")
    private String day;

    @JsonProperty("start_time")
    private String startTime;

    @JsonProperty("end_time")
    private String endTime;

    public timeBlock(String day, String endTime, String startTime) {
        this.day = day;
        this.endTime = endTime;
        this.startTime = startTime;
    }
    public String getDay(){
        return day;
    }
    public String getEndTime(){
        return endTime;
    }
    public String getStartTime(){
        return startTime;
    }

}
