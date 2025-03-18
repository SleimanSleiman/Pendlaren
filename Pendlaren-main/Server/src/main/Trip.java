package main;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;

public class Trip {

    private static final String API_KEY = ConfigAPI.resrobotKey;
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
    private String fromStationID;
    private String toStationID;
    private String date;
    private String time;
    private ArrayList<TripInformation> tripInformation;

    public Trip(String fromStationID, String toStationID, String departureTime){
        this.fromStationID = fromStationID;
        this.toStationID = toStationID;
        LocalDateTime dateTime = LocalDateTime.parse(departureTime);
        this.date = dateTime.format(DateTimeFormatter.ISO_DATE);
        this.time = dateTime.format(DateTimeFormatter.ISO_TIME);
        tripInformation = new ArrayList<>();
    }

    public void getTrip() {
        try {
            clearTripInformation();
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

            String urlString = "https://api.resrobot.se/v2.1/trip?originId=" + fromStationID
                    + "&destId=" + toStationID
                    +"&date=" + date
                    +"&time=" + time
                    + "&format=json&accessId=" + API_KEY;

            String response = getApi(urlString);
            JSONObject jsonResponse = new JSONObject(response);

            JSONArray tripArray = jsonResponse.getJSONArray("Trip");
            int connectionCount = 1;

            LocalTime userTime = LocalTime.parse(time, TIME_FORMATTER);
            for (int i = 0; i < tripArray.length(); i++) {
                JSONObject trip = tripArray.getJSONObject(i);

                JSONObject legList = trip.getJSONObject("LegList");
                JSONArray legs = legList.getJSONArray("Leg");

                JSONObject firstLeg = legs.getJSONObject(0);
                JSONObject lastLeg = legs.getJSONObject(legs.length() - 1);

                String departureTime = firstLeg.getJSONObject("Origin").getString("time");
                String arrivalTime = lastLeg.getJSONObject("Destination").getString("time");

                LocalTime departureTimeObj = LocalTime.parse(departureTime, TIME_FORMATTER);
                LocalTime arrivalTimeObj = LocalTime.parse(arrivalTime, TIME_FORMATTER);
                LocalTime tripTime = LocalTime.parse(departureTime, timeFormatter);

                if (tripTime.isAfter(userTime.minusMinutes(30)) &&
                        tripTime.isBefore(userTime.plusMinutes(30))){
                    System.out.println("Connection " + connectionCount + ":");
                    System.out.println("Departure from: " + firstLeg.getJSONObject("Origin").getString("name"));
                    System.out.println("Departure Time: " + departureTime);
                    System.out.println("Arrival at: " + lastLeg.getJSONObject("Destination").getString("name"));
                    System.out.println("Arrival Time: " + arrivalTime);
                    String travelTime = calculateTravelTime(departureTimeObj, arrivalTimeObj);
                    System.out.println("Travel Time: " + travelTime);
                    System.out.println();

                    TripInformation tripInformationObj = new TripInformation(
                            connectionCount,
                            "Departure from: " + firstLeg.getJSONObject("Origin").getString("name"),
                            "Departure Time: " + departureTime,
                            "Arrival at: " + lastLeg.getJSONObject("Destination").getString("name"),
                            "Arrival Time: " + arrivalTime,
                            travelTime);

                    tripInformation.add(tripInformationObj);
                    connectionCount++;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private String getApi(String urlString) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            return response.toString();
        }
    }


    public ArrayList<TripInformation> getTripInformation() {
        return tripInformation;
    }

    private String calculateTravelTime(LocalTime departureTime, LocalTime arrivalTime) {
        Duration duration = Duration.between(departureTime, arrivalTime);

        long hours = duration.toHours();
        long minutes = duration.toMinutes() % 60;

        return hours + " hours and " + minutes + " minutes";
    }

    private void clearTripInformation() {
        tripInformation.clear();
    }
}
