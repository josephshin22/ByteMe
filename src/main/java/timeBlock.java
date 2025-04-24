import com.fasterxml.jackson.annotation.JsonProperty;

public class timeBlock {

    @JsonProperty("day")
    private String day;

    @JsonProperty("start_time")
    private String startTime;

    @JsonProperty("end_time")
    private String endTime;

    public timeBlock() {

    }

    public timeBlock(String day, String endTime, String startTime) {
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return day + ": " + startTime.substring(0, 5) + "-" + endTime.substring(0, 5);
    }


    public String getDay() {
        return day;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
