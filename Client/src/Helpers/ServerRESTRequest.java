package Helpers;

import Models.ServerInfo;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public final class ServerRESTRequest {

    //TODO: use common sense
    //This string is made for development only! should not be pushed to production
    private static String devActiveServers = "http://127.0.0.1:8080/megapd/index.php?url=servers&status=active";

    private static String activeServers = "http://api.filmaluco.cloud/servers?status=active";
    //private static String inactiveServers = "http://api.filmaluco.cloud/servers?status=innactive";
    private static String specificServer = "http://api.filmaluco.cloud/server?id=";


    private static ArrayList<ServerInfo> jsonRequest(String uri) throws IOException, IllegalAccessException {
        ArrayList<ServerInfo> serverInfo = new ArrayList<>();

        try {
            URL url = new URL(uri);
            StringBuilder resp=new StringBuilder();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milisegundos */);
            conn.setConnectTimeout(15000 /* milisegundos */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();

            InputStream is=conn.getInputStream();
            BufferedReader br=new BufferedReader(new InputStreamReader(is));
            String line;
            while ( (line = br.readLine()) != null ) {
                resp.append(line + "\n");
            }
            JSONArray json = null;
            try {
                 json = new JSONArray(resp.toString());
            }catch (Exception e){
                throw new IllegalAccessException("No active servers are avaiable");
            }

            if(json.length() == 0) throw new IllegalAccessException("No active servers are avaiable");

            for (int i = 0; i < json.length(); i++) {
                JSONObject jsonobject = json.getJSONObject(i);
                boolean status = jsonobject.getInt("Status") == 1;
                String address = jsonobject.getString("IP");
                int port = jsonobject.getInt("Port");
                int id = jsonobject.getInt("ID");
                String name = jsonobject.getString("Name");

                serverInfo.add(new ServerInfo(id, port, status, address, name));
            }


        } catch (IOException e) {
          throw new IOException("Check your online connection");
        }

        return serverInfo;
    }

    public static ServerInfo getServer(int id) throws IOException {
        try {
            return jsonRequest(specificServer + id).get(0);
        }catch (Exception e){
            throw new IOException("Failed to get server with ID=" + id);
        }
    }

    public static ServerInfo getFirst(boolean devMode) throws IOException {
        try {
            return devMode ? jsonRequest(devActiveServers).get(0) : jsonRequest(activeServers).get(0);
        }catch (IOException | IllegalAccessException e){
            throw new IOException(e.getMessage());
        }
    }
}
