package main;
 
import com.google.gson.*;
import io.javalin.*;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Javalin app = Javalin.create(config -> {
            config.bundledPlugins.enableCors(corsConfig -> {
                corsConfig.addRule(rule -> {
                    rule.anyHost();
                });
            });
        }).start(7123);
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

        app.get("/", ctx -> {
            ctx.result("Hello from Javalin! /trip to generate trip, /music to generate music");
        });

        app.get("/search", ctx -> {
            try {
                String input = ctx.queryParam("input");

                if (input == null || input.trim().isEmpty()){
                    ctx.status(400).result("Empty search param");
                    return;
                }
                Stations stations = new Stations();
                List<StationInformation> resultHits = stations.searchStations(input);

                if (resultHits == null){
                    ctx.status(400).result("Invalid searchinput");
                    return;
                }

                ctx.contentType("application/json");
                ctx.result(gson.toJson(resultHits));
            }catch (Exception e){
                ctx.status(500).result("Error detching stations: " + e.getMessage());
            }
        });

//        String fromStationId = "740000120"; // Id för lund C
//        String toStationId = "740000003"; // Id för Malmö C
        app.get("/trip", ctx -> {
            try {
                String fromStationId = ctx.queryParam("fromStationId");
                String toStationId = ctx.queryParam("toStationId");
                String departureTime = ctx.queryParam("departureTime");
                System.out.println(departureTime);
                Trip trip = new Trip(fromStationId, toStationId, departureTime);
                trip.getTrip();
                ArrayList<TripInformation> tripInformation = trip.getTripInformation();
                if (tripInformation.size() == 0){
                    ctx.status(400).result("Inga resor hittades mellan valda hållplatser");
                }
                ctx.contentType("application/json");
                ctx.result(gson.toJson(tripInformation));
            }catch (Exception e){
                ctx.status(500).result("Error fetching trips: " + e.getMessage());
            }
        });

        Music music = new Music();
        app.get("/music", ctx -> {
            try {
                System.out.println("trying to get music");
                //Test
                String travelTimeString = ctx.queryParam("travelTime");
                int travelTime = (travelTimeString != null) ? Integer.parseInt(travelTimeString) : 0;
                int songAmount = music.getSongAmount(travelTime);
                JSONArray playlist = music.getPlaylist(songAmount);
                music.send(playlist);
                ArrayList<MusicInformation> musicInformations = music.getMusicInformationList();
                ctx.contentType("application/json");
                ctx.result(gson.toJson(musicInformations));
            } catch (Exception e) {
                ctx.status(500).result("Error fetching music: " + e.getMessage());
            }
        });

    }
}
