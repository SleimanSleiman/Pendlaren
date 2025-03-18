package main;

public class StationInformation {
    private String ID;
    private String name;
    private double lon;
    private double lat;
    private int weight;
    private int products;

    public StationInformation(String ID, String name, double lon, double lat, int weight, int products){
        this.ID = extractID(ID);
        this.name = name;
        this.lon = lon;
        this.lat = lat;
        this.weight = weight;
        this.products = products;
    }

    private String extractID(String ID) {
        int start = ID.indexOf("L=");
        start += 2;
        int end = ID.indexOf("@", start);
        return ID.substring(start, end);
    }
}
