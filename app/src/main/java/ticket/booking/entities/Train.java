package ticket.booking.entities;

import java.util.List;
import java.util.Map;

public class Train {

    private String trainId;
    private String trainNumber;
    private String sourceStn;
    private String destStn;
    private List<List<Integer>> seatMatrix;
    private Map<String, String> stationTime;
    private List<String> stationList;

    public Train(){}

    public Train(String trainId, String trainNo, List<List<Integer>> seats, Map<String, String> stationTimes, List<String> stations){
        this.trainId = trainId;
        this.trainNumber = trainNo;
        this.seatMatrix = seats;
        this.stationTime = stationTimes;
        this.stationList = stations;
    }

    public List<String> getStations(){
        return stationList;
    }

    public List<List<Integer>> getSeats() {
        return seatMatrix;
    }

    public void setSeats(List<List<Integer>> seats){
        this.seatMatrix = seats;
    }

    public String getTrainId(){
        return trainId;
    }

    public Map<String, String> getStationTimes(){
        return stationTime;
    }

    public String getTrainNo(){
        return trainNumber;
    }

    public void setTrainNo(String trainNo){
        this.trainNumber= trainNo;
    }

    public void setTrainId(String trainId){
        this.trainId = trainId;
    }

    public void setStationTimes(Map<String, String> stationTimes){
        this.stationTime = stationTimes;
    }

    public void setStations(List<String> stations){
        this.stationList = stations;
    }

    public String getTrainInfo(){
        return String.format("Train ID: %s Train No: %s", trainId, trainNumber);
    }
}
