package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;

public class Music {

    private ArrayList<MusicInformation> musicInformationList;

    public Music(){
        musicInformationList = new ArrayList<>();
    }

    public void send(JSONArray playlist) {
        clearMusicList();
        for (int i = 0; i < playlist.length(); i++){
            JSONObject songlink = playlist.getJSONObject(i);

            //TODO: Replace with frontendcall
            System.out.println("Song nr: " + (i+1));
            System.out.println("Name: " + songlink.getString("track"));
            System.out.println("Band: " + songlink.getString("band"));
            System.out.println("Genre: " + songlink.getString("genre"));
            System.out.println(songlink.getString("tracklink"));
            System.out.println("Datum: " + songlink.getString("date"));
            System.out.println();
            MusicInformation musicInformation = new MusicInformation("Song nr: " + (i+1),"Name: " + songlink.getString("track"),
                    "Band: " + songlink.getString("band"), "Genre: " + songlink.getString("genre"),
                    songlink.getString("tracklink"), "Datum: " + songlink.getString("date"));
            musicInformationList.add(musicInformation);
        }
    }

    JSONArray getSong() {
        try {
            URL url = new URL("https://www.xn--demoltar-e0a.se/api/getRandomTrack.php?q=");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            // Check response code
            int statusCode = con.getResponseCode();
            if (statusCode != HttpURLConnection.HTTP_OK) {
                System.out.println("Error: Received non-OK HTTP response code " + statusCode);
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder playlistBuilder = new StringBuilder();
            String song;
            while ((song = reader.readLine()) != null) {
                playlistBuilder.append(song);
            }

            // Parse response as a single object
            JSONObject jsonResponse = new JSONObject(playlistBuilder.toString());

            // Wrap the object into a JSONArray
            JSONArray playlist = new JSONArray();
            playlist.put(jsonResponse);
            return playlist;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public JSONArray getPlaylist(int songAmount) {
        JSONArray combinedTracks = new JSONArray();

        for (int i = 0; i < songAmount; i++) {
            JSONArray singleTrack = getSong();
            combinedTracks.put(singleTrack.getJSONObject(0));
        }

        return combinedTracks;
    }

    public int getSongAmount(int travelTime) {
        return (int) (travelTime / 3.5); //Assumes each song is on average 3.5 min long
    }

    public ArrayList<MusicInformation> getMusicInformationList() {
        return musicInformationList;
    }

    public void clearMusicList() {
        musicInformationList.clear();
    }
}
