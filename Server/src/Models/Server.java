package Models;

import Core.DBContextMegaPD;
import Core.Log;
import Core.UserData;
import Helpers.Constants;

import java.net.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 *  Server model, this class represents all the server information
 *
 * @author FilipeA
 * @version 1.1
 */
public class Server{

    //Variables
    //----------------------------------------------------------------------------------------------
    //Private Variables
        //Connection variables
    private int ID;
    private String IP;
    private int port;

        //TCP socket variables
    private ServerSocket serverS;
    private List<UserData> users;

    //----------------------------------------------------------------------------------------------
    //      CONSTRUCTOR'S
    //----------------------------------------------------------------------------------------------

    /**
     * Create the Server acceptance Socket in the desired port
     * @param name desired name
     * @param port desired port
     * @throws Exception if it can't create a new server socket
     */
    public Server(String name, int port) throws Exception {

        //Find machine real IP address
        try(final DatagramSocket socket = new DatagramSocket()){
            try {
                socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            } catch (UnknownHostException e) {
                Log.w("Cant reach local IP [" + e + "]");
            }
            IP = socket.getLocalAddress().getHostAddress();
        } catch (SocketException e) {
            Log.w("Cant reach local IP [" + e + "]");
        }

        //Create Server socket
        serverS = new ServerSocket(port);
        this.port = serverS.getLocalPort();

        serverS.setSoTimeout(Constants.SERVER_TIMEOUT);

        //Start users sockets storage
        users = new ArrayList<>();

        //Register Server in the DB
        this.ID = DBContextMegaPD.getDBContext().registerServer(name, IP, this.port);
    }

    /**
     * Create the Server acceptance Socket (system generates the port)
     * @param name desired name
     * @throws Exception if it can't create a new server socket
     */
    public Server(String name) throws Exception {
        this(name, 0);
    }

    //----------------------------------------------------------------------------------------------
    //      GETTERS
    //----------------------------------------------------------------------------------------------

    public int getID() {
        return ID;
    }

    /**
     * Gets the machine main IP to remote host
     *
     * @return IP
     */
    public String getIP() {
        return IP;
    }

    /**
     * Gets the machine TCP port to remote host
     *
     * @return port
     */
    public int getPort() {
        return port;
    }

    /**
     *
     * @return
     */
    public ServerSocket getServerSocket() {
        return serverS;
    }

    public List<UserData> getUsers() {
        return users;
    }

    //----------------------------------------------------------------------------------------------
    //      SETTERS
    //----------------------------------------------------------------------------------------------

    //----------------------------------------------------------------------------------------------
    //      Methods
    //----------------------------------------------------------------------------------------------

    // Public Methods ------------------------------------------------------------------------------

    // Private Methods -----------------------------------------------------------------------------
}
