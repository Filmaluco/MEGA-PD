package Modules;


import Models.ServerData;
import PD.Core.Log;
import PD.Core.User;
import PD.Core.Module;
import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class Connection implements Runnable, Module.ConnectionModule {


    private User user;
        //Control variables
    private boolean status = true;
        //TCP socket variables
    private final int TIMEOUT = 5000;
    private ServerSocket notificationServerSocket;
    private Socket userNotificationSocket = null;
    private ServerSocket chatServerSocket;
    private Socket userChatSocket = null;
    private ServerSocket fileManagerServerSocket;
    private Socket userFileManagerSocket = null;
    private Socket connectionSocket;

    //List of active servers
    private List<ServerData> servers;

    public Connection(User user) {
        this.user = user;
        this.servers = new ArrayList<>();
        try {
            this.chatServerSocket = new ServerSocket(0);
            } catch (IOException e) { Log.w("Chat server socket couldn't be created:/n" + e); }
        try {
            this.notificationServerSocket = new ServerSocket(0);
        } catch (IOException e) { Log.w("Notification server socket couldn't be created:/n" + e); }
        try {
            this.fileManagerServerSocket = new ServerSocket(0);
        } catch (IOException e) { Log.w("File manager server socket couldn't be created:/n" + e); }

        //user.setChatPort(chatServerSocket.getLocalPort());
        //user.setNotificationPort(notificationServerSocket.getLocalPort());
        //user.setFileManagerPort(fileManagerServerSocket.getLocalPort());

    }

    //----------------------------------------------------------------------------------------------
    //      Runnable
    //----------------------------------------------------------------------------------------------

    @Override
    public void run() {

        try {
            loadServers();
        } catch (IOException e) {
            Log.exit("[CONNECTION] Cannot connect with database [" + e + "]");
        }

        try {
            connectionSocket = new Socket(servers.get(0).getIP(), servers.get(0).getPort());
        } catch (IOException e) {
            e.printStackTrace();
            Log.w("Could reache server " + servers.get(0).getName());
        }

        Log.i("Connection sucess");
/*
                try {
                    userNotificationSocket = notificationServerSocket.accept();
                } catch (IOException e) { Log.w("Failed to connect to the notification module:\n" + e); }
                try {
                    userChatSocket = chatServerSocket.accept();
                } catch (IOException e) { Log.w("Failed to connect to the chat module:\n" + e); }
                try {
                    userFileManagerSocket = fileManagerServerSocket.accept();
                } catch (IOException e) { Log.w("Failed to connect to the file manager module:\n" + e); }

                user.setConnectionSocket(connectionSocket); // check if this is correct
                user.setNotificationSocket(userNotificationSocket);
                user.setChatSocket(userChatSocket);
                user.setFileManagerSocket(userFileManagerSocket);
*/
    }

    //----------------------------------------------------------------------------------------------
    //      Methods
    //----------------------------------------------------------------------------------------------

    // Public Methods ------------------------------------------------------------------------------


    /**
     * Stops connection module from accepting more requests
     */
    public void close() {
        status = false;
    }

    @Override
    public boolean login(User user){
        String hashedPassword = user.getPassword();
        String directory = System.getProperty("user.dir");

        return true;
    }

    @Override
    public void logout(User user) {

    }
    // check if user exists in the server
        // for servers[i]
        // if(
        //connect him if he does and tell him to fuck off otherwise

    // Private Methods -----------------------------------------------------------------------------

    /**
     * Loads the list of active servers from the database to the list so the user can choose one to access.
     * @throws IOException if it fails to connect to the database.
     */
    private void loadServers() throws IOException {
            URL url = new URL("https://api.filmaluco.cloud/servers?status=active");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            int responseCode = conn.getResponseCode();
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while((inputLine = in.readLine())!= null){
                response.append(inputLine);
            }
            JSONArray serverData = new JSONArray(response.toString());
            //if(serverData.length()==0)
                //TODO: Dar warning ao user que não há servidores disponiveis
            for (int i = 0; i < serverData.length(); i++) {
                servers.add(new ServerData(serverData.getJSONObject(i).getInt("ID"),
                        serverData.getJSONObject(i).getString("Name"),
                        serverData.getJSONObject(i).getString("IP"),
                        serverData.getJSONObject(i).getInt("Port"),
                        (serverData.getJSONObject(i).getInt("Status")==1)));
            }

    }

    //TODO:LoadServer mas só devolver um
}
