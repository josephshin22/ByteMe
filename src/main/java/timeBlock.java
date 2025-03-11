public class timeBlock {


    private String day;
    private String endTime;
    private String startTime;
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
