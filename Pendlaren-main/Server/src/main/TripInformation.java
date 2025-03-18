package main;

public class TripInformation {
    public int connection;
    public String departureFrom;
    public String departureTime;
    public String arrivalAt;
    public String arrivalTime;
    public String travelTime;
    public TripInformation(int connection, String departureFrom, String departureTime, String arrivalAt, String arrivalTime, String travelTime) {
        this.connection = connection;
        this.departureFrom = departureFrom;
        this.departureTime = departureTime;
        this.arrivalAt = arrivalAt;
        this.arrivalTime = arrivalTime;
        this.travelTime = travelTime;
    }
}
