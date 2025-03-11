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

}
