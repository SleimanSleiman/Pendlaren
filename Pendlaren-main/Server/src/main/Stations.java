package main;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Stations {
    private static final Map<String, String> stationMap = new HashMap<>();
    private static final String API_KEY = ConfigAPI.resrobotKey;

    public List<StationInformation> searchStations(String input){
        try{
            List<StationInformation> stations = new ArrayList<>();
            String fuzzySearch = input.trim() + "?";
            String urlString = "https://api.resrobot.se/v2.1/location.name?input=" + fuzzySearch
                    + "&format=json&accessId=" + API_KEY;
            String response = getApi(urlString);

            //Attempt to fix java.io.Reader#lock error
            response = response.replace("xmlns=\"hafas_rest_v1\"", "");

            JSONObject xmlToJson = XML.toJSONObject(response);
            JSONObject locationlist = xmlToJson.getJSONObject("LocationList");
            JSONArray stopLocations = locationlist.getJSONArray("StopLocation");

            for (int i = 0; i < stopLocations.length(); i++){
                JSONObject station = stopLocations.getJSONObject(i);
                String ID = station.getString("id");
                String name = station.getString("name");
                double lon = station.getLong("lon");
                double lat = station.getLong("lat");
                int weight = station.getInt("weight");
                int products = station.getInt("products");
                StationInformation info = new StationInformation(ID, name, lon, lat, weight, products);
                stations.add(info);
            }

            return stations;

        }catch (Exception e){
            e.printStackTrace();
            return null;
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

    public static String getStationId(String stationName) {
        return stationMap.get(stationName);
    }
}