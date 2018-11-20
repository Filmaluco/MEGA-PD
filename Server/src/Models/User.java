package Models;

import java.io.Serializable;
import java.net.DatagramSocket;
import java.net.Socket;
import java.util.List;

/**
 *
 *  User model, this class represents all the user information <br>
 *  <i>this model needs to be synced between client and server</i>
 *
 * @author FilipeA
 * @version 1.0
 */
public class User implements Serializable {

    public final static long serialVersionUID = 1L;

    //Variables
    //----------------------------------------------------------------------------------------------
    //Private Variables
    private int ID;
    private String username;
    private String hashedPassword;
    private String directory;
        //Network related variables
        private String IP;
            //Main server (connection module)
        private int serverID;
        private transient Socket connectionSocket;
        private int connectionPort;
            //Other servers (ping <-> broadcast module)
        private transient DatagramSocket pingSocket;
        private int pingPort;
        private int errorCount;
            //Notifications module
        private transient Socket notificationSocket;
        private int notificationPort;
            //Chat module
        private transient Socket chatSocket;
        private int chatPort;
            //FileManager module
        private transient Socket fileManagerSocket;
        private int fileManagerPort;
            //FileTransfer module
        private transient List<Socket> fileTransfers;



    //Public Variables


    //----------------------------------------------------------------------------------------------
    //      CONSTRUCTOR'S
    //----------------------------------------------------------------------------------------------


    //----------------------------------------------------------------------------------------------
    //      GETTERS
    //----------------------------------------------------------------------------------------------

    /**
     * Returns the user ID
     * @return ID
     */
    public int getID() {
        return ID;
    }

    /**
     * Returns the username
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Returns the user current directory name
     * @return directory name
     */
    public String getDirectory() {
        return directory;
    }

    /**
     * Returns the socket responsible for the connection between the main server and the user
     * @return connectionSocket
     */
    public Socket getConnectionSocket() {
        return connectionSocket;
    }

    /**
     * Returns the UDP socket responsible for the ping system
     * @return pingSocket
     */
    public DatagramSocket getPingSocket() {
        return pingSocket;
    }

    /**
     * Returns the user current IP address
     * @return user ip
     */
    public String getIP() {
        return IP;
    }


    //----------------------------------------------------------------------------------------------
    //      SETTERS
    //----------------------------------------------------------------------------------------------

    /**
     * sets the user ConnectionSocket (TCP) <br>
     * this socket is used to keep the connection between the user and the mainServer
     * @param connectionSocket
     */
    public void setConnectionSocket(Socket connectionSocket) {
        this.connectionSocket = connectionSocket;
    }

    /**
     * sets the user PingsSocket (UDP) <br>
     * this socket is used to verify if the user is still connected
     * @param pingSocket datagramSocket to associate to the user
     */
    public void setPingSocket(DatagramSocket pingSocket) {
        this.pingSocket = pingSocket;
    }

    /**
     * Sets the user IP
     * @param IP
     */
    public void setIP(String IP) {
        this.IP = IP;
    }

    /**
     * associates the user with a hashed password
     * @see Helpers.PasswordHasher on how to hash a password
     * @param hashedPassword to associate with the user
     */
    public void setHashedPassword(String hashedPassword){this.hashedPassword = hashedPassword; }

    //----------------------------------------------------------------------------------------------
    //      Methods
    //----------------------------------------------------------------------------------------------

    // Public Methods ------------------------------------------------------------------------------

    /**
     * Locks future access to the user
     */
    public void lock(){
        throw new UnsupportedOperationException("Operation not implemented yet");
    }

    /**
     * Unlocks access to the user
     */
    public void unlock(){
        throw new UnsupportedOperationException("Operation not implemented yet");
    }

    // Private Methods -----------------------------------------------------------------------------
}
