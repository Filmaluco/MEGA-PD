package Helpers;

import Models.ServerData;
import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public final class ServerManager {

    private final static String API_URL_ALL = "https://api.filmaluco.cloud/servers";
    private final static String API_URL_ACTIVE = "https://api.filmaluco.cloud/servers?status=active";
    private final static String API_URL_INACTIVE = "https://api.filmaluco.cloud/servers?status=inactive";
    private final static String API_URL_SERVER = "https://api.filmaluco.cloud/server?id=";

    /**
     * Loads all servers depending on the active param
     * @param active true = all active servers
     * @return Servers
     */
    public static ArrayList<ServerData> loadServers(boolean active){
        String requestedURL = active ? API_URL_ACTIVE : API_URL_INACTIVE;
        return loadServerArray(requestedURL);
    }

    /**
     *  Loads all servers
     * @return Servers
     */
    public static ArrayList<ServerData> loadServers(){
        return loadServerArray(API_URL_ALL);
    }

    /**
     * Loads the first active server
     * @return ServerData
     */
    public static ServerData loadServer(){
        ArrayList<ServerData> servers = loadServerArray(API_URL_ACTIVE);
        if(servers == null) return null;
        return servers.get(0);
    }


    private static ArrayList<ServerData> loadServerArray(String requestedURL) {
        ArrayList<ServerData> servers = new ArrayList<>();
        URL url;
        try {
            //Connects to the API
            url = new URL(requestedURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //Loads Response
            int responseCode = conn.getResponseCode();
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            //Converts response to JSONArray
            JSONArray serverData = new JSONArray(response.toString());

            //Checks if there are any servers active
            if(serverData.length() == 0){
                return null;
            }

            //otherwise load them all
            for (int i = 0; i < serverData.length(); i++) {
                servers.add(new ServerData(serverData.getJSONObject(i).getInt("ID"),
                        serverData.getJSONObject(i).getString("Name"),
                        serverData.getJSONObject(i).getString("IP"),
                        serverData.getJSONObject(i).getInt("Port"),
                        (serverData.getJSONObject(i).getInt("Status") == 1)));
            }
        } catch (IOException e) {
            return null;
        }
        return servers;
    }

}
